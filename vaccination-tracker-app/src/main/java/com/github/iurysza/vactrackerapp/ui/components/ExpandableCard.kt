package com.github.iurysza.vactrackerapp.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.iurysza.vactrackerapp.R
import com.github.iurysza.vactrackerapp.ui.home.StateVaccinationCardModel
import com.github.iurysza.vactrackerapp.ui.theme.ColorHeader
import com.github.iurysza.vactrackerapp.ui.theme.ColorPrimary
import com.github.iurysza.vactrackerapp.ui.theme.ColorProgress
import com.github.iurysza.vactrackerapp.ui.theme.EXPAND_ANIMATION_DURATION
import com.github.iurysza.vactrackerapp.ui.theme.ColorCardCollapsedBackground
import com.github.iurysza.vactrackerapp.ui.theme.cardExpandedBackgroundColor

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ExpandableCard(
  model: StateVaccinationCardModel,
  onCardArrowClick: () -> Unit,
  expanded: Boolean,
  onItemClicked: (StateVaccinationCardModel) -> Unit,
) {
  val transitionState = remember {
    MutableTransitionState(expanded).apply {
      targetState = !expanded
    }
  }
  val transition = updateTransition(transitionState, label = "transition")
  val cardBgColor by transition.animateColor({
    tween(durationMillis = EXPAND_ANIMATION_DURATION)
  }, label = "bgColorTransition") {
    if (expanded) cardExpandedBackgroundColor else ColorCardCollapsedBackground
  }
  val cardPaddingHorizontal by transition.animateDp({
    tween(durationMillis = EXPAND_ANIMATION_DURATION)
  }, label = "paddingTransition") {
    if (expanded) 24.dp else 8.dp
  }
  val cardElevation by transition.animateDp({
    tween(durationMillis = EXPAND_ANIMATION_DURATION)
  }, label = "elevationTransition") {
    if (expanded) 16.dp else 2.dp
  }
  val cardRoundedCorners by transition.animateDp({
    tween(
      durationMillis = EXPAND_ANIMATION_DURATION,
      easing = FastOutSlowInEasing
    )
  }, label = "cornersTransition") {
    if (expanded) 16.dp else 32.dp
  }
  val arrowRotationDegree by transition.animateFloat({
    tween(durationMillis = EXPAND_ANIMATION_DURATION)
  }, label = "rotationDegreeTransition") {
    if (expanded) 0f else -90f
  }

  Card(
    backgroundColor = cardBgColor,
    contentColor = ColorPrimary,
    elevation = cardElevation,
    shape = RoundedCornerShape(cardRoundedCorners),
    modifier = Modifier
      .fillMaxWidth()
      .noRippleClickable {
        onCardArrowClick()
      }
      .padding(
        horizontal = 24.dp,
        vertical = cardPaddingHorizontal
      )
  ) {
    HeaderProgress(model.coverage)
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        FlagIcon(model.icon, model.name)
        CardTitle(model.name)
        CardArrow(
          degrees = arrowRotationDegree,
          onClick = onCardArrowClick
        )
      }
      ContentGrid(
        visible = expanded,
        dataList = model.dataList,
        onListClicked = { onItemClicked(model) }
      )
    }
  }
}

@Composable
private fun HeaderProgress(progressValue: Float) {
  LinearProgressIndicator(
    progress = progressValue,
    modifier = Modifier
      .fillMaxWidth()
      .height(54.dp),
    backgroundColor = ColorCardCollapsedBackground,
    color = ColorProgress,
  )
}

@Composable
private fun FlagIcon(iconId: Int, description: String = "") {
  Box(modifier = Modifier.clip(RoundedCornerShape(200.dp))) {
    Image(
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .size(48.dp)
        .clip(RoundedCornerShape(48.dp)),
      painter = painterResource(id = iconId),
      contentDescription = description
    )
  }
}

@Composable
fun CardArrow(
  degrees: Float,
  onClick: () -> Unit
) {
  IconButton(
    onClick = onClick,
    content = {
      Icon(
        painter = painterResource(id = R.drawable.ic_arrow_down),
        contentDescription = "Expandable Arrow",
        modifier = Modifier.rotate(degrees),
      )
    },
  )
}

@Composable
fun CardTitle(title: String) {
  Text(
    buildAnnotatedString {
      withStyle(
        style = SpanStyle(fontWeight = FontWeight.W500, color = ColorHeader)
      ) {
        append(title)
      }
    },
    modifier = Modifier
      .wrapContentWidth()
      .padding(16.dp),
    textAlign = TextAlign.Center,
    fontSize = 16.sp
  )
}

@Preview
@Composable
fun PreviewCollapsedContent() {
  ExpandableCard(
    model = FakeModels.model(),
    onCardArrowClick = {},
    expanded = false
  ) {

  }
}

@Preview
@Composable
fun PreviewExpandedContent() {
  ExpandableCard(
    model = FakeModels.model(),
    onCardArrowClick = {},
    expanded = true
  ) {

  }
}

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
  clickable(indication = null,
    interactionSource = remember {
      MutableInteractionSource()
    }
  ) { onClick() }
}