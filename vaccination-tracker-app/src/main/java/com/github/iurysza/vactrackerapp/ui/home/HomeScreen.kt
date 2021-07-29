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
import com.github.iurysza.vactrackerapp.ui.theme.AndroidClientTheme
import com.github.iurysza.vactrackerapp.ui.theme.ColorPrimary
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
  val screenState = viewModel.stateFlow.collectAsState().value
  val expandedCardIds = viewModel.expandedCardIdsList.collectAsState().value

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
          IconButton(
            onClick = { viewModel.getVaccinationData(true) },
            content = {
              Icon(
                painter = painterResource(id = R.drawable.ic_refresh),
                contentDescription = "Atualizar",
              )
            },
          )
        }
      )
    }) {
    when (screenState) {
      State.Error -> Text("Error")
      State.Loading -> {
        Box(
          modifier = Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center
        ) {
          CircularProgressIndicator()
        }
      }
      is State.Success -> {
        LazyColumn {
          itemsIndexed(
            screenState.vaccinationDataList
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

@ExperimentalFoundationApi
@Preview
@Composable
fun PreviewCardScreen() {
  val screenState = FakeModels.vaccinationCardModelList(State.Loading)

  AndroidClientTheme {
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
            Text(text = "Refresh", color = ColorPrimary)
          }
        )
      }) {
      when (screenState) {
        State.Error -> Text("Error")
        State.Loading ->
          Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
          ) {
            CircularProgressIndicator()
          }
        is State.Success ->
          LazyColumn {
            itemsIndexed(
              screenState.vaccinationDataList
            ) { index, card ->
              ExpandableCard(
                card = card,
                onCardArrowClick = {},
                expanded = index == 0,
              )
            }
          }
      }
    }
  }
}
