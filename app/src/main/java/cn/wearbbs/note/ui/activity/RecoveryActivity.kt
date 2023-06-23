package cn.wearbbs.note.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cn.wearbbs.note.R
import cn.wearbbs.note.application.MainApplication
import cn.wearbbs.note.database.bean.Note
import cn.wearbbs.note.ui.theme.Typography
import cn.wearbbs.note.ui.theme.WearNoteTheme
import kotlinx.coroutines.launch
import java.io.File

class RecoveryActivity : ComponentActivity() {
    private val externalStoragePath = Environment.getExternalStorageDirectory().path

    val appDataDirectoryPath = "$externalStoragePath/Android/data/cn.wearbbs.note/"

    private var granted = mutableStateOf(false)

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // 文件存储权限已授予
                Toast.makeText(this, "文件存储权限已授予", Toast.LENGTH_SHORT).show()
                // 执行相关操作
                granted.value = true
            } else {
                // 文件存储权限被拒绝
                Toast.makeText(this, "文件存储权限被拒绝", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearNoteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    var isButtonEnabled by remember { mutableStateOf(false) }
                    LaunchedEffect(granted.value) {
                        // 监听 granted 的变化并更新按钮的启用状态
                        isButtonEnabled = granted.value
                        println(granted)
                        println(isButtonEnabled)
                    }
                    val scope = rememberCoroutineScope()
                    val scroll = rememberScrollState(0)
                    Column(modifier = Modifier
                        .padding(5.dp)
                        .verticalScroll(scroll)) {
                        Text(text = stringResource(id = R.string.recovery))
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "”文件恢复“功能目前仅能恢复 v1 版本更新到 v2 版本造成的数据丢失，不支持恢复因为更换设备、清除数据等造成的数据丢失。",
                            style = Typography.subtitle1
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Button(onClick = {
                            scope.launch {
                                requestStoragePermission()
                            }
                        }, modifier = Modifier.fillMaxWidth(), enabled = !isButtonEnabled) {
                            Text(text = stringResource(id = R.string.request_for_permission))
                        }
                        val unnamed = stringResource(id = R.string.unnamed)
                        val msg = stringResource(id = R.string.recover_successfully)
                        Button(onClick = {
                            scope.launch {
                                val list = convertTxtToNoteList(getTxtFiles(appDataDirectoryPath))
                                list.forEach {
                                    if (it.name.isBlank()) {
                                        it.name = unnamed
                                    }
                                }
                                MainApplication.noteDao.insertAll(*list.toTypedArray())
                                clearFolder(appDataDirectoryPath)
                                Toast.makeText(
                                    this@RecoveryActivity,
                                    msg.replace("%s", list.size.toString()),
                                    Toast.LENGTH_SHORT
                                ).show()
                                setResult(RESULT_OK, intent)
                                finish()
                            }
                        }, modifier = Modifier.fillMaxWidth(), enabled = isButtonEnabled) {
                            Text(text = stringResource(id = R.string.recovery_button))
                        }
                    }
                }
            }

        }
    }

    private fun requestStoragePermission() {
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val permissionGranted = PackageManager.PERMISSION_GRANTED


        when {
            ContextCompat.checkSelfPermission(this, permission) == permissionGranted -> {
                // 权限已经被授予，执行相关操作
                Toast.makeText(this, "文件存储权限已授予", Toast.LENGTH_SHORT).show()
                granted.value = true
            }

            ActivityCompat.shouldShowRequestPermissionRationale(this, permission) -> {
                // 当用户拒绝权限请求时，显示一个解释的对话框
                // 您可以在这里显示一个对话框，解释为什么需要文件存储权限，并向用户请求授予权限
                Toast.makeText(this, "需要文件存储权限才能执行此操作", Toast.LENGTH_SHORT).show()
                requestPermissionLauncher.launch(permission)
            }

            else -> {
                // 直接请求权限
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    private fun getTxtFiles(directoryPath: String): List<File> {
        val directory = File(directoryPath)
        return directory.listFiles { file ->
            file.isFile && file.extension.equals("txt", ignoreCase = true)
        }?.toList() ?: emptyList()
    }

    private fun convertTxtToNoteList(txtFiles: List<File>): List<Note> {
        val noteList = mutableListOf<Note>()
        for (file in txtFiles) {
            val name = file.nameWithoutExtension
            val content = file.readText()
            val createTime = file.lastModified()
            val note = Note(name = name, content = content, createTime = createTime)
            noteList.add(note)
        }
        return noteList
    }

    private fun clearFolder(directoryPath: String) {
        val folder = File(directoryPath)
        if (folder.exists() && folder.isDirectory) {
            val files = folder.listFiles()
            if (files != null) {
                for (file in files) {
                    if (file.isDirectory) {
                        clearFolder(file.path) // 递归清空子文件夹
                    } else {
                        file.delete() // 删除文件
                    }
                }
            }
        }
    }

}
