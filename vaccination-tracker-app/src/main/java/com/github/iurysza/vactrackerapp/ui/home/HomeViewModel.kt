package com.github.iurysza.vactrackerapp.ui.home

import kotlinx.coroutines.flow.MutableStateFlow

interface HomeViewModel {

  val bottomSheetState: MutableStateFlow<StateVaccinationCardModel?>
  val state: MutableStateFlow<HomeScreenState>
  val expandedCardIdsList: MutableStateFlow<List<String>>
  val average14daysToggle: MutableStateFlow<Boolean>
  val sortedByValue: MutableStateFlow<Boolean>
  val totalToggle: MutableStateFlow<Boolean>
  val isSortEnabled: MutableStateFlow<Boolean>
  val dailyToggle: MutableStateFlow<Boolean>

  fun get14DaysAverageVaccinationData()
  fun getDailyVaccinationData()
  fun getTotalVaccinationData(forceReload: Boolean = false)

  fun onToggleExpand(itemId: String)
  fun refresh()
  fun toggleSort()
  fun onItemClicked(stateVaccinationCardModel: StateVaccinationCardModel)
}