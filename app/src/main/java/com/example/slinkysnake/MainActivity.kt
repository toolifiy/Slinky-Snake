package com.example.slinkysnake

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.slinkysnake.model.Direction
import com.example.slinkysnake.ui.screens.AchievementsDialog
import com.example.slinkysnake.ui.screens.GamePlayScreen
import com.example.slinkysnake.ui.screens.HomeScreen
import com.example.slinkysnake.ui.screens.PowerUpGuideDialog
import com.example.slinkysnake.ui.screens.SettingsDialog
import com.example.slinkysnake.ui.screens.SkinSelectorDialog
import com.example.slinkysnake.ui.theme.SlinkySnakeTheme
import com.example.slinkysnake.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SlinkySnakeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SlinkySnakeApp(viewModel = viewModel)
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

    var showSkinsDialog by remember { mutableStateOf(false) }
    var showAchievementsDialog by remember { mutableStateOf(false) }
    var showGuideDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    if (uiState.isPlaying) {
        GamePlayScreen(
            viewModel = viewModel,
            uiState = uiState,
            onBackToHome = { viewModel.exitGame() }
        )
    } else {
        HomeScreen(
            viewModel = viewModel,
            uiState = uiState,
            onStartGame = { viewModel.startGame() },
            onOpenSkins = { showSkinsDialog = true },
            onOpenAchievements = { showAchievementsDialog = true },
            onOpenGuide = { showGuideDialog = true },
            onOpenSettings = { showSettingsDialog = true }
        )
    }

    // Dialogs
    if (showSkinsDialog) {
        SkinSelectorDialog(
            selectedSkin = uiState.selectedSkin,
            onSelectSkin = { skin -> viewModel.selectSkin(skin) },
            onDismiss = { showSkinsDialog = false }
        )
    }

    if (showAchievementsDialog) {
        AchievementsDialog(
            unlockedAchievements = uiState.unlockedAchievements,
            onDismiss = { showAchievementsDialog = false }
        )
    }

    if (showGuideDialog) {
        PowerUpGuideDialog(
            onDismiss = { showGuideDialog = false }
        )
    }

    if (showSettingsDialog) {
        SettingsDialog(
            currentThemeId = uiState.boardThemeId,
            speedMultiplier = uiState.speedMultiplier,
            isSoundEnabled = uiState.isSoundEnabled,
            soundVolume = uiState.soundVolume,
            allowedFruits = uiState.allowedFruits,
            onSelectTheme = { themeId -> viewModel.setBoardTheme(themeId) },
            onSpeedChange = { mult -> viewModel.setSpeedMultiplier(mult) },
            onSoundToggle = { enabled -> viewModel.setSoundEnabled(enabled) },
            onVolumeChange = { vol -> viewModel.setSoundVolume(vol) },
            onFruitToggle = { fruit -> viewModel.toggleFruit(fruit) },
            onResetProgress = { viewModel.resetAllProgress() },
            onDismiss = { showSettingsDialog = false }
        )
    }
}
