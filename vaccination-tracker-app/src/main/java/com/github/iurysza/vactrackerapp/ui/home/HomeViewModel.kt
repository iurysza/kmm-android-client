package com.github.iurysza.vactrackerapp.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.iurysza.vaccinationtracker.VaccinationTracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(context: Context, private val sdk: VaccinationTracker) : ViewModel() {

  val cards = MutableStateFlow(listOf<StateVaccinationCardModel>())
  val expandedCardIdsList = MutableStateFlow(listOf<String>())

  init {
    getVaccinationData(context)
  }

  private fun getVaccinationData(context: Context, forceReload: Boolean = false) {
    viewModelScope.launch(Dispatchers.Default) {
      val vaccinationData = sdk
        .getVaccinationData(latest = forceReload)
        .mapToUiModel(context, getDrawableByName)

      Log.e("TAG", "getVaccinationData: $vaccinationData")
      cards.emit(vaccinationData)
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

