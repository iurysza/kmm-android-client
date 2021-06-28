package com.iurysza.learn.androidclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.iurysza.learn.kmmpoc.shared.HackernewsSDK
import com.iurysza.learn.kmmpoc.shared.cache.DatabaseDriverFactory
import com.iurysza.learn.kmmpoc.shared.cache.HitStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  private val sdk = HackernewsSDK(databaseDriverFactory = DatabaseDriverFactory(this))
  private val articleList = MutableStateFlow<List<HitStore>>(emptyList())

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    lifecycleScope.launch {
      articleList.value = sdk.getArticles(forceReload = false)
      setContent {
        ArticleScreen(articleList)
      }
    }
  }
}