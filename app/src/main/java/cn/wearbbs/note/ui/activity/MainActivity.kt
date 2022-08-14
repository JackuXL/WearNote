package cn.wearbbs.note.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import cn.wearbbs.note.R
import cn.wearbbs.note.database.bean.Note
import cn.wearbbs.note.application.MainApplication.Companion.noteDao
import cn.wearbbs.note.ui.activity.ui.theme.WearNoteTheme
import java.sql.Date
import java.sql.Timestamp
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearNoteTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    var notes: List<Note> by remember { mutableStateOf(listOf()) }
                    LaunchedEffect(null){
                        notes = noteDao.getAll()
                    }
                    Column(modifier = Modifier.padding(5.dp)) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text(text = "文档")
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = Color.White, modifier = Modifier
                                .clip(CircleShape)
                                .clickable {
                                    startActivity(
                                        Intent(
                                            this@MainActivity,
                                            AddActivity::class.java
                                        )
                                    )
                                })
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Box{
                            LazyColumn {
                                items(notes){
                                    NoteItem(note = it, onClick = {
                                        startActivity(Intent(this@MainActivity,EditActivity::class.java).putExtra("id", it.id))
                                    })
                                    Spacer(modifier = Modifier.height(5.dp))
                                }
                            }
                            Image(painter = painterResource(id = R.drawable.ic_page_first), contentDescription = "First", modifier = Modifier.align(alignment = BottomCenter).padding(bottom = 5.dp))
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun NoteItem(note: Note,onClick:()->Unit){
    Column(modifier = Modifier
        .clip(shape = RoundedCornerShape(8.dp))
        .clickable {
            onClick()
        }
        .fillMaxWidth()
        .background(Color.DarkGray)
        .padding(5.dp)
        ) {
        Row {
            Text(text = note.name, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(weight = 1f,fill = false))
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = Date(Timestamp(note.createTime).time).toString(), fontSize = 14.sp, modifier = Modifier.wrapContentWidth(), maxLines = 1)
        }
        Text(text = note.content, maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 12.sp, color = Color.LightGray)
    }
}