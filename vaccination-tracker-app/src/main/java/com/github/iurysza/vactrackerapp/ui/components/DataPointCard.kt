package com.github.iurysza.vactrackerapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.iurysza.vactrackerapp.ui.theme.ColorPrimary
import com.github.iurysza.vactrackerapp.ui.theme.ColorProgress

@Composable
fun DataPointCard(
  value: String,
  label: String
) {
  Card(
    modifier = Modifier
      .height(94.dp)
      .padding(4.dp)
      .clickable { },
    elevation = 0.dp
  ) {
    Column(
      modifier = Modifier.background(color = ColorPrimary),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      TitleText(value)
      LabelText(label)
    }
  }
}

@Composable
private fun LabelText(label: String) {
  Text(
    buildAnnotatedString {
      withStyle(
        style = SpanStyle(
          fontWeight = FontWeight.W900,
          color = ColorProgress,
        )
      ) {
        append(label)
      }
    },
    fontSize = 16.sp,
    textAlign = TextAlign.Center,
  )
}

@Composable
private fun TitleText(value: String) {
  Text(
    buildAnnotatedString {
      withStyle(
        style = SpanStyle(
          fontWeight = FontWeight.W900,
          color = Color.White
        )
      ) {
        append(value)
      }
    },
    fontSize = 25.sp,
    textAlign = TextAlign.Center
  )
}

@Preview
@Composable
fun DataCardPreview() {
  DataPointCard(
    value = "25.0",
    label = "Label"
  )
}

