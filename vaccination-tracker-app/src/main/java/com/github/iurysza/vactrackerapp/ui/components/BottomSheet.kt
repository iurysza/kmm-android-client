package com.github.iurysza.vactrackerapp.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.iurysza.vactrackerapp.ui.home.PreviewCardScreen
import com.github.iurysza.vactrackerapp.ui.home.StateVaccinationCardModel
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@Composable
@ExperimentalMaterialApi
fun ModelBottomSheet(
  model: StateVaccinationCardModel,
  content: @Composable () -> Unit,
  onDismiss: () -> Unit
) {
  val modalBottomSheetState = rememberModalBottomSheetState(
    initialValue = ModalBottomSheetValue.Hidden
  )

  val scope = rememberCoroutineScope()
  ModalBottomSheetLayout(
    modifier = Modifier.noRippleClickable {
      scope.launch { modalBottomSheetState.hide() }
      onDismiss()
    },
    sheetState = modalBottomSheetState,
    sheetContent = {
      Text(text = model.name)
      ContentGrid(visible = true, dataList = model.dataList, onListClicked = onDismiss)
    },
    content = content,
  )
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
@Preview
fun ModalSheet() {
  PreviewCardScreen()
}