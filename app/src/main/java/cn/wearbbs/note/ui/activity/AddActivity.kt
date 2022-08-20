package cn.wearbbs.note.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
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
import cn.wearbbs.note.ui.theme.WearNoteTheme
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AddActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearNoteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    var name by remember { mutableStateOf("") }
                    val keyboardController = LocalSoftwareKeyboardController.current
                    val scope = rememberCoroutineScope()
                    Column(modifier = Modifier.padding(5.dp)) {
                        Text(text = stringResource(id = R.string.add))
                        Spacer(modifier = Modifier.height(5.dp))
                        TextField(
                            value = name,
                            onValueChange = { name = it },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {keyboardController?.hide()}),
                            modifier = Modifier.weight(4f),
                            label = { Text(text = stringResource(id = R.string.file_name)) }
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        val msg = stringResource(id = R.string.add_successfully)
                        val error = stringResource(id = R.string.file_name_cannot_be_blank)
                        Button(onClick = {
                            scope.launch {
                                if (name.isBlank()) {
                                    Toast.makeText(this@AddActivity, error, Toast.LENGTH_SHORT)
                                        .show()
                                    cancel()
                                }
                                MainApplication.noteDao.insertAll(
                                    Note(
                                        name = name,
                                        createTime = System.currentTimeMillis()
                                    )
                                )
                                Toast.makeText(this@AddActivity, msg, Toast.LENGTH_SHORT).show()
                                setResult(RESULT_OK, intent)
                                finish()
                            }
                        }, modifier = Modifier.fillMaxWidth()) {
                            Text(text = stringResource(id = R.string.add))
                        }
                    }
                }
            }
        }
    }
}