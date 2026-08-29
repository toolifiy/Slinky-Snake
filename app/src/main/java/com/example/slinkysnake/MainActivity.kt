package com.example.slinkysnake

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.slinkysnake.audio.SoundSynth
import com.example.slinkysnake.model.Direction
import com.example.slinkysnake.ui.components.NavTab
import com.example.slinkysnake.ui.screens.GamePlayScreen
import com.example.slinkysnake.ui.screens.HomeScreen
import com.example.slinkysnake.ui.screens.MarketScreen
import com.example.slinkysnake.ui.screens.SettingsScreen
import com.example.slinkysnake.ui.screens.SkinsScreen
import com.example.slinkysnake.ui.theme.SlinkySnakeTheme
import com.example.slinkysnake.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize zero-latency audio engine immediately
        SoundSynth.init(applicationContext)

        setContent {
            SlinkySnakeTheme {
                CompositionLocalProvider(
                    LocalOverscrollConfiguration provides null
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        SlinkySnakeApp(viewModel = viewModel)
                    }
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_W -> {
                viewModel.onDirectionInput(Direction.UP)
                return true
            }
            KeyEvent.KEYCODE_DPAD_DOWN, KeyEvent.KEYCODE_S -> {
                viewModel.onDirectionInput(Direction.DOWN)
                return true
            }
            KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_A -> {
                viewModel.onDirectionInput(Direction.LEFT)
                return true
            }
            KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_D -> {
                viewModel.onDirectionInput(Direction.RIGHT)
                return true
            }
            KeyEvent.KEYCODE_SPACE -> {
                if (viewModel.uiState.value.isPlaying) {
                    viewModel.togglePause()
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}

private fun getTabIndex(tabName: String): Int = when (tabName) {
    NavTab.HOME.name -> 0
    NavTab.MARKET.name -> 1
    NavTab.SKINS.name -> 2
    NavTab.SETTINGS.name -> 3
    else -> 0
}

@Composable
fun SlinkySnakeApp(viewModel: GameViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    var currentTab by remember { mutableStateOf(NavTab.HOME) }

    val isGameActive = uiState.isPlaying || uiState.showGameOver || uiState.showLevelClear || uiState.showVictory

    AnimatedContent(
        targetState = if (isGameActive) "GAMEPLAY" else currentTab.name,
        transitionSpec = {
            (fadeIn(animationSpec = tween(300, easing = FastOutSlowInEasing)))
                .togetherWith(fadeOut(animationSpec = tween(300, easing = FastOutSlowInEasing)))
        },
        label = "AppScreenTransition"
    ) { screen ->
        when (screen) {
            "GAMEPLAY" -> {
                GamePlayScreen(
                    viewModel = viewModel,
                    uiState = uiState,
                    onBackToHome = { viewModel.exitGame() }
                )
            }
            NavTab.HOME.name -> {
                HomeScreen(
                    viewModel = viewModel,
                    uiState = uiState,
                    onStartGame = { viewModel.startGame() },
                    onOpenSkins = { currentTab = NavTab.SKINS },
                    onOpenMarket = { currentTab = NavTab.MARKET },
                    onOpenSettings = { currentTab = NavTab.SETTINGS }
                )
            }
            NavTab.MARKET.name -> {
                MarketScreen(
                    viewModel = viewModel,
                    uiState = uiState,
                    onBackToHome = { currentTab = NavTab.HOME },
                    onOpenSkins = { currentTab = NavTab.SKINS },
                    onOpenSettings = { currentTab = NavTab.SETTINGS }
                )
            }
            NavTab.SKINS.name -> {
                SkinsScreen(
                    viewModel = viewModel,
                    uiState = uiState,
                    onBackToHome = { currentTab = NavTab.HOME },
                    onOpenMarket = { currentTab = NavTab.MARKET },
                    onOpenSettings = { currentTab = NavTab.SETTINGS }
                )
            }
            NavTab.SETTINGS.name -> {
                SettingsScreen(
                    viewModel = viewModel,
                    uiState = uiState,
                    onBackToHome = { currentTab = NavTab.HOME },
                    onOpenMarket = { currentTab = NavTab.MARKET },
                    onOpenSkins = { currentTab = NavTab.SKINS }
                )
            }
        }
    }
}
