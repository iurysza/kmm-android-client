package com.github.iurysza.vactrackerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import com.github.iurysza.vaccinationtracker.VaccinationTracker
import com.github.iurysza.vaccinationtracker.cache.DatabaseDriverFactory
import com.github.iurysza.vactrackerapp.ui.CardsScreen
import com.github.iurysza.vactrackerapp.ui.theme.AndroidClientTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {

  private val sdk = VaccinationTracker(databaseDriverFactory = DatabaseDriverFactory(this))

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      AndroidClientTheme {
        CardsScreen(CardsViewModel(this, sdk))
      }
    }
  }
}
