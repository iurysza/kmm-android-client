package com.github.iurysza.vactrackerapp

import android.content.Context
import android.icu.text.NumberFormat
import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.iurysza.vaccinationtracker.VaccinationTracker
import com.github.iurysza.vaccinationtracker.cache.CovidVaccinationData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

class CardsViewModel(context: Context, private val sdk: VaccinationTracker) : ViewModel() {

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

