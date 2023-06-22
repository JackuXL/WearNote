package cn.wearbbs.note.ui.activity.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.wearbbs.note.R
import cn.wearbbs.note.database.bean.Note
import cn.wearbbs.note.ui.theme.Gray
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
            .background(Gray)
            .padding(5.dp)
    ) {
        Text(
            text = note.name,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Row {
            Text(
                text = formatMemoDate(Date(Timestamp(note.createTime).time)),
                fontSize = 14.sp,
                modifier = Modifier.wrapContentWidth(),
                maxLines = 1,
                color = Color.LightGray
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = note.content.ifEmpty { stringResource(id = R.string.empty_content) },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
                color = Color.LightGray
            )
        }

    }
}

fun formatMemoDate(date: Date): String {
    // Get the current date and time
    val now = Calendar.getInstance()

    val memoDate = Calendar.getInstance().apply {
        time = date
    }
    // Calculate the date difference
    val diff = now.get(Calendar.DAY_OF_YEAR) - memoDate.get(Calendar.DAY_OF_YEAR)
    val days = diff.toLong()

    return when {
        days == 0L -> {
            // If it's today, display the specific time
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(memoDate.time)
        }

        days == 1L -> "昨天 "
        days < 7L -> {
            // If it's within a week, display the day of the week
            SimpleDateFormat("EEEE", Locale.getDefault()).format(memoDate.time)
        }

        else -> {
            // Otherwise, display the full date
            SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault()).format(memoDate.time)
        }
    }
}
