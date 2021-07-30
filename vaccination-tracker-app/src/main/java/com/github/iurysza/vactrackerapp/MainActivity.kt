package com.github.iurysza.vactrackerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.github.iurysza.vaccinationtracker.VaccinationTracker
import com.github.iurysza.vaccinationtracker.cache.DatabaseDriverFactory
import com.github.iurysza.vactrackerapp.ui.home.AppHomeViewModel
import com.github.iurysza.vactrackerapp.ui.home.HomeScreen
import com.github.iurysza.vactrackerapp.ui.theme.AndroidClientTheme

class MainActivity : ComponentActivity() {

  private val sdk = VaccinationTracker(databaseDriverFactory = DatabaseDriverFactory(this))

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      AndroidClientTheme {
        HomeScreen(
          AppHomeViewModel(this, sdk)
        )
      }
    }
  }
}
