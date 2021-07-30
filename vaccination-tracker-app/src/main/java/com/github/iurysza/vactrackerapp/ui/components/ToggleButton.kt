package com.github.iurysza.vactrackerapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.iurysza.vactrackerapp.ui.theme.ColorPrimary

@Composable
fun ToggleButton(
  text: String,
  icon: ImageVector,
  checkedColor: Color = ColorPrimary,
  defaultColor: Color = Color(0xFFB0BEC5),
  checked: Boolean, onChecked: (Boolean) -> Unit = {}
) {

  Box(modifier = Modifier.size(width = 74.dp, height = 80.dp)) {
    IconToggleButton(
      checked = checked,
      onCheckedChange = onChecked,
      modifier = Modifier.fillMaxSize()
    ) {
      val tint by animateColorAsState(if (checked) checkedColor else defaultColor)
      val textColor by animateColorAsState(if (checked) checkedColor else defaultColor)
      Column {
        Icon(
          icon,
          contentDescription = "Localized description",
          tint = tint,
          modifier = Modifier.fillMaxWidth()
        )
        Text(
          text = text,
          color = textColor,
          modifier = Modifier.fillMaxWidth(),
          textAlign = TextAlign.Center,
        )
      }
    }
  }
}

@Preview
@Composable
fun PreviewToggle() {
  ToggleButton(
    text = "Total",
    icon = Icons.Filled.Person,
    checked = true
  )
}
