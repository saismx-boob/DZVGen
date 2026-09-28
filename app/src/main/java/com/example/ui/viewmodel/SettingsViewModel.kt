package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.PreferencesManager
import com.example.data.repository.CreationRepository
import com.example.model.AiEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(
    val stabilityApiKey: String = "",
    val runwayApiKey: String = "",
    val defaultEngine: AiEngine = AiEngine.STABLE_DIFFUSION_XL,
    val autoSave: Boolean = true,
    val autoEnhancePrompt: Boolean = false,
    val savedMessage: String? = null
)

class SettingsViewModel(
    private val prefs: PreferencesManager,
    private val repository: CreationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        val defaultId = prefs.defaultEngineId
        val engine = AiEngine.values().find { it.id == defaultId } ?: AiEngine.STABLE_DIFFUSION_XL
        _uiState.update {
            it.copy(
                stabilityApiKey = prefs.stabilityApiKey,
                runwayApiKey = prefs.runwayApiKey,
                defaultEngine = engine,
                autoSave = prefs.autoSave,
                autoEnhancePrompt = prefs.autoEnhancePrompt
            )
        }
    }

    fun onStabilityKeyChange(key: String) {
        _uiState.update { it.copy(stabilityApiKey = key) }
    }

    fun onRunwayKeyChange(key: String) {
        _uiState.update { it.copy(runwayApiKey = key) }
    }

    fun onDefaultEngineChange(engine: AiEngine) {
        _uiState.update { it.copy(defaultEngine = engine) }
    }

    fun onAutoSaveChange(value: Boolean) {
        _uiState.update { it.copy(autoSave = value) }
    }

    fun onAutoEnhanceChange(value: Boolean) {
        _uiState.update { it.copy(autoEnhancePrompt = value) }
    }

    fun saveAll() {
        val s = _uiState.value
        prefs.stabilityApiKey = s.stabilityApiKey.trim()
        prefs.runwayApiKey = s.runwayApiKey.trim()
        prefs.defaultEngineId = s.defaultEngine.id
        prefs.autoSave = s.autoSave
        prefs.autoEnhancePrompt = s.autoEnhancePrompt
        _uiState.update { it.copy(savedMessage = "Paramètres enregistrés avec succès !") }
    }

    fun restoreShowcaseSamples() {
        viewModelScope.launch {
            repository.restoreSamples()
            _uiState.update { it.copy(savedMessage = "Exemples de démonstration restaurés !") }
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearHistory()
            _uiState.update { it.copy(savedMessage = "Historique vidé.") }
        }
    }

    fun dismissMessage() {
        _uiState.update { it.copy(savedMessage = null) }
    }
}
