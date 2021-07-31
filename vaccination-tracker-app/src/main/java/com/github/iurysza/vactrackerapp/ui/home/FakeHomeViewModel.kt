package com.github.iurysza.vactrackerapp.ui.home

import com.github.iurysza.vactrackerapp.ui.components.FakeModels
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FakeHomeViewModel : HomeViewModel {
  override val isSortEnabled: MutableStateFlow<Boolean> = MutableStateFlow(true)
  val value = HomeScreenState.Success(emptyList())
  override val bottomSheetState: MutableStateFlow<StateVaccinationCardModel?>
    get() = TODO("Not yet implemented")
  override val state = MutableStateFlow<HomeScreenState>(value)

  override val expandedCardIdsList = MutableStateFlow(listOf<String>())
  override val average14daysToggle: MutableStateFlow<Boolean> = MutableStateFlow(false)
  override val totalToggle: MutableStateFlow<Boolean> = MutableStateFlow(false)
  override val sortedByValue: MutableStateFlow<Boolean> = MutableStateFlow(false)
  override val dailyToggle: MutableStateFlow<Boolean> = MutableStateFlow(false)

  override fun get14DaysAverageVaccinationData() {
    TODO("Not yet implemented")
  }

  override fun refresh() {
    TODO("Not yet implemented")
  }

  override fun toggleSort() {
    TODO("Not yet implemented")
  }

  override fun onItemClicked(stateVaccinationCardModel: StateVaccinationCardModel) {
    TODO("Not yet implemented")
  }

  override fun onToggleExpand(cardId: String) {
    expandedCardIdsList.value = expandedCardIdsList.value.toMutableList().also { list ->
      if (list.contains(cardId)) {
        list.remove(cardId)
      } else {
        list.add(cardId)
      }
    }
  }

  override fun getDailyVaccinationData() {
    TODO("Not yet implemented")
  }

  override fun getTotalVaccinationData(forceReload: Boolean) {
    GlobalScope.launch {
      state.emit(HomeScreenState.Loading)
      delay(300)
      state.emit(FakeModels.vaccinationCardModelList())
    }
  }
}