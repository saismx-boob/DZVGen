package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.CreationEntity
import com.example.data.local.PreferencesManager
import com.example.data.remote.GenerationProgress
import com.example.data.remote.PromptEnhancer
import com.example.data.repository.CreationRepository
import com.example.model.AiEngine
import com.example.model.AspectRatioChoice
import com.example.model.CameraMotions
import com.example.model.GenerationInputMode
import com.example.model.MediaType
import com.example.model.StylePreset
import com.example.model.StylePresets
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StudioUiState(
    val prompt: String = "",
    val negativePrompt: String = "",
    val selectedEngine: AiEngine = AiEngine.STABLE_DIFFUSION_XL,
    val selectedPreset: StylePreset = StylePresets.list.first(),
    val selectedAspectRatio: AspectRatioChoice = AspectRatioChoice.SQUARE,
    val inputMode: GenerationInputMode = GenerationInputMode.TEXT_TO_IMAGE,
    val sourceImageUri: String? = null,
    val imageStrength: Float = 0.75f,
    val steps: Int = 30,
    val cfgScale: Float = 7.5f,
    val seed: Long = 0L,
    val isRandomSeed: Boolean = true,
    val videoDuration: Int = 5,
    val selectedCameraMotion: String = CameraMotions.list.first().name,
    val motionScore: Int = 6,
    val sampler: String = "Default",
    val fps: Int = 30,
    val isGenerating: Boolean = false,
    val currentStepText: String = "",
    val progressPercent: Int = 0,
    val lastGeneratedItem: CreationEntity? = null,
    val errorMessage: String? = null,
    val showAdvParams: Boolean = false
)

