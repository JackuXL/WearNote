package cn.wearbbs.note.ui.activity.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.wearbbs.note.R
import cn.wearbbs.note.application.MainApplication

@Composable
fun About() {
    val scroll = rememberScrollState(0)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
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