package com.github.iurysza.vactrackerapp.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.iurysza.vaccinationtracker.VaccinationTracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val context: Context, private val sdk: VaccinationTracker) :
  ViewModel() {

  val stateFlow = MutableStateFlow<State>(State.Loading)
  val expandedCardIdsList = MutableStateFlow(listOf<String>())

  init {
    getVaccinationData()
  }

  fun getVaccinationData(forceReload: Boolean = false) {
    viewModelScope.launch(Dispatchers.Default) {
      stateFlow.emit(State.Loading)

      runCatching {
        sdk.getVaccinationData(latest = forceReload)
          .mapToUiModel(context, getDrawableByName)
      }.onSuccess {
        Log.d("TAG", "getVaccinationData: $it")
        stateFlow.emit(State.Success(it))
      }.onFailure {
        Log.e("TAG", "getVaccinationData: $it")
        stateFlow.emit(State.Error)
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

  fun onCardArrowClicked(cardId: String) {
    expandedCardIdsList.value = expandedCardIdsList.value.toMutableList().also { list ->
      if (list.contains(cardId)) {
        list.remove(cardId)
      } else {
        list.add(cardId)
      }
    }
  }
}

sealed class State {
  data class Success(val vaccinationDataList: List<StateVaccinationCardModel>) : State()
  object Loading : State()
  object Error : State()
}
