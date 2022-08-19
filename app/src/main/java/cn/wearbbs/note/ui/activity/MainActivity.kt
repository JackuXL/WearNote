package cn.wearbbs.note.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
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
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import java.sql.Date
import java.sql.Timestamp

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearNoteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    var notes: List<Note> by remember { mutableStateOf(listOf()) }
                    var listFlag by remember { mutableStateOf(false) }
                    var shadowState by remember {
                        mutableStateOf(false)
                    }
                    val pagerState = rememberPagerState()
                    val launcher =
                        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                            when (it.resultCode) {
                                Activity.RESULT_OK -> {
                                    listFlag = !listFlag
                                }
                            }
                        }
                    var clickedNote by remember {
                        mutableStateOf(
                            Note(
                                name = "",
                                content = "",
                                createTime = 0L
                            )
                        )
                    }
                    val scope = rememberCoroutineScope()
                    LaunchedEffect(listFlag) {
                        notes = noteDao.getAll()
                    }
                    if (shadowState) {
                        Box {
                            Image(
                                painter = painterResource(id = R.drawable.bg_longpress),
                                contentDescription = stringResource(id = R.string.background),
                                modifier = Modifier.fillMaxSize()
                            )
                            Column(
                                modifier = Modifier.align(Center),
                                horizontalAlignment = CenterHorizontally
                            ) {
                                Row {
                                    Column(horizontalAlignment = CenterHorizontally) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = stringResource(id = R.string.rename),
                                            modifier = Modifier
                                                .size(50.dp)
                                                .clip(CircleShape)
                                                .background(Blue)
                                                .clickable {
                                                    shadowState = false
                                                    launcher.launch(
                                                        Intent(
                                                            this@MainActivity,
                                                            RenameActivity::class.java
                                                        ).putExtra("id", clickedNote.id)
                                                    )
                                                }
                                                .padding(10.dp)
                                        )
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = stringResource(id = R.string.rename),
                                            fontSize = 10.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(40.dp))
                                    Column(horizontalAlignment = CenterHorizontally) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = stringResource(id = R.string.delete),
                                            modifier = Modifier
                                                .size(50.dp)
                                                .clip(CircleShape)
                                                .background(Color.Red)
                                                .clickable {
                                                    scope.launch {
                                                        noteDao.delete(clickedNote)
                                                        shadowState = false
                                                        notes = notes
                                                            .toMutableList()
                                                            .also { it.remove(clickedNote) }
                                                    }
                                                }
                                                .padding(10.dp))
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = stringResource(id = R.string.delete),
                                            fontSize = 10.sp
                                        )
                                    }

                                }
                                Spacer(modifier = Modifier.height(20.dp))
                                Box(
                                    contentAlignment = Center,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color.DarkGray)
                                        .clickable { shadowState = false }
                                        .padding(horizontal = 15.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.cancel),
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }


                            }
                        }
                    } else {
                        Box {
                            HorizontalPager(
                                state = pagerState,
                                count = 2
                            ) { page ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(5.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = if (page == 0) stringResource(id = R.string.note) else stringResource(
                                                id = R.string.about
                                            )
                                        )
                                        if (page == 0) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = stringResource(id = R.string.add),
                                                tint = Color.White,
                                                modifier = Modifier
                                                    .clip(CircleShape)
                                                    .clickable {
                                                        launcher.launch(
                                                            Intent(
                                                                this@MainActivity,
                                                                AddActivity::class.java
                                                            )
                                                        )
                                                    })
                                        }
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
                                                        contentDescription = stringResource(id = R.string.empty),
                                                        modifier = Modifier.size(50.dp)
                                                    )
                                                    Text(
                                                        text = stringResource(id = R.string.empty),
                                                        fontSize = 12.sp
                                                    )
                                                }
                                            } else {
                                                LazyColumn {
                                                    items(notes) {
                                                        NoteItem(note = it, onClick = {
                                                            launcher.launch(
                                                                Intent(
                                                                    this@MainActivity,
                                                                    EditActivity::class.java
                                                                ).putExtra("id", it.id)
                                                            )
                                                        }, onLongClick = {
                                                            shadowState = true
                                                            clickedNote = it
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
                                                    text = stringResource(id = R.string.about_author),
                                                    color = Color.LightGray,
                                                    fontSize = 10.sp
                                                )
                                                Text(
                                                    text = stringResource(id = R.string.about_organization),
                                                    color = Color.LightGray,
                                                    fontSize = 10.sp
                                                )
                                                Text(
                                                    text = stringResource(id = R.string.about_feedback),
                                                    color = Color.LightGray,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }

                                    }

                                }
                            }
                            HorizontalPagerIndicator(
                                pagerState = pagerState,
                                modifier = Modifier
                                    .align(BottomCenter)
                                    .padding(5.dp),
                            )
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