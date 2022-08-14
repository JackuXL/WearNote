package cn.wearbbs.note.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.wearbbs.note.application.MainApplication
import cn.wearbbs.note.ui.activity.ui.theme.WearNoteTheme

class FullActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearNoteTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val scroll = rememberScrollState(0)
                    Text(
                        text = MainApplication.noteDao.findById(
                            intent.getIntExtra("id",0)
                        ).content,
                        modifier = Modifier.fillMaxSize().padding(5.dp).verticalScroll(scroll)
                    )
                }
            }
        }
    }
}
