package com.github.iurysza.vactrackerapp.ui.home

import android.content.Context
import android.icu.text.NumberFormat
import androidx.compose.runtime.Immutable
import com.github.iurysza.vaccinationtracker.cache.CovidVaccinationData

fun List<CovidVaccinationData>.mapToUiModel(
  context: Context,
  getIconDrawable: (Context, String) -> Int
): List<StateVaccinationCardModel> {
  return map {
    StateVaccinationCardModel(
      icon = getIconDrawable(context, it.isoCode),
      name = it.state,
      coverage = it.fullyVaccinatedPercentage.toFloat() / 100,
      dataList = listOf(
        DataPoint(
          it.firstDose.formatNumber(),
          "1ª dose"
        ),
        DataPoint(
          "${it.firstDosePercentage}%",
          "1ª dose"
        ),
        DataPoint(
          it.secondDose.formatNumber(),
          "2ª dose"
        ),
        DataPoint(
          "${it.secondDosePercentage}%",
          "2ª dose"
        ),
        DataPoint(
          it.fullyVaccinated.formatNumber(),
          "Imunizados"
        ),
        DataPoint(
          "${it.fullyVaccinatedPercentage}%",
          "Imunizados"
        ),
      )
    )

  }
}

fun Long.formatNumber(): String = NumberFormat.getInstance().format(this)

@Immutable
data class StateVaccinationCardModel(
  val icon: Int,
  val coverage: Float,
  val name: String,
  val dataList: List<DataPoint>
)

@Immutable
data class DataPoint(
  val value: String,
  val label: String,
)
