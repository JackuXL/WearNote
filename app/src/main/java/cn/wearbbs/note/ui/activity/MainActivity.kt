package cn.wearbbs.note.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.wearbbs.note.R
import cn.wearbbs.note.application.MainApplication.Companion.noteDao
import cn.wearbbs.note.database.bean.Note
import cn.wearbbs.note.ui.activity.compose.*
import cn.wearbbs.note.ui.theme.Blue
import cn.wearbbs.note.ui.theme.WearNoteTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

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
                                RESULT_OK -> {
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
                                    MenuIconButton(
                                        text = stringResource(id = R.string.rename),
                                        icon = Icons.Default.Edit,
                                        onClick = {
                                            shadowState = false
                                            launcher.launch(
                                                Intent(
                                                    this@MainActivity,
                                                    RenameActivity::class.java
                                                ).putExtra("id", clickedNote.id)
                                            )
                                        },
                                        background = Blue
                                    )


                                    Spacer(modifier = Modifier.width(40.dp))
                                    MenuIconButton(
                                        text = stringResource(id = R.string.delete),
                                        icon = Icons.Default.Delete,
                                        onClick = {
                                            scope.launch {
                                                noteDao.delete(clickedNote)
                                                shadowState = false
                                                notes = notes
                                                    .toMutableList()
                                                    .also { it.remove(clickedNote) }
                                            }
                                        },
                                        background = Color.Red
                                    )

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

                                    when (page) {
                                        0 -> {
                                            VectorTitle(
                                                title = stringResource(id = R.string.note),
                                                icon = Icons.Default.Add,
                                                iconDescription = stringResource(id = R.string.add),
                                                onIconClick = {
                                                    launcher.launch(
                                                        Intent(
                                                            this@MainActivity,
                                                            AddActivity::class.java
                                                        )
                                                    )
                                                }
                                            )
                                        }
                                        1 -> {
                                            VectorTitle(title = stringResource(id = R.string.about))
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(5.dp))
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        if (page == 0) {
                                            if (notes.isEmpty()) {
                                                EmptyMessage()
                                            } else {
                                                NoteList(notes = notes, onItemClick = {
                                                    launcher.launch(
                                                        Intent(
                                                            this@MainActivity,
                                                            EditActivity::class.java
                                                        ).putExtra("id", it.id)
                                                    )
                                                }) {
                                                    shadowState = true
                                                    clickedNote = it
                                                }
                                            }
                                        } else {
                                            About()
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


