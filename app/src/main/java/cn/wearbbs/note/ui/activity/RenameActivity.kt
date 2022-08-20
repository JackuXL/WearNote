package cn.wearbbs.note.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cn.wearbbs.note.R
import cn.wearbbs.note.application.MainApplication
import cn.wearbbs.note.database.bean.Note
import cn.wearbbs.note.ui.activity.compose.VectorTitle
import cn.wearbbs.note.ui.activity.ui.theme.WearNoteTheme
import kotlinx.coroutines.launch

class RenameActivity : ComponentActivity() {
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
                    val scope = rememberCoroutineScope()
                    LaunchedEffect(null) {
                        note = MainApplication.noteDao.findById(intent.getIntExtra("id", 0))
                    }
                    val keyboardController = LocalSoftwareKeyboardController.current
                    Column(modifier = Modifier.padding(5.dp)) {
                        VectorTitle(title = stringResource(id = R.string.rename))
                        Spacer(modifier = Modifier.height(5.dp))
                        TextField(
                            value = note.name,
                            onValueChange = {
                                note = note.copy(name = it)
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = { keyboardController?.hide() }),
                            modifier = Modifier
                                .weight(4f)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            label = null
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val msg = stringResource(id = R.string.save_successfully)
                            Button(onClick = {
                                scope.launch {
                                    MainApplication.noteDao.update(note)
                                    Toast.makeText(this@RenameActivity, msg, Toast.LENGTH_SHORT)
                                        .show()
                                    setResult(RESULT_OK)
                                    finish()
                                }
                            }, modifier = Modifier.fillMaxWidth()) {
                                Text(text = stringResource(id = R.string.save))
                            }
                        }
                    }
                }
            }
        }
    }
}
