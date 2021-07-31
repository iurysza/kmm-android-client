package com.github.iurysza.vactrackerapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.iurysza.vactrackerapp.ui.home.DataPoint
import com.github.iurysza.vactrackerapp.ui.theme.COLLAPSE_ANIMATION_DURATION
import com.github.iurysza.vactrackerapp.ui.theme.EXPAND_ANIMATION_DURATION
import com.github.iurysza.vactrackerapp.ui.theme.FADE_IN_ANIMATION_DURATION
import com.github.iurysza.vactrackerapp.ui.theme.FADE_OUT_ANIMATION_DURATION

@ExperimentalFoundationApi
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ContentGrid(
  visible: Boolean,
  dataList: List<DataPoint>,
  onListClicked: () -> Unit,
) {
  val (enterFadeIn, enterExpand) = enterAnimation()
  val (exitFadeOut, exitCollapse) = exitAnimation()

  AnimatedVisibility(
    visible = visible,
    enter = enterExpand + enterFadeIn,
    exit = exitCollapse + exitFadeOut
  ) {

    LazyVerticalGrid(
      modifier = Modifier
        .height(300.dp)
        .padding(8.dp),
      cells = GridCells.Fixed(2)
    ) {
      items(count = dataList.size) { index ->
        val data: DataPoint = dataList[index]
        DataPointCard(
          value = if (data.value.contains(".")) {
            data.value
          } else {
            runCatching { data.value.format() }.getOrElse { data.value }
          },
          label = data.label
        ) { onListClicked() }
      }
    }
  }
}

@ExperimentalAnimationApi
@Composable
fun exitAnimation(): Pair<ExitTransition, ExitTransition> {
  val exitFadeOut = remember {
    fadeOut(
      animationSpec = TweenSpec(
        durationMillis = FADE_OUT_ANIMATION_DURATION,
        easing = LinearOutSlowInEasing
      )
    )
  }
  val exitCollapse = remember {
    shrinkVertically(animationSpec = tween(COLLAPSE_ANIMATION_DURATION))
  }
  return Pair(exitFadeOut, exitCollapse)
}

@ExperimentalAnimationApi
@Composable
private fun enterAnimation(): Pair<EnterTransition, EnterTransition> {
  val enterFadeIn = remember {
    fadeIn(
      animationSpec = TweenSpec(
        durationMillis = FADE_IN_ANIMATION_DURATION,
        easing = FastOutLinearInEasing
      )
    )
  }
  val enterExpand = remember {
    expandVertically(animationSpec = tween(EXPAND_ANIMATION_DURATION))
  }
  return Pair(enterFadeIn, enterExpand)
}

@ExperimentalFoundationApi
@Composable
@Preview
fun CardPrev() {
  ContentGrid(
    visible = true,
    dataList = FakeModels.model().dataList
  ) { }
}
