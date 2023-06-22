package cn.wearbbs.note.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cn.wearbbs.note.R
import cn.wearbbs.note.application.MainApplication.Companion.noteDao
import cn.wearbbs.note.database.bean.Note
import cn.wearbbs.note.ui.activity.compose.EmptyMessage
import cn.wearbbs.note.ui.activity.compose.VectorTitle
import cn.wearbbs.note.ui.activity.page.AboutPage
import cn.wearbbs.note.ui.activity.page.NoteMenuPage
import cn.wearbbs.note.ui.activity.page.NotePage
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
                        NoteMenuPage(
                            onRenameClick = {
                                shadowState = false
                                launcher.launch(
                                    Intent(
                                        this@MainActivity,
                                        RenameActivity::class.java
                                    ).putExtra("id", clickedNote.id)
                                )
                            },
                            onDeleteClick = {
                                scope.launch {
                                    noteDao.delete(clickedNote)
                                    shadowState = false
                                    notes = notes
                                        .toMutableList()
                                        .also { it.remove(clickedNote) }
                                }
                            },
                            onCancelClick = {
                                shadowState = false
                            }
                        )
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
                                                NotePage(notes = notes, onItemClick = {
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
                                            AboutPage(onRecoveryClick = {
                                                launcher.launch(
                                                    Intent(
                                                        this@MainActivity,
                                                        RecoveryActivity::class.java
                                                    )
                                                )
                                            })
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


