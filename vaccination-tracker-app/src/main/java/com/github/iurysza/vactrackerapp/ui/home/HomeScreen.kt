package com.github.iurysza.vactrackerapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun HomeScreen(
  viewModel: HomeViewModel,
  state: HomeScreenState? = null,
  selectedId: List<String>? = null,
) {
  val isSorted = viewModel.sortedByValue.collectAsState().value
  val hasSortToggle = viewModel.isSortEnabled.collectAsState().value

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
          if (hasSortToggle) {
            AppIconButton(
              iconId = if (!isSorted) R.drawable.ic_sort else R.drawable.ic_sort_by_alpha,
              onClick = { viewModel.toggleSort() }
            )
          }
        })
    }
  ) {

    val screenState = state ?: viewModel.state.collectAsState().value
    val expandedCardIds = selectedId ?: viewModel.expandedCardIdsList.collectAsState().value

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
            itemsIndexed(screenState.modelList) { _, card ->
              ExpandableCard(
                model = card,
                onCardArrowClick = { viewModel.onToggleExpand(card.name) },
                expanded = expandedCardIds.contains(card.name),
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun ToggleButton(
  onChecked: (Boolean) -> Unit,
  checked: Boolean
) = Box(
  modifier = Modifier.fillMaxWidth(),
  contentAlignment = Alignment.Center
) {
  ToggleButton(
    "Ordenado",
    icon = Icons.Filled.KeyboardArrowDown,
    onChecked = onChecked,
    checked = checked,
  )
}

@Composable
private fun HeaderMenu(viewModel: HomeViewModel) = Card(
  backgroundColor = cardExpandedBackgroundColor,
  contentColor = ColorPrimary,
  elevation = 24.dp,
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Center
  ) {
    ToggleButton(
      "Total",
      Icons.Filled.DateRange,
      checked = viewModel.totalToggle.collectAsState().value,
      onChecked = { viewModel.getTotalVaccinationData() }
    )
    ToggleButton(
      "14 dias",
      Icons.Filled.DateRange,
      checked = viewModel.average14daysToggle.collectAsState().value,
      onChecked = { viewModel.get14DaysAverageVaccinationData() }
    )
    ToggleButton(
      "Diário",
      Icons.Filled.DateRange,
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
