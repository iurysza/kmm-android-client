package com.github.iurysza.vactrackerapp.ui.components

import com.github.iurysza.vactrackerapp.DataPoint
import com.github.iurysza.vactrackerapp.R
import com.github.iurysza.vactrackerapp.StateVaccinationCardModel
import java.util.UUID
import kotlin.random.Random

object FakeModels {

  fun vaccinationCardModelList(): List<StateVaccinationCardModel> {
    return (0..20).map {
      StateVaccinationCardModel(
        icon = R.drawable.ic_flag_rj,
        name = randomString(10),
        coverage = .2f,
        dataList = listOf(
          DataPoint(
            Random.nextInt(100).toString(),
            "1st Dose"
          ),
          DataPoint(
            Random.nextInt(100).toString(),
            "1st Dose %"
          ),
          DataPoint(
            Random.nextInt(100).toString(),
            "2nd Dose"
          ),
          DataPoint(
            Random.nextInt(100).toString(),
            "2nd Dose %"
          ),
          DataPoint(
            Random.nextInt(100).toString(),
            "Fully Vaccinated"
          ),
          DataPoint(
            Random.nextInt(100).toString(),
            "Fully Vaccinated %"
          ),
        )

      )
    }
  }

  fun model(): StateVaccinationCardModel = StateVaccinationCardModel(
    icon = R.drawable.ic_flag_mg,
    name = "Minas Gerais",
    coverage = .2f,
    dataList = (1 until 7).map {
      DataPoint(
        Random.nextInt(100).toString(),
        "Label $it"
      )
    }
  )

  private fun randomString(size: Int): String = UUID.randomUUID().toString().take(size)
}