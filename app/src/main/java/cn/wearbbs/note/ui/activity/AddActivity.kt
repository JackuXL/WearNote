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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.wearbbs.note.application.MainApplication
import cn.wearbbs.note.database.bean.Note
import cn.wearbbs.note.ui.activity.ui.theme.WearNoteTheme
import kotlinx.coroutines.launch

class AddActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearNoteTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    var name by remember { mutableStateOf("") }
                    val keyboardController = LocalSoftwareKeyboardController.current
                    val scope = rememberCoroutineScope()
                    Column(modifier = Modifier.padding(5.dp)) {
                        Text(text = "新建")
                        Spacer(modifier = Modifier.height(5.dp))
                        TextField(
                            value = name,
                            onValueChange = { name = it },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {keyboardController?.hide()}),
                            modifier = Modifier.weight(4f),
                            label = { Text(text = "文件名...") }
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Button(onClick = {
                            scope.launch {
                                MainApplication.noteDao.insertAll(
                                    Note(
                                        name = name,
                                        createTime = System.currentTimeMillis()
                                    )
                                )
                                Toast.makeText(this@AddActivity, "新建成功", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }, modifier = Modifier.fillMaxWidth()) {
                            Text(text = "确定")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    WearNoteTheme {
        Greeting2("Android")
    }
}