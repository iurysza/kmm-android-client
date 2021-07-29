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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
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
import com.github.iurysza.vactrackerapp.StateVaccinationCardModel
import com.github.iurysza.vactrackerapp.ui.theme.ColorHeader
import com.github.iurysza.vactrackerapp.ui.theme.ColorProgress
import com.github.iurysza.vactrackerapp.ui.theme.ColorPrimary
import com.github.iurysza.vactrackerapp.ui.theme.cardCollapsedBackgroundColor
import com.github.iurysza.vactrackerapp.ui.theme.cardExpandedBackgroundColor

const val EXPAND_ANIMATION_DURATION = 450
const val COLLAPSE_ANIMATION_DURATION = 300
const val FADE_IN_ANIMATION_DURATION = 350
const val FADE_OUT_ANIMATION_DURATION = 300

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ExpandableCard(
  card: StateVaccinationCardModel,
  onCardArrowClick: () -> Unit,
  expanded: Boolean,
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
    if (expanded) cardExpandedBackgroundColor else cardCollapsedBackgroundColor
  }
  val cardPaddingHorizontal by transition.animateDp({
    tween(durationMillis = EXPAND_ANIMATION_DURATION)
  }, label = "paddingTransition") {
    if (expanded) 24.dp else 8.dp
  }
  val cardElevation by transition.animateDp({
    tween(durationMillis = EXPAND_ANIMATION_DURATION)
  }, label = "elevationTransition") {
    if (!expanded) 4.dp else 16.dp
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
      .clickable { onCardArrowClick() }
      .padding(
        horizontal = 24.dp,
        vertical = cardPaddingHorizontal
      )
  ) {

    LinearProgressIndicator(
      progress = card.coverage,
      modifier = Modifier
        .fillMaxWidth()
        .height(54.dp),
      backgroundColor = cardCollapsedBackgroundColor,
      color = ColorProgress,
    )
    Column {
      Row(
        modifier = Modifier
          .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Box(modifier = Modifier.clip(RoundedCornerShape(200.dp))) {
          FlagIcon(card)
        }

        CardTitle(title = card.name)
        CardArrow(
          degrees = arrowRotationDegree,
          onClick = onCardArrowClick
        )
      }
      ExpandableContent(visible = expanded, card)
    }
  }
}

@Composable
private fun FlagIcon(card: StateVaccinationCardModel) {
  Image(
    contentScale = ContentScale.Crop,
    modifier = Modifier
      .size(48.dp)
      .clip(RoundedCornerShape(48.dp)),
    painter = painterResource(id = card.icon),
    contentDescription = card.name
  )
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
    card = FakeModels.model(),
    expanded = false,
    onCardArrowClick = {}
  )
}

@Preview
@Composable
fun PreviewExpandedContent() {
  ExpandableCard(
    card = FakeModels.model(),
    expanded = true,
    onCardArrowClick = {}
  )
}
