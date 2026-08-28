package com.example.slinkysnake

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import com.example.slinkysnake.model.Direction
import com.example.slinkysnake.ui.components.NavTab
import com.example.slinkysnake.ui.screens.GamePlayScreen
import com.example.slinkysnake.ui.screens.HomeScreen
import com.example.slinkysnake.ui.screens.MissionsScreen
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

@Composable
fun SlinkySnakeApp(viewModel: GameViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    var currentTab by remember { mutableStateOf(NavTab.HOME) }

    val isGameActive = uiState.isPlaying || uiState.showGameOver || uiState.showLevelClear || uiState.showVictory

    if (isGameActive) {
        GamePlayScreen(
            viewModel = viewModel,
            uiState = uiState,
            onBackToHome = { viewModel.exitGame() }
        )
    } else {
        when (currentTab) {
            NavTab.HOME -> {
                HomeScreen(
                    viewModel = viewModel,
                    uiState = uiState,
                    onStartGame = { viewModel.startGame() },
                    onOpenSkins = { currentTab = NavTab.SKINS },
                    onOpenAchievements = { currentTab = NavTab.MISSIONS },
                    onOpenSettings = { currentTab = NavTab.SETTINGS }
                )
            }
            NavTab.MISSIONS -> {
                MissionsScreen(
                    viewModel = viewModel,
                    uiState = uiState,
                    onBackToHome = { currentTab = NavTab.HOME },
                    onOpenSkins = { currentTab = NavTab.SKINS },
                    onOpenSettings = { currentTab = NavTab.SETTINGS }
                )
            }
            NavTab.SKINS -> {
                SkinsScreen(
                    viewModel = viewModel,
                    uiState = uiState,
                    onBackToHome = { currentTab = NavTab.HOME },
                    onOpenMissions = { currentTab = NavTab.MISSIONS },
                    onOpenSettings = { currentTab = NavTab.SETTINGS }
                )
            }
            NavTab.SETTINGS -> {
                SettingsScreen(
                    viewModel = viewModel,
                    uiState = uiState,
                    onBackToHome = { currentTab = NavTab.HOME },
                    onOpenMissions = { currentTab = NavTab.MISSIONS },
                    onOpenSkins = { currentTab = NavTab.SKINS }
                )
            }
        }
    }
}
