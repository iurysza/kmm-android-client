package com.github.iurysza.vactrackerapp.ui.home

import kotlinx.coroutines.flow.MutableStateFlow

interface HomeViewModel {

  val stateFlow: MutableStateFlow<HomeState>
  val expandedCardIdsList: MutableStateFlow<List<String>>

  fun getVaccinationData(forceReload: Boolean = false)
  fun onCardArrowClicked(name: String)
}