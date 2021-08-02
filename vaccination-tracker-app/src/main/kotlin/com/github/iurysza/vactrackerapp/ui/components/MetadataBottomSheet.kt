package com.github.iurysza.vactrackerapp.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.iurysza.vactrackerapp.ui.home.BottomSheetModel
import com.github.iurysza.vactrackerapp.ui.theme.ColorHeader
import com.github.iurysza.vactrackerapp.ui.theme.ColorPrimary
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@Composable
@ExperimentalMaterialApi
fun MetadataBottomSheet(
  model: BottomSheetModel?,
  content: @Composable () -> Unit = {},
) {
  val sheetState = rememberModalBottomSheetState(
    initialValue = ModalBottomSheetValue.Hidden
  )

  val scope = rememberCoroutineScope()
  SideEffect {
    scope.launch {
      if (model == null) {
        sheetState.hide()
      } else {
        sheetState.animateTo(ModalBottomSheetValue.Expanded)
      }
    }
  }
  ModalBottomSheetLayout(
    sheetElevation = 24.dp,
    modifier = Modifier.padding(horizontal = 4.dp),
    sheetShape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
    sheetState = sheetState,
    sheetContent = { MetadataContent(model) },
    content = content,
  )
}

@Composable
private fun MetadataContent(model: BottomSheetModel?) {
  Text("")
  if (model != null) {
    Column(
      modifier = Modifier
        .heightIn(max = 200.dp)
        .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
        .fillMaxWidth()
    ) {
      Text(
        buildAnnotatedString {
          withStyle(style = SpanStyle(fontWeight = FontWeight.W500, color = ColorHeader)) {
            append("Dados Oficiais de ${model.name}")
          }
        },
        modifier = Modifier.wrapContentWidth(),
        fontSize = 18.sp
      )
      Spacer(modifier = Modifier.padding(vertical = 8.dp))
      Text(
        buildAnnotatedString {
          withStyle(style = SpanStyle(fontWeight = FontWeight.W500, color = ColorHeader)) {
            append("Última atualização: ${model.lastUpdate}")
          }
        },
        modifier = Modifier.wrapContentWidth(),
        fontSize = 12.sp
      )
      Spacer(modifier = Modifier.padding(vertical = 1.dp))

      val uriHandler = LocalUriHandler.current
      val annotatedLinkString = annotatedLinkString(model)
      ClickableText(
        modifier = Modifier.fillMaxWidth(),
        text = annotatedLinkString,
        onClick = {
          annotatedLinkString
            .getStringAnnotations("URL", it, it)
            .firstOrNull()?.let { stringAnnotation ->
              uriHandler.openUri(stringAnnotation.item)
            }
        }
      )
    }
  }
}

fun annotatedLinkString(model: BottomSheetModel) = buildAnnotatedString {
  val label = "Ir para fonte"
  append(label)
  addStyle(
    style = SpanStyle(
      color = ColorPrimary,
      fontSize = 12.sp,
    ),
    start = 0,
    end = label.length
  )
  addStringAnnotation(
    tag = "URL",
    annotation = model.sourceWebsite,
    start = 0,
    end = model.sourceWebsite.length
  )
}

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
@Preview
fun ModalSheet() {
  MetadataContent(
    BottomSheetModel(
      name = "Minas Gerais",
      lastUpdate = "2021-08-13",
      sourceName = "Source",
      sourceWebsite = "https://coronavirus.saude.mg.gov.br/vacinometro"
    )
  )
}