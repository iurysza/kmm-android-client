package com.github.iurysza.vactrackerapp.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.iurysza.vaccinationtracker.VaccinationTracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AppHomeViewModel(
  private val context: Context,
  private val sdk: VaccinationTracker
) : HomeViewModel, ViewModel() {

  override val state = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
  override val expandedCardIdsList = MutableStateFlow(emptyList<String>())

  override val bottomSheetState = MutableStateFlow<BottomSheetModel?>(null)
  override val totalToggle = MutableStateFlow(true)
  override val isSortEnabled = MutableStateFlow(false)
  override val dailyToggle = MutableStateFlow(false)
  override val sortedByValue = MutableStateFlow(false)
  override val average14daysToggle = MutableStateFlow(false)

  private val fullVaccinationDataCache by lazy {
    viewModelScope.async {
      // state.emit(HomeScreenState.Loading)
      sdk.getFullVaccinationData()
    }
  }

  init {
    viewModelScope.launch {
      fullVaccinationDataCache.await()
    }
    getTotalVaccinationData()
  }

  override fun getTotalVaccinationData(forceReload: Boolean) {
    viewModelScope.launch(Dispatchers.IO) {

      isSortEnabled.emit(true)
      totalToggle.emit(true)
      average14daysToggle.emit(false)
      dailyToggle.emit(false)

      runCatching {
        sdk.getVaccinationData(latest = forceReload).mapToUiModel(context, getDrawableByName)
      }.onSuccess {
        state.emit(HomeScreenState.Success(it))
      }.onFailure { state.emit(HomeScreenState.Error) }
    }
  }

  override fun get14DaysAverageVaccinationData() {
    viewModelScope.launch(Dispatchers.IO) {
      average14daysToggle.emit(true)
      isSortEnabled.emit(false)
      totalToggle.emit(false)
      dailyToggle.emit(false)

      runCatching {
        fullVaccinationDataCache.await().fromAverage14DaysToUiModel(context, getDrawableByName)
      }.onSuccess {
        state.emit(HomeScreenState.Success(it))
      }.onFailure {
        state.emit(HomeScreenState.Error)
      }
    }
  }

  override fun getDailyVaccinationData() {
    viewModelScope.launch(Dispatchers.IO) {
      dailyToggle.emit(true)
      isSortEnabled.emit(false)
      average14daysToggle.emit(false)
      totalToggle.emit(false)
      onDismiss()

      runCatching {
        fullVaccinationDataCache.await().fromDailyToUiModel(context, getDrawableByName)
      }.onSuccess {
        state.emit(HomeScreenState.Success(it))
      }.onFailure {
        state.emit(HomeScreenState.Error)
      }
    }
  }

  override fun onItemClicked(stateVaccinationCardModel: StateVaccinationCardModel) {
    viewModelScope.launch {
      val name = stateVaccinationCardModel.name
      bottomSheetState.emit(
        fullVaccinationDataCache.await().find { it.state == name }?.let {
          BottomSheetModel(
            name = name,
            sourceName = it.sourceName,
            sourceWebsite = it.sourceWebsite,
            lastUpdate = it.lastUpdateDate
          )
        }
      )
    }
  }

  override fun toggleSort() {
    val modelList = (state.value as? HomeScreenState.Success)?.modelList ?: return

    viewModelScope.launch(Dispatchers.Default) {
      state.emit(HomeScreenState.Success(
        if (sortedByValue.value) {
          sortedByValue.emit(false)
          modelList.sortedBy { it.name }
        } else {
          sortedByValue.emit(true)
          modelList.sortedByDescending { it.coverage }
        }))
    }
  }

  override fun refresh() {
    when {
      totalToggle.value -> getTotalVaccinationData(true)
      average14daysToggle.value -> get14DaysAverageVaccinationData()
      dailyToggle.value -> getDailyVaccinationData()
    }
  }

  override fun onToggleExpand(itemId: String) {
    expandedCardIdsList.value = expandedCardIdsList.value.toMutableList().also { list ->
      if (list.contains(itemId)) {
        list.remove(itemId)
      } else {
        list.add(itemId)
      }
    }
  }

  override fun onDismiss() {
    viewModelScope.launch {
      // bottomSheetState.emit(null)
    }
  }
}

private val getDrawableByName: (Context, String) -> Int = { context, isoCode ->
  context.resources.getIdentifier(
    "ic_flag_$isoCode",
    "drawable",
    context.packageName
  )
}

sealed class HomeScreenState {
  data class Success(val modelList: List<StateVaccinationCardModel>) : HomeScreenState()
  object Loading : HomeScreenState()
  object Error : HomeScreenState()
}
