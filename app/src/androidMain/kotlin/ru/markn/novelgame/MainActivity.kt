package ru.markn.novelgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import ru.markn.engine.audio.GlobalAudioController

class MainActivity : ComponentActivity() {

    private lateinit var windowInsetsController: WindowInsetsControllerCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        windowInsetsController = window.let {
            WindowInsetsControllerCompat(it, it.decorView)
        }
        setContent {
            GameApp {
                androidContext(this@MainActivity)
                androidLogger()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        GlobalAudioController.setAudioEnabled(false)
        windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
    }

    override fun onResume() {
        super.onResume()
        GlobalAudioController.setAudioEnabled(true)
        windowInsetsController.run {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}
