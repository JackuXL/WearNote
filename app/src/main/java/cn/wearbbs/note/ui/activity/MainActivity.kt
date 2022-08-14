package cn.wearbbs.note.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.wearbbs.note.R
import cn.wearbbs.note.application.MainApplication
import cn.wearbbs.note.application.MainApplication.Companion.noteDao
import cn.wearbbs.note.database.bean.Note
import cn.wearbbs.note.ui.activity.ui.theme.WearNoteTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import java.sql.Date
import java.sql.Timestamp

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPagerApi::class)
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
                    HorizontalPager(count = 2) { page ->
                        Column(modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp)) {
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text(text = if(page==0) "文档" else "关于")
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
                            Box(modifier = Modifier.fillMaxSize()){
                                if(page==0){
                                    LazyColumn {
                                        items(notes){
                                            NoteItem(note = it, onClick = {
                                                startActivity(Intent(this@MainActivity,EditActivity::class.java).putExtra("id", it.id))
                                            })
                                            Spacer(modifier = Modifier.height(5.dp))
                                        }
                                    }
                                }
                                else{
                                    val scroll = rememberScrollState(0)
                                    Column(horizontalAlignment = CenterHorizontally, modifier = Modifier.fillMaxSize().verticalScroll(scroll)) {
                                        Image(painter = painterResource(id = R.drawable.ic_app_round), contentDescription = "Icon", modifier = Modifier.size(50.dp))
                                        Spacer(modifier = Modifier.height(5.dp))
                                        Text(text = stringResource(id = R.string.app_name))
                                        Text(text = "v${MainApplication.applicationVersion}", color = Color.LightGray, fontSize = 12.sp)
                                        Text(text = "Made with ❤ by JackuXL", color = Color.LightGray, fontSize = 10.sp)
                                        Text(text = "TechNetSpace 出品", color = Color.LightGray, fontSize = 10.sp)
                                        Text(text = "反馈：support@technet.space", color = Color.LightGray, fontSize = 10.sp)
                                    }
                                }
                                Image(painter = painterResource(id = if(page==0) R.drawable.ic_page_first else R.drawable.ic_page_second), contentDescription = "First", modifier = Modifier
                                    .align(alignment = BottomCenter)
                                    .padding(bottom = 5.dp))
                            }
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