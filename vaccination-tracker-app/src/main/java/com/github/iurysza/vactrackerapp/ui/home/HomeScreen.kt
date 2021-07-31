package com.github.iurysza.vactrackerapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.iurysza.vactrackerapp.R
import com.github.iurysza.vactrackerapp.ui.components.ExpandableCard
import com.github.iurysza.vactrackerapp.ui.components.FakeModels
import com.github.iurysza.vactrackerapp.ui.components.ToggleButton
import com.github.iurysza.vactrackerapp.ui.theme.ColorPrimary
import com.github.iurysza.vactrackerapp.ui.theme.cardExpandedBackgroundColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun HomeScreen(
  viewModel: HomeViewModel,
  state: HomeScreenState? = null,
  selectedId: List<String>? = null,
) {
  val isSorted = viewModel.sortedByValue.collectAsState().value
  val hasSortToggle = viewModel.isSortEnabled.collectAsState().value
  val systemUiController = rememberSystemUiController()

  SideEffect {
    systemUiController.setSystemBarsColor(
      color = ColorPrimary,
      darkIcons = false
    )
    systemUiController.setNavigationBarColor(Color.Transparent)
  }
  Scaffold(
    topBar = {
      TopAppBar(
        navigationIcon = {},
        title = {
          Text(
            text = "Vacinação COVID 19",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
          )
        },
        actions = {
          SortToggle(
            hasSortToggle = hasSortToggle,
            isSorted = isSorted,
            onClick = { viewModel.toggleSort() }
          )
        })
    }
  ) {

    val screenState = state ?: viewModel.state.collectAsState().value
    val expandedItemIdList = selectedId ?: viewModel.expandedCardIdsList.collectAsState().value

    Column {
      HeaderMenu(viewModel)
      when (screenState) {
        HomeScreenState.Error -> Box(
          modifier = Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center
        ) {
          Text("Algo de errado aconteceu")
        }
        HomeScreenState.Loading -> FullScreenProgress()
        is HomeScreenState.Success -> {
          LazyColumn {
            itemsIndexed(screenState.modelList) { _, model ->
              ExpandableCard(
                model = model,
                expanded = expandedItemIdList.contains(model.name),
                onCardArrowClick = { viewModel.onToggleExpand(model.name) },
                onItemClicked = { viewModel.onItemClicked(it) },
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun SortToggle(
  hasSortToggle: Boolean,
  isSorted: Boolean,
  onClick: () -> Unit
) {
  if (hasSortToggle) {
    AppIconButton(
      iconId = if (!isSorted) R.drawable.ic_sort else R.drawable.ic_sort_by_alpha,
      onClick = onClick
    )
  } else {
    IconButton(onClick = {}, content = {})
  }
}

@Composable
private fun HeaderMenu(viewModel: HomeViewModel) = Card(
  backgroundColor = cardExpandedBackgroundColor,
  contentColor = ColorPrimary,
  elevation = 0.dp,
  modifier = Modifier
    .fillMaxWidth()
    .padding(vertical = 8.dp)
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Center
  ) {
    ToggleButton(
      "Total",
      R.drawable.ic_total,
      checked = viewModel.totalToggle.collectAsState().value,
      onChecked = { viewModel.getTotalVaccinationData() }
    )
    ToggleButton(
      "14 dias",
      R.drawable.ic_week,
      checked = viewModel.average14daysToggle.collectAsState().value,
      onChecked = { viewModel.get14DaysAverageVaccinationData() }
    )
    ToggleButton(
      "Hoje",
      R.drawable.ic_day,
      checked = viewModel.dailyToggle.collectAsState().value,
      onChecked = { viewModel.getDailyVaccinationData() }
    )
  }
}

@Composable
fun AppIconButton(
  onClick: () -> Unit,
  iconId: Int,
  description: String = ""
) = IconButton(
  onClick = onClick,
  content = {
    Icon(
      painter = painterResource(id = iconId),
      contentDescription = description,
    )
  },
)

@Composable
fun FullScreenProgress() {
  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    CircularProgressIndicator()
  }
}

@Preview
@Composable
fun PreviewCardScreen() {
  val state = FakeModels.vaccinationCardModelList()
  val selectedIds = (state as? HomeScreenState.Success)?.modelList?.take(1)?.map { it.name }

  HomeScreen(
    viewModel = FakeHomeViewModel(),
    state = state,
    selectedId = selectedIds
  )
}
