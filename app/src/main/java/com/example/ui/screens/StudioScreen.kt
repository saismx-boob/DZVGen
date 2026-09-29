package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.CreationEntity
import com.example.data.remote.PromptEnhancer
import com.example.model.CameraMotions
import com.example.model.MediaType
import com.example.ui.components.AdvancedPromptBuilder
import com.example.ui.components.AspectRatioSelector
import com.example.ui.components.EngineCardSelector
import com.example.ui.components.GenerationModeSelector
import com.example.ui.components.LiveRenderDialog
import com.example.ui.components.ModelOptionsCard
import com.example.ui.components.StylePresetSelector
import com.example.ui.theme.StudioAmberFlame
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioDarkBg
import com.example.ui.theme.StudioDarkSurface
import com.example.ui.theme.StudioDarkSurfaceElevated
import com.example.ui.theme.StudioDarkSurfaceVariant
import com.example.ui.theme.StudioNeonCyan
import com.example.ui.theme.StudioNeonPink
import com.example.ui.theme.StudioNeonViolet
import com.example.ui.theme.StudioNeonVioletLight
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioTextPrimary
import com.example.ui.theme.StudioTextSecondary
import com.example.ui.viewmodel.StudioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioScreen(
    viewModel: StudioViewModel,
    onNavigateToDetail: (CreationEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isVideo = uiState.selectedEngine.mediaType == MediaType.VIDEO

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDarkBg)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Title Banner
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = StudioNeonViolet.copy(alpha = 0.25f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = StudioNeonCyan,
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(18.dp)
                            )
                        }
                        Text(
                            text = "VisionAI Studio",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.5.sp
                            ),
                            color = StudioTextPrimary
                        )
                    }

                    Text(
                        text = "Génération créative haute performance avec LTX Video, FLUX.1, Stable Diffusion & Runway ML",
                        style = MaterialTheme.typography.bodySmall,
                        color = StudioTextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick Free / Popular Model Bar
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        item {
                            val isLtx25 = uiState.selectedEngine == com.example.model.AiEngine.LTX_VIDEO_2_5
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isLtx25) com.example.ui.theme.StudioEmeraldGlow.copy(alpha = 0.35f) else StudioDarkSurface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isLtx25) com.example.ui.theme.StudioEmeraldGlow else StudioBorder),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { viewModel.onSelectEngine(com.example.model.AiEngine.LTX_VIDEO_2_5) }
                                    .testTag("quick_select_ltx_2_5")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .background(com.example.ui.theme.StudioEmeraldGlow, CircleShape)
                                    )
                                    Text(
                                        text = "🎬 LTX 2.5 Turbo (Gratuit)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isLtx25) Color.White else StudioTextPrimary
                                    )
                                }
                            }
                        }

                        item {
                            val isLtx23 = uiState.selectedEngine == com.example.model.AiEngine.LTX_VIDEO_2_3
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isLtx23) com.example.ui.theme.StudioEmeraldGlow.copy(alpha = 0.35f) else StudioDarkSurface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isLtx23) com.example.ui.theme.StudioEmeraldGlow else StudioBorder),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { viewModel.onSelectEngine(com.example.model.AiEngine.LTX_VIDEO_2_3) }
                                    .testTag("quick_select_ltx_2_3")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .background(StudioNeonCyan, CircleShape)
                                    )
                                    Text(
                                        text = "🎥 LTX 2.3 HD (Gratuit)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isLtx23) Color.White else StudioTextPrimary
                                    )
                                }
                            }
                        }

                        item {
                            val isFlux = uiState.selectedEngine == com.example.model.AiEngine.FLUX_SCHNELL
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isFlux) StudioNeonViolet.copy(alpha = 0.3f) else StudioDarkSurface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isFlux) StudioNeonCyan else StudioBorder),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { viewModel.onSelectEngine(com.example.model.AiEngine.FLUX_SCHNELL) }
                                    .testTag("quick_select_flux_schnell")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .background(StudioNeonCyan, CircleShape)
                                    )
                                    Text(
                                        text = "🎨 FLUX.1 (Gratuit)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isFlux) Color.White else StudioTextPrimary
                                    )
                                }
                            }
                        }

                        item {
                            val isRunway = uiState.selectedEngine == com.example.model.AiEngine.RUNWAY_GEN3
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isRunway) StudioNeonPink.copy(alpha = 0.3f) else StudioDarkSurface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isRunway) StudioNeonPink else StudioBorder),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { viewModel.onSelectEngine(com.example.model.AiEngine.RUNWAY_GEN3) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "⚡ Runway Gen-3",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isRunway) Color.White else StudioTextPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Generation Mode Selector (Text-to-Image, Image-to-Image, Text-to-Video, Image-to-Video)
            item {
                GenerationModeSelector(
                    selectedMode = uiState.inputMode,
                    onSelectMode = { viewModel.onInputModeChange(it) },
                    sourceImageUri = uiState.sourceImageUri,
                    onSourceImageSelected = { viewModel.onSourceImageSelected(it) },
                    imageStrength = uiState.imageStrength,
                    onImageStrengthChange = { viewModel.onImageStrengthChange(it) }
                )
            }

            // Engine Selector Card
            item {
                EngineCardSelector(
                    selectedEngine = uiState.selectedEngine,
                    onSelectEngine = { viewModel.onSelectEngine(it) }
                )
            }

            // Prompt Input Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioDarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "VOTRE DESCRIPTION CRÉATIVE (PROMPT)",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = StudioTextSecondary
                            )

                            // Magic Wand Button
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = StudioNeonViolet.copy(alpha = 0.25f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, StudioNeonVioletLight.copy(alpha = 0.5f)),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { viewModel.enhanceCurrentPrompt() }
                                    .testTag("magic_enhance_prompt_button")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = "Améliorer",
                                        tint = StudioNeonCyan,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Améliorer IA",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = StudioNeonCyan
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = uiState.prompt,
                            onValueChange = { viewModel.onPromptChange(it) },
                            placeholder = {
                                Text(
                                    text = if (isVideo) {
                                        "Ex: Vue aérienne cinématique au drone d'une cité cyberpunk sous la pluie avec néons éclatants..."
                                    } else {
                                        "Ex: Portrait détaillé d'un robot barista dans un café steampunk à Paris..."
                                    },
                                    color = StudioTextMuted,
                                    fontSize = 13.sp
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .testTag("prompt_text_field"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = StudioNeonViolet,
                                unfocusedBorderColor = StudioBorder,
                                focusedContainerColor = StudioDarkBg,
                                unfocusedContainerColor = StudioDarkBg,
                                focusedTextColor = StudioTextPrimary,
                                unfocusedTextColor = StudioTextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Inspiration Chips
                        Text(
                            text = "Idées d'inspiration :",
                            style = MaterialTheme.typography.labelSmall,
                            color = StudioTextMuted
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(PromptEnhancer.samplePrompts) { sample ->
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = StudioDarkSurfaceVariant,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .clickable { viewModel.applySamplePrompt(sample) }
                                ) {
                                    Text(
                                        text = sample,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        fontSize = 11.sp,
                                        color = StudioTextSecondary,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Advanced Prompt Construction UI (Suggestions tags: styles, lighting, quality, camera, motion)
            item {
                AdvancedPromptBuilder(
                    currentPrompt = uiState.prompt,
                    onPromptChanged = { viewModel.onPromptChange(it) }
                )
            }

            // Model-Specific Creation & Generation Options
            item {
                ModelOptionsCard(
                    engine = uiState.selectedEngine,
                    steps = uiState.steps,
                    onStepsChange = { viewModel.onStepsChange(it) },
                    cfgScale = uiState.cfgScale,
                    onCfgScaleChange = { viewModel.onCfgScaleChange(it) },
                    sampler = uiState.sampler,
                    onSamplerChange = { viewModel.onSamplerChange(it) },
                    fps = uiState.fps,
                    onFpsChange = { viewModel.onFpsChange(it) },
                    videoDuration = uiState.videoDuration,
                    onDurationChange = { viewModel.onVideoDurationChange(it) },
                    motionScore = uiState.motionScore,
                    onMotionScoreChange = { viewModel.onMotionScoreChange(it) },
                    selectedCameraMotion = uiState.selectedCameraMotion,
                    onCameraMotionChange = { viewModel.onCameraMotionChange(it) },
                    negativePrompt = uiState.negativePrompt,
                    onNegativePromptChange = { viewModel.onNegativePromptChange(it) },
                    seed = uiState.seed,
                    onSeedChange = { viewModel.onSeedChange(it) },
                    isRandomSeed = uiState.isRandomSeed,
                    onRandomSeedToggle = { viewModel.onRandomSeedToggle(it) },
                    onResetToModelDefaults = { viewModel.importModelRecommendedOptions() },
                    isExpanded = uiState.showAdvParams,
                    onToggleExpanded = { viewModel.toggleAdvParams() }
                )
            }

            // Style Presets Carousel
            item {
                StylePresetSelector(
                    selectedPreset = uiState.selectedPreset,
                    onSelectPreset = { viewModel.onSelectPreset(it) }
                )
            }

            // Aspect Ratio Selector
            item {
                AspectRatioSelector(
                    selectedRatio = uiState.selectedAspectRatio,
                    onSelectRatio = { viewModel.onSelectAspectRatio(it) }
                )
            }

            // If Runway: Video duration selector (5s vs 10s)
            if (isVideo) {
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "DURÉE VIDÉO RUNWAY",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            ),
                            color = StudioTextSecondary,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf(5, 10).forEach { dur ->
                                val isDurSelected = uiState.videoDuration == dur
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(
                                            1.dp,
                                            if (isDurSelected) StudioNeonPink else StudioBorder,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable { viewModel.onVideoDurationChange(dur) },
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isDurSelected) StudioDarkSurfaceElevated else StudioDarkSurface
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "$dur Secondes",
                                            fontWeight = FontWeight.Bold,
                                            color = if (isDurSelected) Color.White else StudioTextSecondary
                                        )
                                        Text(
                                            text = if (dur == 5) "Rendu rapide 60fps" else "Séquence cinématographique",
                                            fontSize = 11.sp,
                                            color = StudioTextMuted
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Big Glowing Generate Button
            item {
                Spacer(modifier = Modifier.height(4.dp))
                val gradientBrush = if (isVideo) {
                    Brush.horizontalGradient(listOf(StudioNeonPink, StudioAmberFlame))
                } else {
                    Brush.horizontalGradient(listOf(StudioNeonViolet, StudioNeonCyan))
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(gradientBrush)
                        .clickable {
                            viewModel.startGeneration { newCreation ->
                                onNavigateToDetail(newCreation)
                            }
                        }
                        .testTag("generate_creative_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = if (isVideo) Icons.Default.PlayArrow else Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        val buttonTitle = when {
                            uiState.inputMode == com.example.model.GenerationInputMode.IMAGE_TO_VIDEO ->
                                "ANIMER L'IMAGE EN VIDÉO (${uiState.selectedEngine.displayName.uppercase()})"
                            uiState.inputMode == com.example.model.GenerationInputMode.IMAGE_TO_IMAGE ->
                                "STYLER L'IMAGE SOURCE (${uiState.selectedEngine.displayName.uppercase()})"
                            uiState.selectedEngine == com.example.model.AiEngine.LTX_VIDEO_2_5 ->
                                "GÉNÉRER AVEC LTX 2.5 TURBO (GRATUIT)"
                            uiState.selectedEngine == com.example.model.AiEngine.LTX_VIDEO_2_3 ->
                                "GÉNÉRER AVEC LTX 2.3 HD (GRATUIT)"
                            uiState.selectedEngine == com.example.model.AiEngine.LTX_VIDEO_2_0 ->
                                "GÉNÉRER AVEC LTX 2.0 PRO (GRATUIT)"
                            uiState.selectedEngine == com.example.model.AiEngine.LTX_VIDEO ->
                                "GÉNÉRER AVEC LTX VIDEO (GRATUIT)"
                            uiState.selectedEngine == com.example.model.AiEngine.FLUX_SCHNELL ->
                                "GÉNÉRER L'IMAGE FLUX.1 (GRATUIT)"
                            uiState.selectedEngine == com.example.model.AiEngine.SD_TURBO ->
                                "GÉNÉRER AVEC SDXL TURBO (GRATUIT)"
                            uiState.selectedEngine == com.example.model.AiEngine.COGVIDEOX ->
                                "GÉNÉRER AVEC COGVIDEOX (GRATUIT)"
                            uiState.selectedEngine == com.example.model.AiEngine.RUNWAY_GEN3 || uiState.selectedEngine == com.example.model.AiEngine.RUNWAY_GEN2 ->
                                "GÉNÉRER LA VIDÉO RUNWAY"
                            else -> "GÉNÉRER L'IMAGE STABLE DIFFUSION"
                        }
                        Text(
                            text = buttonTitle,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            ),
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Live Render Progress Dialog
        if (uiState.isGenerating) {
            LiveRenderDialog(
                stepText = uiState.currentStepText,
                progressPercent = uiState.progressPercent,
                engineName = uiState.selectedEngine.displayName,
                onCancel = { viewModel.cancelGeneration() }
            )
        }
    }
}
