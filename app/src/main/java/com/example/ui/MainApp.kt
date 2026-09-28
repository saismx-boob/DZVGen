package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CreationEntity
import com.example.ui.screens.DetailScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StudioScreen
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioDarkBg
import com.example.ui.theme.StudioDarkSurface
import com.example.ui.theme.StudioDarkSurfaceElevated
import com.example.ui.theme.StudioNeonCyan
import com.example.ui.theme.StudioNeonViolet
import com.example.ui.theme.StudioNeonVioletLight
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioTextPrimary
import com.example.ui.theme.StudioTextSecondary
import com.example.ui.viewmodel.HistoryViewModel
import com.example.ui.viewmodel.SettingsViewModel
import com.example.ui.viewmodel.StudioViewModel

enum class StudioDestination(val title: String, val testTag: String) {
    STUDIO("Créer", "nav_studio"),
    HISTORY("Historique", "nav_history"),
    SETTINGS("Paramètres", "nav_settings")
}

@Composable
fun MainApp(
    studioViewModel: StudioViewModel,
    historyViewModel: HistoryViewModel,
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    var currentDestination by remember { mutableStateOf(StudioDestination.STUDIO) }
    var selectedCreationForDetail by remember { mutableStateOf<CreationEntity?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDarkBg),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (selectedCreationForDetail == null) {
                Surface(
                    color = StudioDarkSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    NavigationBar(
                        containerColor = StudioDarkSurface,
                        contentColor = StudioTextPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .height(64.dp)
                    ) {
                        // Tab 1: Studio
                        val isStudio = currentDestination == StudioDestination.STUDIO
                        NavigationBarItem(
                            selected = isStudio,
                            onClick = { currentDestination = StudioDestination.STUDIO },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Studio",
                                    tint = if (isStudio) StudioNeonCyan else StudioTextMuted,
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = "Créer",
                                    fontSize = 11.sp,
                                    fontWeight = if (isStudio) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isStudio) StudioTextPrimary else StudioTextMuted
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = StudioNeonViolet.copy(alpha = 0.25f)
                            ),
                            modifier = Modifier.testTag(StudioDestination.STUDIO.testTag)
                        )

                        // Tab 2: History
                        val isHistory = currentDestination == StudioDestination.HISTORY
                        NavigationBarItem(
                            selected = isHistory,
                            onClick = { currentDestination = StudioDestination.HISTORY },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.GridView,
                                    contentDescription = "Historique",
                                    tint = if (isHistory) StudioNeonCyan else StudioTextMuted,
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = "Historique",
                                    fontSize = 11.sp,
                                    fontWeight = if (isHistory) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isHistory) StudioTextPrimary else StudioTextMuted
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = StudioNeonViolet.copy(alpha = 0.25f)
                            ),
                            modifier = Modifier.testTag(StudioDestination.HISTORY.testTag)
                        )

                        // Tab 3: Settings
                        val isSettings = currentDestination == StudioDestination.SETTINGS
                        NavigationBarItem(
                            selected = isSettings,
                            onClick = { currentDestination = StudioDestination.SETTINGS },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Paramètres",
                                    tint = if (isSettings) StudioNeonCyan else StudioTextMuted,
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = "Paramètres",
                                    fontSize = 11.sp,
                                    fontWeight = if (isSettings) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSettings) StudioTextPrimary else StudioTextMuted
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = StudioNeonViolet.copy(alpha = 0.25f)
                            ),
                            modifier = Modifier.testTag(StudioDestination.SETTINGS.testTag)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
        ) {
            val detailItem = selectedCreationForDetail
            if (detailItem != null) {
                DetailScreen(
                    creation = detailItem,
                    onBack = { selectedCreationForDetail = null },
                    onRemix = { itemToRemix ->
                        studioViewModel.loadForRemix(itemToRemix)
                        selectedCreationForDetail = null
                        currentDestination = StudioDestination.STUDIO
                    },
                    onToggleFavorite = { item ->
                        historyViewModel.toggleFavorite(item)
                        // update locally displayed item
                        selectedCreationForDetail = item.copy(isFavorite = !item.isFavorite)
                    },
                    onDelete = { id ->
                        historyViewModel.deleteItem(id)
                        selectedCreationForDetail = null
                    }
                )
            } else {
                AnimatedContent(
                    targetState = currentDestination,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "tab_navigation"
                ) { target ->
                    when (target) {
                        StudioDestination.STUDIO -> {
                            StudioScreen(
                                viewModel = studioViewModel,
                                onNavigateToDetail = { creation ->
                                    selectedCreationForDetail = creation
                                }
                            )
                        }
                        StudioDestination.HISTORY -> {
                            HistoryScreen(
                                viewModel = historyViewModel,
                                onNavigateToDetail = { creation ->
                                    selectedCreationForDetail = creation
                                },
                                onNavigateToStudio = {
                                    currentDestination = StudioDestination.STUDIO
                                }
                            )
                        }
                        StudioDestination.SETTINGS -> {
                            SettingsScreen(
                                viewModel = settingsViewModel,
                                snackbarHostState = snackbarHostState
                            )
                        }
                    }
                }
            }
        }
    }
}
