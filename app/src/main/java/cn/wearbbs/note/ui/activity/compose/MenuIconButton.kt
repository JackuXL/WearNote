package cn.wearbbs.note.ui.activity.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cn.wearbbs.note.ui.theme.Typography

@Composable
fun MenuIconButton(text: String, icon: ImageVector, onClick: () -> Unit, background: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(background)
                .clickable {
                    onClick()
                }
                .padding(10.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = text,
            style = Typography.caption
        )
    }
}