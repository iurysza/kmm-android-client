package com.github.iurysza.vactrackerapp.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.iurysza.vaccinationtracker.VaccinationTracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModelImpl(
  private val context: Context,
  private val sdk: VaccinationTracker
) : HomeViewModel, ViewModel() {

  override val stateFlow = MutableStateFlow<HomeState>(HomeState.Loading)
  override val expandedCardIdsList = MutableStateFlow(listOf<String>())

  init {
    getVaccinationData()
  }

  override fun getVaccinationData(forceReload: Boolean) {
    viewModelScope.launch(Dispatchers.Default) {
      stateFlow.emit(HomeState.Loading)

      runCatching {
        sdk.getVaccinationData(latest = forceReload)
          .mapToUiModel(context, getDrawableByName)
      }.onSuccess {
        Log.d("TAG", "getVaccinationData: $it")
        stateFlow.emit(HomeState.Success(it))
      }.onFailure {
        Log.e("TAG", "getVaccinationData: $it")
        stateFlow.emit(HomeState.Error)
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

  override fun onCardArrowClicked(cardId: String) {
    expandedCardIdsList.value = expandedCardIdsList.value.toMutableList().also { list ->
      if (list.contains(cardId)) {
        list.remove(cardId)
      } else {
        list.add(cardId)
      }
    }
  }
}

sealed class HomeState {
  data class Success(val modelList: List<StateVaccinationCardModel>) : HomeState()
  object Loading : HomeState()
  object Error : HomeState()
}
