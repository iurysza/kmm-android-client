package com.github.iurysza.vactrackerapp.ui.home

import com.github.iurysza.vactrackerapp.ui.components.FakeModels
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FakeHomeModel : HomeViewModel {

  val value = HomeState.Success(emptyList())
  override val stateFlow = MutableStateFlow<HomeState>(value)

  override val expandedCardIdsList = MutableStateFlow(listOf<String>())

  override fun onCardArrowClicked(cardId: String) {
    expandedCardIdsList.value = expandedCardIdsList.value.toMutableList().also { list ->
      if (list.contains(cardId)) {
        list.remove(cardId)
      } else {
        list.add(cardId)
      }
    }
  }

  override fun getVaccinationData(forceReload: Boolean) {
    GlobalScope.launch {
      stateFlow.emit(HomeState.Loading)
      delay(300)
      stateFlow.emit(FakeModels.vaccinationCardModelList(HomeState.Success(emptyList())))
    }
  }
}