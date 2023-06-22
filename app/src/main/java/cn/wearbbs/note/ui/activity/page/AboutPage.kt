package cn.wearbbs.note.ui.activity.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cn.wearbbs.note.R
import cn.wearbbs.note.application.MainApplication
import cn.wearbbs.note.ui.theme.Blue
import cn.wearbbs.note.ui.theme.Typography

@Composable
fun AboutPage(onRecoveryClick: () -> Unit) {
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
        Text(
            text = stringResource(id = R.string.app_name),
            style = Typography.subtitle1
        )
        Text(
            text = "v${MainApplication.applicationVersion}",
            style = Typography.body2
        )
        Text(
            text = stringResource(id = R.string.about_author),
            style = Typography.caption
        )
        Text(
            text = stringResource(id = R.string.about_organization),
            style = Typography.caption
        )
        Text(
            text = stringResource(id = R.string.about_opensource),
            style = Typography.caption
        )
        Text(
            text = stringResource(id = R.string.about_feedback),
            style = Typography.caption
        )
        Text(
            text = stringResource(id = R.string.recovery),
            color = Blue,
            style = Typography.caption,
            modifier = Modifier.clickable {
                onRecoveryClick()
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}