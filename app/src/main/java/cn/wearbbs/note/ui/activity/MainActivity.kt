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
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.wearbbs.note.R
import cn.wearbbs.note.application.MainApplication
import cn.wearbbs.note.application.MainApplication.Companion.noteDao
import cn.wearbbs.note.database.bean.Note
import cn.wearbbs.note.ui.activity.ui.theme.Blue
import cn.wearbbs.note.ui.activity.ui.theme.WearNoteTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import kotlinx.coroutines.launch
import java.sql.Date
import java.sql.Timestamp

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearNoteTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    var notes: MutableList<Note> by remember { mutableStateOf(mutableListOf()) }
                    var shadowState by remember {
                        mutableStateOf(false)
                    }
                    var clickNote by remember {
                        mutableStateOf(
                            Note(
                                name = "",
                                content = "",
                                createTime = 0L
                            )
                        )
                    }
                    val scope = rememberCoroutineScope()
                    LaunchedEffect(null) {
                        notes = noteDao.getAll()
                    }
                    Box {
                        if (shadowState) {
                            Box {
                                Image(
                                    painter = painterResource(id = R.drawable.bg_longpress),
                                    contentDescription = "Background",
                                    modifier = Modifier.fillMaxSize()
                                )
                                Column(
                                    modifier = Modifier.align(Center),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row {
                                        Column(horizontalAlignment = CenterHorizontally) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Rename",
                                                modifier = Modifier
                                                    .clip(CircleShape)
                                                    .background(Blue)
                                                    .padding(10.dp)
                                            )
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(text = "重命名", fontSize = 10.sp)
                                        }

                                        Spacer(modifier = Modifier.width(40.dp))
                                        Column(horizontalAlignment = CenterHorizontally) {
                                            Icon(imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                modifier = Modifier
                                                    .clip(CircleShape)
                                                    .background(Color.Red)
                                                    .clickable {
                                                        println(clickNote)
                                                        scope.launch {
                                                            noteDao.delete(clickNote)
                                                            shadowState = false
                                                            notes.remove(clickNote)
                                                        }
                                                    }
                                                    .padding(10.dp))
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(text = "删除", fontSize = 10.sp)
                                        }

                                    }
                                    Spacer(modifier = Modifier.height(15.dp))
                                    Box(
                                        contentAlignment = Center,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color.DarkGray)
                                            .clickable { shadowState = false }
                                            .padding(horizontal = 15.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = "取消",
                                            fontSize = 10.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    }


                                }
                            }
                        } else {
                            HorizontalPager(count = 2) { page ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(5.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(text = if (page == 0) "文档" else "关于")
                                        Icon(imageVector = Icons.Default.Add,
                                            contentDescription = "Add",
                                            tint = Color.White,
                                            modifier = Modifier
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
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        if (page == 0) {
                                            if (notes.isEmpty()) {
                                                Column(
                                                    verticalArrangement = Arrangement.Center,
                                                    horizontalAlignment = CenterHorizontally,
                                                    modifier = Modifier.fillMaxSize()
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Search,
                                                        contentDescription = "Empty",
                                                        modifier = Modifier.size(50.dp)
                                                    )
                                                    Text("暂无文档", fontSize = 12.sp)
                                                }
                                            } else {
                                                LazyColumn {
                                                    items(notes) {
                                                        NoteItem(note = it, onClick = {
                                                            startActivity(
                                                                Intent(
                                                                    this@MainActivity,
                                                                    EditActivity::class.java
                                                                ).putExtra("id", it.id)
                                                            )
                                                        }, onLongClick = {
                                                            shadowState = true
                                                            clickNote = it
                                                        })
                                                        Spacer(modifier = Modifier.height(5.dp))
                                                    }
                                                }
                                            }
                                        } else {
                                            val scroll = rememberScrollState(0)
                                            Column(
                                                horizontalAlignment = CenterHorizontally,
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .verticalScroll(scroll)
                                            ) {
                                                Image(
                                                    painter = painterResource(id = R.drawable.ic_app_round),
                                                    contentDescription = "Icon",
                                                    modifier = Modifier.size(50.dp)
                                                )
                                                Spacer(modifier = Modifier.height(5.dp))
                                                Text(text = stringResource(id = R.string.app_name))
                                                Text(
                                                    text = "v${MainApplication.applicationVersion}",
                                                    color = Color.LightGray,
                                                    fontSize = 12.sp
                                                )
                                                Text(
                                                    text = "Made with ❤ by JackuXL",
                                                    color = Color.LightGray,
                                                    fontSize = 10.sp
                                                )
                                                Text(
                                                    text = "TechNetSpace 出品",
                                                    color = Color.LightGray,
                                                    fontSize = 10.sp
                                                )
                                                Text(
                                                    text = "反馈：support@technet.space",
                                                    color = Color.LightGray,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                        Image(
                                            painter = painterResource(id = if (page == 0) R.drawable.ic_page_first else R.drawable.ic_page_second),
                                            contentDescription = "First",
                                            modifier = Modifier
                                                .align(alignment = BottomCenter)
                                                .padding(bottom = 5.dp)
                                        )
                                    }
                                }
                            }
                        }


                    }
                }
                    }

        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(note: Note, onClick: () -> Unit, onLongClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            )
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