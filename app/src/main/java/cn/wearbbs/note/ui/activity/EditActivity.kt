package cn.wearbbs.note.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cn.wearbbs.note.R
import cn.wearbbs.note.application.MainApplication
import cn.wearbbs.note.database.bean.Note
import cn.wearbbs.note.ui.activity.ui.theme.WearNoteTheme
import kotlinx.coroutines.launch

class EditActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearNoteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    var note: Note by remember { mutableStateOf(Note(0, "", "", 0L)) }
                    var content by remember { mutableStateOf(TextFieldValue()) }
                    val scope = rememberCoroutineScope()
                    LaunchedEffect(null){
                        note = MainApplication.noteDao.findById(intent.getIntExtra("id",0))
                    }
                    val keyboardController = LocalSoftwareKeyboardController.current
                    Column(modifier = Modifier.padding(5.dp)) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text(text = stringResource(id = R.string.edit))
                            Icon(painter = painterResource(id = R.drawable.baseline_fullscreen_24),
                                contentDescription = "Add",
                                tint = Color.White,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable {
                                        startActivity(
                                            Intent(
                                                this@EditActivity,
                                                FullActivity::class.java
                                            ).putExtra("id", note.id)
                                        )
                                    })
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        TextField(
                            value = content,
                            onValueChange = {
                                content = it
                                println(content)
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = { keyboardController?.hide() }),
                            modifier = Modifier.weight(4f),
                            shape = RoundedCornerShape(8.dp),
                            label = null
                        )

                        Spacer(modifier = Modifier.height(5.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(2f)
                        ) {
                            val msg = stringResource(id = R.string.save_successfully)
                            Button(onClick = {
                                scope.launch {
                                    note.content = content.annotatedString.text
                                    MainApplication.noteDao.update(note)
                                    Toast.makeText(this@EditActivity, msg, Toast.LENGTH_SHORT)
                                        .show()
                                    setResult(RESULT_OK)
                                    finish()
                                }
                            }, modifier = Modifier.weight(1.0f)) {
                                Text(text = stringResource(id = R.string.save))
                            }
                            Spacer(modifier = Modifier.weight(0.1f))
                            Button(onClick = {
                                with(content.annotatedString.text) {
                                    content = content.copy(
                                        AnnotatedString(
                                            substring(
                                                0,
                                                content.selection.min
                                            ) + "\n" + substring(content.selection.max)
                                        ),
                                        selection = TextRange(
                                            content.selection.min,
                                            content.selection.min
                                        )
                                    )
                                }

                            }, modifier = Modifier.weight(1.0f)) {
                                Text(text = stringResource(id = R.string.enter))
                            }
                        }
                    }
                }
            }
        }
    }
}
