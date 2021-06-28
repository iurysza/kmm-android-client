package com.iurysza.learn.androidclient

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iurysza.learn.androidclient.ui.theme.AndroidClientTheme
import com.iurysza.learn.kmmpoc.shared.cache.HitStore
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

@Composable
fun ArticleScreen(hitStore: MutableStateFlow<List<HitStore>>) {
  Log.e("TAG", "ArticleScreen:${hitStore.value}", )
  Scaffold(
    topBar = {
      TopAppBar(title = {
        Text(text = "Articles")
      })
    }
  ) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
      val articleList = hitStore.value
      items(
        count = articleList.size,
        key = { articleList[it].storyTitle }
      ) { pos ->
        Article(article = articleList[pos]) {}
      }
    }
  }
}

@Composable
private fun Article(article: HitStore, onClick: (HitStore) -> Unit) {
  Column(modifier = Modifier.padding(16.dp)) {
    Text(article.title)
    Text(article.url)
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  AndroidClientTheme {
    ArticleScreen(MutableStateFlow(articles))
  }
}


val articles = (0..15).map {
  HitStore(
    url = "my.url.com",
    title = randomString(it + 10),
    storyUrl = randomString(10),
    storyTitle = randomString(10),
    storyId = randomString(10),
    relevancyScore = (0L..15L).random(),
    storyText = randomString(10),
  )

}

private fun randomString(maxChars: Int) = UUID.randomUUID().toString().take(maxChars)