class StudioViewModel(
    private val repository: CreationRepository,
    private val prefs: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudioUiState())
    val uiState: StateFlow<StudioUiState> = _uiState.asStateFlow()

    private var generationJob: Job? = null

    init {
        // Load default engine from preferences and import its recommended options
        val defaultId = prefs.defaultEngineId
        val engine = AiEngine.values().find { it.id == defaultId } ?: AiEngine.STABLE_DIFFUSION_XL
        val profile = engine.profile
        _uiState.update {
            it.copy(
                selectedEngine = engine,
                steps = profile.defaultSteps,
                cfgScale = profile.defaultCfg,
                sampler = profile.defaultSampler,
                fps = profile.defaultFps,
                videoDuration = if (profile.supportedDurations.isNotEmpty()) profile.defaultDuration else 0,
                selectedAspectRatio = if (profile.supportedAspectRatios.contains(it.selectedAspectRatio)) it.selectedAspectRatio else profile.supportedAspectRatios.first()
            )
        }
    }

    fun onPromptChange(newPrompt: String) {
        _uiState.update { it.copy(prompt = newPrompt) }
    }

    fun onNegativePromptChange(newNeg: String) {
        _uiState.update { it.copy(negativePrompt = newNeg) }
    }

    fun onSelectEngine(engine: AiEngine) {
        val profile = engine.profile
        _uiState.update { current ->
            // Adjust aspect ratio default if current is not in supported list
            val ratio = if (current.selectedAspectRatio in profile.supportedAspectRatios) {
                current.selectedAspectRatio
            } else {
                profile.supportedAspectRatios.firstOrNull() ?: AspectRatioChoice.LANDSCAPE
            }

            // Sync inputMode with mediaType
            val adjustedMode = if (engine.mediaType == MediaType.VIDEO) {
                if (current.inputMode.isImageInput) GenerationInputMode.IMAGE_TO_VIDEO else GenerationInputMode.TEXT_TO_VIDEO
            } else {
                if (current.inputMode.isImageInput) GenerationInputMode.IMAGE_TO_IMAGE else GenerationInputMode.TEXT_TO_IMAGE
            }

            // Import model's exact creation & generation parameters
            current.copy(
                selectedEngine = engine,
                selectedAspectRatio = ratio,
                inputMode = adjustedMode,
                steps = profile.defaultSteps,
                cfgScale = profile.defaultCfg,
                sampler = profile.defaultSampler,
                fps = profile.defaultFps,
                videoDuration = if (profile.supportedDurations.isNotEmpty()) profile.defaultDuration else 0
            )
        }
    }

    fun importModelRecommendedOptions() {
        val profile = _uiState.value.selectedEngine.profile
        _uiState.update { current ->
            current.copy(
                steps = profile.defaultSteps,
                cfgScale = profile.defaultCfg,
                sampler = profile.defaultSampler,
                fps = profile.defaultFps,
                videoDuration = if (profile.supportedDurations.isNotEmpty()) profile.defaultDuration else 0,
                selectedAspectRatio = if (current.selectedAspectRatio in profile.supportedAspectRatios) current.selectedAspectRatio else profile.supportedAspectRatios.first()
            )
        }
    }

    fun onSamplerChange(sampler: String) {
        _uiState.update { it.copy(sampler = sampler) }
    }

    fun onFpsChange(fps: Int) {
        _uiState.update { it.copy(fps = fps) }
    }

    fun onInputModeChange(mode: GenerationInputMode) {
        _uiState.update { current ->
            // If selecting a video mode but current engine is image, switch to best video engine
            val engine = if (mode.isVideo && current.selectedEngine.mediaType != MediaType.VIDEO) {
                AiEngine.LTX_VIDEO_2_5
            } else if (!mode.isVideo && current.selectedEngine.mediaType != MediaType.IMAGE) {
                AiEngine.FLUX_SCHNELL
            } else {
                current.selectedEngine
            }

            val ratio = if (mode.isVideo && current.selectedAspectRatio == AspectRatioChoice.SQUARE) {
                AspectRatioChoice.LANDSCAPE
            } else {
                current.selectedAspectRatio
            }

            current.copy(
                inputMode = mode,
                selectedEngine = engine,
                selectedAspectRatio = ratio
            )
        }
    }

    fun onSourceImageSelected(uri: String?) {
        _uiState.update { it.copy(sourceImageUri = uri) }
    }

    fun onImageStrengthChange(strength: Float) {
        _uiState.update { it.copy(imageStrength = strength) }
    }

    fun onSelectPreset(preset: StylePreset) {
        _uiState.update { it.copy(selectedPreset = preset) }
    }

    fun onSelectAspectRatio(ratio: AspectRatioChoice) {
        _uiState.update { it.copy(selectedAspectRatio = ratio) }
    }

    fun onStepsChange(steps: Int) {
        _uiState.update { it.copy(steps = steps) }
    }

    fun onCfgScaleChange(cfg: Float) {
        _uiState.update { it.copy(cfgScale = cfg) }
    }

    fun onSeedChange(seed: Long) {
        _uiState.update { it.copy(seed = seed, isRandomSeed = false) }
    }

    fun onRandomSeedToggle(random: Boolean) {
        _uiState.update { it.copy(isRandomSeed = random, seed = if (random) 0L else 12345L) }
    }

    fun onVideoDurationChange(seconds: Int) {
        _uiState.update { it.copy(videoDuration = seconds) }
    }

    fun onCameraMotionChange(motionName: String) {
        _uiState.update { it.copy(selectedCameraMotion = motionName) }
    }

    fun onMotionScoreChange(score: Int) {
        _uiState.update { it.copy(motionScore = score) }
    }

    fun toggleAdvParams() {
        _uiState.update { it.copy(showAdvParams = !it.showAdvParams) }
    }

    fun enhanceCurrentPrompt() {
        val currentPrompt = _uiState.value.prompt
        val preset = _uiState.value.selectedPreset
        val isVideo = _uiState.value.selectedEngine.mediaType == MediaType.VIDEO
        val enhanced = PromptEnhancer.enhance(currentPrompt, preset.label, isVideo)
        _uiState.update { it.copy(prompt = enhanced) }
    }

    fun applySamplePrompt(sample: String) {
        _uiState.update { it.copy(prompt = sample) }
    }

    fun loadForRemix(creation: CreationEntity) {
        val engine = AiEngine.values().find { it.displayName == creation.modelEngine }
            ?: if (creation.type == "VIDEO") AiEngine.RUNWAY_GEN3 else AiEngine.STABLE_DIFFUSION_XL
        val preset = StylePresets.list.find { it.label == creation.stylePreset } ?: StylePresets.list.first()
        val ratio = AspectRatioChoice.values().find { it.code == creation.aspectRatio } ?: AspectRatioChoice.SQUARE

        _uiState.update {
            it.copy(
                prompt = creation.prompt,
                negativePrompt = creation.negativePrompt,
                selectedEngine = engine,
                selectedPreset = preset,
                selectedAspectRatio = ratio,
                steps = creation.steps,
                cfgScale = creation.cfgScale,
                seed = creation.seed,
                isRandomSeed = false,
                videoDuration = if (creation.durationSeconds > 0) creation.durationSeconds else 5,
                selectedCameraMotion = creation.cameraMotion,
                motionScore = creation.motionScore,
                errorMessage = null
            )
        }
    }

    fun startGeneration(onSuccess: (CreationEntity) -> Unit) {
        val state = _uiState.value
        val effectivePrompt = state.prompt.trim().ifEmpty {
            "Cinematic masterwork, vivid atmosphere, ultra high quality digital art"
        }

        val fullPrompt = effectivePrompt + state.selectedPreset.promptSuffix
        val fullNegative = (state.negativePrompt.trim() + " " + state.selectedPreset.negativeSuffix).trim()

        generationJob?.cancel()
        generationJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isGenerating = true,
                    currentStepText = "Initialisation...",
                    progressPercent = 5,
                    errorMessage = null
                )
            }

            repository.generateCreative(
                prompt = fullPrompt,
                negativePrompt = fullNegative,
                engine = state.selectedEngine,
                stylePreset = state.selectedPreset.label,
                aspectRatio = state.selectedAspectRatio.code,
                steps = state.steps,
                cfgScale = state.cfgScale,
                seed = if (state.isRandomSeed) 0L else state.seed,
                durationSeconds = state.videoDuration,
                cameraMotion = state.selectedCameraMotion,
                motionScore = state.motionScore,
                sourceImageUrl = state.sourceImageUri ?: "",
                inputMode = state.inputMode.name,
                imageStrength = state.imageStrength,
                fps = state.fps,
                sampler = state.sampler
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
                        val savedId = repository.saveCreation(progress.creation)
                        val finalItem = progress.creation.copy(id = savedId)
                        _uiState.update {
                            it.copy(
                                isGenerating = false,
                                progressPercent = 100,
                                lastGeneratedItem = finalItem
                            )
                        }
                        onSuccess(finalItem)
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
        _uiState.update { it.copy(isGenerating = false, currentStepText = "", progressPercent = 0) }
    }

    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
