package com.github.iurysza.vactrackerapp.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.github.iurysza.vactrackerapp.R
import com.github.iurysza.vactrackerapp.ui.components.ExpandableCard
import com.github.iurysza.vactrackerapp.ui.components.FakeModels
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@Composable
fun HomeScreen(
  viewModel: HomeViewModel,
  state: HomeState? = null,
  selectedId: List<String>? = null,
) {
  val screenState = state ?: viewModel.stateFlow.collectAsState().value
  val expandedCardIds = selectedId ?: viewModel.expandedCardIdsList.collectAsState().value

  Scaffold(
    topBar = {
      TopAppBar(
        navigationIcon = {},
        title = {
          Text(
            text = "Vacinação COVID 19 ",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
          )
        },
        actions = {
          AppIconButton(
            iconId = R.drawable.ic_refresh,
            onClick = { viewModel.getVaccinationData(true) }
          )
        }
      )
    }) {
    when (screenState) {
      HomeState.Error -> {
        Box(
          modifier = Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center
        ) {
          Text("Algo de Errado Aconteceu")
        }
      }
      HomeState.Loading -> {
        FullScreenProgress()
      }
      is HomeState.Success -> {
        LazyColumn {
          itemsIndexed(
            screenState.modelList
          ) { _, card ->
            ExpandableCard(
              card = card,
              onCardArrowClick = { viewModel.onCardArrowClicked(card.name) },
              expanded = expandedCardIds.contains(card.name),
            )
          }
        }
      }
    }
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
private fun FullScreenProgress() {
  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    CircularProgressIndicator()
  }
}

@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@Preview
@Composable
fun PreviewCardScreen() {
  val state = FakeModels.vaccinationCardModelList(HomeState.Success(emptyList()))
  val selectedIds = (state as? HomeState.Success)?.modelList?.take(1)?.map { it.name }

  HomeScreen(
    viewModel = FakeHomeModel(),
    state = state,
    selectedId = selectedIds
  )
}
