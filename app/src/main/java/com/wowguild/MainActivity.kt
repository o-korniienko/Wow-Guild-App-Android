package com.wowguild

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.wowguild.tool.SaveDataProvider
import com.wowguild.ui.theme.WowGuildAppTheme
import com.wowguild.util.WowGuildLogger

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val saveDataProvider = SaveDataProvider(this)
        saveDataProvider.logLevel = WowGuildLogger.LogLevels.PUSHSDK_LOG_LEVEL_DEBUG.name
        println("firebase token is ${saveDataProvider.firebaseRegistrationToken}")

        enableEdgeToEdge()
        setContent {
            WowGuildAppTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFEB3B))
                            .padding(innerPadding)
                    ) {
                        HelloText()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HelloText() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFEB3B))
    ) {
        Text(
            text = stringResource(id = R.string.hello_text),

            style = TextStyle(
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color(0xFF056A96),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}