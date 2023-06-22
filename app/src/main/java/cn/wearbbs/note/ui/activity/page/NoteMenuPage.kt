package cn.wearbbs.note.ui.activity.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cn.wearbbs.note.R
import cn.wearbbs.note.ui.activity.compose.MenuIconButton
import cn.wearbbs.note.ui.theme.Blue
import cn.wearbbs.note.ui.theme.Typography


@Composable
fun NoteMenuPage(
    modifier: Modifier = Modifier,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                MenuIconButton(
                    text = stringResource(id = R.string.rename),
                    icon = Icons.Default.Edit,
                    onClick = onRenameClick,
                    background = Blue
                )


                Spacer(modifier = Modifier.width(40.dp))
                MenuIconButton(
                    text = stringResource(id = R.string.delete),
                    icon = Icons.Default.Delete,
                    onClick = onDeleteClick,
                    background = Color.Red
                )

            }
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))

                    .background(Color.DarkGray)
                    .clickable { onCancelClick() }
                    .padding(horizontal = 15.dp, vertical = 5.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    textAlign = TextAlign.Center,
                    style = Typography.body2
                )
            }


        }
    }
}