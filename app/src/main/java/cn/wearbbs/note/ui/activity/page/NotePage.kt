package cn.wearbbs.note.ui.activity.page

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.wearbbs.note.database.bean.Note
import cn.wearbbs.note.ui.activity.compose.NoteItem

@Composable
fun NotePage(notes: List<Note>, onItemClick: (Note) -> Unit, onItemLongClick: (Note) -> Unit) {
    LazyColumn {
        items(notes) {
            NoteItem(note = it, onClick = onItemClick, onLongClick = onItemLongClick)
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}