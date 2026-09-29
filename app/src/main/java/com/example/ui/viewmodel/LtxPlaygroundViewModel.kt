package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.CreationEntity
import com.example.data.local.PreferencesManager
import com.example.data.remote.GenerationProgress
import com.example.data.remote.LtxApiClient
import com.example.data.repository.CreationRepository
import com.example.model.AiEngine
import com.example.model.CameraMotions
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

data class LtxPlaygroundState(
    val imageUri: String? = null,
    val prompt: String = "Cinematic camera slowly pushes in on the subject with warm volumetric sunlight, subtle hair movement, fluid depth of field",
    val negativePrompt: String = "blur, distorted, jitter, bad anatomy, low quality, artifacts, watermark",
    val selectedModel: String = "ltx-2-5-pro",
    val durationSeconds: Int = 5,
    val fps: Int = 60,
    val resolution: String = "1080p",
    val aspectRatio: String = "16:9", // 16:9, 9:16, 1:1
    val cameraMotion: String = "Zoom Avant Fluide (Dolly In)",
    val seed: Long = 0L,
    val isRandomSeed: Boolean = true,
    val apiKey: String = "",
    val isGenerating: Boolean = false,
    val currentStepText: String = "",
    val progressPercent: Int = 0,
    val lastGeneratedCreation: CreationEntity? = null,
    val errorMessage: String? = null,
    val showCurlDialog: Boolean = false,
    val showApiKeyDialog: Boolean = false
)

class LtxPlaygroundViewModel(
    private val repository: CreationRepository,
    private val prefs: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LtxPlaygroundState())
    val uiState: StateFlow<LtxPlaygroundState> = _uiState.asStateFlow()

    private var generationJob: Job? = null

    init {
        _uiState.update { it.copy(apiKey = prefs.ltxApiKey) }
    }

    fun onImageSelected(uri: String?) {
        _uiState.update { it.copy(imageUri = uri) }
    }

    fun onPromptChange(newPrompt: String) {
        _uiState.update { it.copy(prompt = newPrompt) }
    }

    fun onNegativePromptChange(newNegative: String) {
        _uiState.update { it.copy(negativePrompt = newNegative) }
    }

    fun onModelChange(model: String) {
        _uiState.update { it.copy(selectedModel = model) }
    }

    fun onDurationChange(seconds: Int) {
        _uiState.update { it.copy(durationSeconds = seconds) }
    }

    fun onFpsChange(fps: Int) {
        _uiState.update { it.copy(fps = fps) }
    }

    fun onResolutionChange(res: String) {
        _uiState.update { it.copy(resolution = res) }
    }

    fun onAspectRatioChange(ratio: String) {
        _uiState.update { it.copy(aspectRatio = ratio) }
    }

    fun onCameraMotionChange(motion: String) {
        _uiState.update { it.copy(cameraMotion = motion) }
    }

    fun onSeedChange(seed: Long) {
        _uiState.update { it.copy(seed = seed) }
    }

    fun onRandomSeedToggle(isRandom: Boolean) {
        _uiState.update { it.copy(isRandomSeed = isRandom) }
    }

    fun onApiKeyChange(key: String) {
        _uiState.update { it.copy(apiKey = key) }
    }

    fun saveApiKey(key: String) {
        prefs.ltxApiKey = key.trim()
        _uiState.update { it.copy(apiKey = key.trim(), showApiKeyDialog = false) }
    }

    fun toggleCurlDialog(show: Boolean) {
        _uiState.update { it.copy(showCurlDialog = show) }
    }

    fun toggleApiKeyDialog(show: Boolean) {
        _uiState.update { it.copy(showApiKeyDialog = show) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun applyMotionPreset(preset: String) {
        _uiState.update { current ->
            val updated = if (current.prompt.isBlank()) preset else "${current.prompt}, $preset"
            current.copy(prompt = updated)
        }
    }

    fun generateLtxVideo() {
        val state = _uiState.value
        if (state.imageUri.isNullOrBlank()) {
            _uiState.update { it.copy(errorMessage = "Veuillez sélectionner une image de départ à animer.") }
            return
        }

        generationJob?.cancel()
        generationJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isGenerating = true,
                    currentStepText = "Connexion à console.ltx.io...",
                    progressPercent = 5,
                    errorMessage = null
                )
            }

            val targetEngine = when (state.selectedModel) {
                "ltx-2-5-pro" -> AiEngine.LTX_VIDEO_2_5
                "ltx-2-5-fast" -> AiEngine.LTX_VIDEO_2_5
                "ltx-2-3" -> AiEngine.LTX_VIDEO_2_3
                "ltx-2-0" -> AiEngine.LTX_VIDEO_2_0
                else -> AiEngine.LTX_VIDEO
            }

            val seedToUse = if (state.isRandomSeed || state.seed == 0L) {
                Random.nextLong(100000, 99999999)
            } else {
                state.seed
            }

            repository.generateCreative(
                prompt = state.prompt,
                negativePrompt = state.negativePrompt,
                engine = targetEngine,
                stylePreset = "Cinematic LTX",
                aspectRatio = state.aspectRatio,
                steps = 30,
                cfgScale = 3.5f,
                seed = seedToUse,
                durationSeconds = state.durationSeconds,
                cameraMotion = state.cameraMotion,
                motionScore = 8,
                sourceImageUrl = state.imageUri,
                inputMode = "IMAGE_TO_VIDEO",
                imageStrength = 0.85f,
                fps = state.fps,
                sampler = "Lightricks DiT Solver"
            ).collect { progress ->
                when (progress) {
                    is GenerationProgress.Status -> {
                        _uiState.update {
                            it.copy(
                                currentStepText = progress.step,
                                progressPercent = progress.percentage
                            )
                        }
                    }
                    is GenerationProgress.Success -> {
                        repository.saveCreation(progress.creation)
                        _uiState.update {
                            it.copy(
                                isGenerating = false,
                                progressPercent = 100,
                                lastGeneratedCreation = progress.creation
                            )
                        }
                    }
                    is GenerationProgress.Error -> {
                        _uiState.update {
                            it.copy(
                                isGenerating = false,
                                errorMessage = progress.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun cancelGeneration() {
        generationJob?.cancel()
        _uiState.update {
            it.copy(
                isGenerating = false,
                currentStepText = "",
                progressPercent = 0
            )
        }
    }
}
