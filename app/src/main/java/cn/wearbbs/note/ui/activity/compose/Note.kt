package cn.wearbbs.note.ui.activity.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.wearbbs.note.database.bean.Note
import java.sql.Date
import java.sql.Timestamp

@Composable
fun NoteList(notes: List<Note>, onItemClick: (Note) -> Unit, onItemLongClick: (Note) -> Unit) {
    LazyColumn {
        items(notes) {
            NoteItem(note = it, onClick = onItemClick, onLongClick = onItemLongClick)
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(note: Note, onClick: (Note) -> Unit, onLongClick: (Note) -> Unit) {
    Column(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .combinedClickable(
                onClick = {
                    onClick(note)
                },
                onLongClick = {
                    onLongClick(note)
                },
            )
            .fillMaxWidth()
            .background(Color.DarkGray)
            .padding(5.dp)
    ) {
        Row {
            Text(
                text = note.name,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(weight = 1f, fill = false)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = Date(Timestamp(note.createTime).time).toString(),
                fontSize = 14.sp,
                modifier = Modifier.wrapContentWidth(),
                maxLines = 1
            )
        }
        Text(
            text = note.content,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 12.sp,
            color = Color.LightGray
        )
    }
}