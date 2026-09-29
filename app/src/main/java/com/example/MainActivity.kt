package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.local.AppDatabase
import com.example.data.local.PreferencesManager
import com.example.data.remote.AiGenerationService
import com.example.data.repository.CreationRepository
import com.example.ui.MainApp
import com.example.ui.theme.VisionAITheme
import com.example.ui.viewmodel.HistoryViewModel
import com.example.ui.viewmodel.LtxPlaygroundViewModel
import com.example.ui.viewmodel.SettingsViewModel
import com.example.ui.viewmodel.StudioViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(applicationContext)
        val preferencesManager = PreferencesManager(applicationContext)
        val aiService = AiGenerationService(applicationContext, preferencesManager)
        val repository = CreationRepository(database.creationDao(), aiService)

        val studioViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return StudioViewModel(repository, preferencesManager) as T
                }
            }
        )[StudioViewModel::class.java]

        val ltxPlaygroundViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return LtxPlaygroundViewModel(repository, preferencesManager) as T
                }
            }
        )[LtxPlaygroundViewModel::class.java]

        val historyViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HistoryViewModel(repository) as T
                }
            }
        )[HistoryViewModel::class.java]

        val settingsViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(preferencesManager, repository) as T
                }
            }
        )[SettingsViewModel::class.java]

        setContent {
            VisionAITheme {
                MainApp(
                    studioViewModel = studioViewModel,
                    ltxPlaygroundViewModel = ltxPlaygroundViewModel,
                    historyViewModel = historyViewModel,
                    settingsViewModel = settingsViewModel
                )
            }
        }
    }
}
