package cn.wearbbs.note.ui.activity.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import cn.wearbbs.note.ui.theme.Typography

@Composable
fun VectorTitle(
    title: String,
    icon: ImageVector? = null,
    iconDescription: String? = null,
    onIconClick: (() -> Unit)? = null
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = title, style = Typography.h6)
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = iconDescription,
                tint = Color.White,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        if (onIconClick != null) {
                            onIconClick()
                        }
                    })
        }
    }
}

@Composable
fun PainterTitle(
    title: String,
    icon: Painter? = null,
    iconDescription: String? = null,
    onIconClick: (() -> Unit)? = null
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = title, style = Typography.h6)
        if (icon != null) {
            Icon(
                painter = icon,
                contentDescription = iconDescription,
                tint = Color.White,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        if (onIconClick != null) {
                            onIconClick()
                        }
                    })
        }
    }

}