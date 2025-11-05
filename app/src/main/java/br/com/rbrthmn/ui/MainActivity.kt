package br.com.rbrthmn.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.Modifier
import br.com.rbrthmn.ui.financialcompanion.ComFinApp
import br.com.rbrthmn.ui.theme.ComFinTheme
import br.com.rbrthmn.ui.theme.LocalTheme
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.core.annotation.KoinExperimentalAPI

class MainActivity : ComponentActivity() {

    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComFinTheme(darkTheme = LocalTheme.current.isDark) {
                KoinAndroidContext {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        ComFinApp(windowSize = WindowWidthSizeClass.Compact)
                    }
                }
            }
        }
    }
}
