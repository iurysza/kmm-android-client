package com.github.iurysza.vactrackerapp.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import com.github.iurysza.vactrackerapp.CardsViewModel
import com.github.iurysza.vactrackerapp.ui.components.ExpandableCard
import com.github.iurysza.vactrackerapp.ui.components.FakeModels
import com.github.iurysza.vactrackerapp.ui.theme.AndroidClientTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@Composable
fun CardsScreen(viewModel: CardsViewModel) {
  val cards = viewModel.cards.collectAsState()
  val expandedCardIds = viewModel.expandedCardIdsList.collectAsState()

  Scaffold {
    LazyColumn {
      itemsIndexed(cards.value) { _, card ->
        ExpandableCard(
          card = card,
          onCardArrowClick = { viewModel.onCardArrowClicked(card.name) },
          expanded = expandedCardIds.value.contains(card.name),
        )
      }
    }
  }
}

@ExperimentalFoundationApi
@Preview
@Composable
fun PreviewCardScreen() {
  val cards = FakeModels.vaccinationCardModelList()

  AndroidClientTheme {
    Scaffold(
    ) {
      LazyColumn {
        itemsIndexed(cards) { index, card ->
          ExpandableCard(
            card = card,
            onCardArrowClick = {},
            expanded = index == 0
          )
        }
      }
    }
  }
}
