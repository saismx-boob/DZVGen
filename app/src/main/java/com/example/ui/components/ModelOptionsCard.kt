package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import com.example.model.AiEngine
import com.example.model.CameraMotions
import com.example.model.ModelGenerationProfile
import com.example.ui.theme.StudioAmberFlame
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioDarkBg
import com.example.ui.theme.StudioDarkSurface
import com.example.ui.theme.StudioDarkSurfaceElevated
import com.example.ui.theme.StudioDarkSurfaceVariant
import com.example.ui.theme.StudioEmeraldGlow
import com.example.ui.theme.StudioNeonCyan
import com.example.ui.theme.StudioNeonPink
import com.example.ui.theme.StudioNeonViolet
import com.example.ui.theme.StudioNeonVioletLight
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioTextPrimary
import com.example.ui.theme.StudioTextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ModelOptionsCard(
    engine: AiEngine,
    steps: Int,
    onStepsChange: (Int) -> Unit,
    cfgScale: Float,
    onCfgScaleChange: (Float) -> Unit,
    sampler: String,
    onSamplerChange: (String) -> Unit,
    fps: Int,
    onFpsChange: (Int) -> Unit,
    videoDuration: Int,
    onDurationChange: (Int) -> Unit,
    motionScore: Int,
    onMotionScoreChange: (Int) -> Unit,
    selectedCameraMotion: String,
    onCameraMotionChange: (String) -> Unit,
    negativePrompt: String,
    onNegativePromptChange: (String) -> Unit,
    seed: Long,
    onSeedChange: (Long) -> Unit,
    isRandomSeed: Boolean,
    onRandomSeedToggle: (Boolean) -> Unit,
    onResetToModelDefaults: () -> Unit,
    isExpanded: Boolean,
    onToggleExpanded: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profile: ModelGenerationProfile = engine.profile
    val isVideo = engine.mediaType == com.example.model.MediaType.VIDEO

    val accentColor = when {
        engine.isLtx -> StudioEmeraldGlow
        isVideo -> StudioNeonPink
        engine == AiEngine.FLUX_SCHNELL -> StudioNeonCyan
        else -> StudioNeonVioletLight
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = StudioDarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Model Name + Architecture tag + Expand/Collapse
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpanded() }
                    .testTag("model_options_header"),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(accentColor.copy(alpha = 0.2f), CircleShape)
                            .border(1.dp, accentColor.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isVideo) Icons.Default.Videocam else Icons.Default.Tune,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "OPTIONS DU MODÈLE : ${engine.displayName.uppercase()}",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = StudioTextPrimary
                            )

                            if (engine.isFree) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = StudioEmeraldGlow.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "GRATUIT",
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = StudioEmeraldGlow
                                    )
                                }
                            }
                        }

                        Text(
                            text = profile.architectureName,
                            fontSize = 11.sp,
                            color = accentColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Réduire" else "Dérouler",
                    tint = StudioTextSecondary
                )
            }

            // Summary description
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = profile.summary,
                fontSize = 11.sp,
                color = StudioTextSecondary,
                lineHeight = 15.sp
            )

            // Feature Highlights tags
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                profile.featureHighlights.forEach { tag ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = StudioDarkSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(0.5.dp, StudioBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .background(accentColor, CircleShape)
                            )
                            Text(
                                text = tag,
                                fontSize = 10.sp,
                                color = StudioTextPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Collapsible Detailed Model Parameters
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 14.dp)) {
                    // Recommendation Banner
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = StudioDarkBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Column {
                                Text(
                                    text = "Recommandation de prompt pour ce modèle :",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudioTextPrimary
                                )
                                Text(
                                    text = profile.promptRecommendation,
                                    fontSize = 11.sp,
                                    color = StudioTextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Reset to recommended button
                    OutlinedButton(
                        onClick = onResetToModelDefaults,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = accentColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp)
                            .testTag("reset_model_defaults_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Importer les paramètres optimaux du modèle",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 1. MODEL SAMPLER / SCHEDULER SELECTOR
                    if (profile.supportedSamplers.isNotEmpty()) {
                        Text(
                            text = "ÉCHANTILLONNEUR (SAMPLER / SCHEDULER) :",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = StudioTextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(profile.supportedSamplers) { samp ->
                                val isSelected = samp == sampler || (sampler == "Default" && samp == profile.defaultSampler)
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) accentColor.copy(alpha = 0.25f) else StudioDarkBg,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) accentColor else StudioBorder
                                    ),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { onSamplerChange(samp) }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = accentColor,
                                                modifier = Modifier.size(12.dp)
                                            )
                                        }
                                        Text(
                                            text = samp,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) StudioTextPrimary else StudioTextSecondary
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    // 2. MODEL STEPS SLIDER (Adapted to model min..max)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Étapes de diffusion / génération (Steps) :",
                            fontSize = 12.sp,
                            color = StudioTextSecondary
                        )
                        Text(
                            text = "$steps étapes (Optimal : ${profile.defaultSteps})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )
                    }

                    val stepRange = profile.minSteps.toFloat()..profile.maxSteps.toFloat()
                    val stepSteps = (profile.maxSteps - profile.minSteps).coerceAtLeast(1)
                    val safeSteps = steps.coerceIn(profile.minSteps, profile.maxSteps)

                    Slider(
                        value = safeSteps.toFloat(),
                        onValueChange = { onStepsChange(it.toInt()) },
                        valueRange = stepRange,
                        steps = (stepSteps - 1).coerceIn(0, 30),
                        colors = SliderDefaults.colors(
                            thumbColor = accentColor,
                            activeTrackColor = accentColor,
                            inactiveTrackColor = StudioDarkSurfaceVariant
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("slider_model_steps")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 3. MODEL CFG / GUIDANCE SCALE
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Échelle de guidage (CFG Scale) :",
                            fontSize = 12.sp,
                            color = StudioTextSecondary
                        )
                        Text(
                            text = "${String.format("%.1f", cfgScale)} (Recommandé : ${profile.defaultCfg})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )
                    }

                    val cfgRange = profile.minCfg..profile.maxCfg
                    val safeCfg = cfgScale.coerceIn(profile.minCfg, profile.maxCfg)

                    Slider(
                        value = safeCfg,
                        onValueChange = onCfgScaleChange,
                        valueRange = cfgRange,
                        steps = 15,
                        colors = SliderDefaults.colors(
                            thumbColor = accentColor,
                            activeTrackColor = accentColor,
                            inactiveTrackColor = StudioDarkSurfaceVariant
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("slider_model_cfg")
                    )

                    // 4. VIDEO-SPECIFIC CONTROLS (FPS, Duration, Motion, Camera)
                    if (isVideo) {
                        Spacer(modifier = Modifier.height(14.dp))

                        // FPS Selector
                        if (profile.supportedFps.isNotEmpty()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Fréquence d'images (FPS) :",
                                    fontSize = 12.sp,
                                    color = StudioTextSecondary
                                )
                                Text(
                                    text = "$fps FPS",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = accentColor
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                profile.supportedFps.forEach { frameRate ->
                                    val isFpsSelected = frameRate == fps
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isFpsSelected) accentColor.copy(alpha = 0.25f) else StudioDarkBg,
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isFpsSelected) accentColor else StudioBorder
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { onFpsChange(frameRate) }
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "$frameRate FPS",
                                                fontSize = 12.sp,
                                                fontWeight = if (isFpsSelected) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isFpsSelected) StudioTextPrimary else StudioTextSecondary
                                            )
                                            if (frameRate == 60) {
                                                Text(
                                                    text = "Ultra Fluide",
                                                    fontSize = 8.sp,
                                                    color = StudioEmeraldGlow,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                        }

                        // Duration Selector (5s / 10s)
                        if (profile.supportedDurations.isNotEmpty()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Durée de la vidéo :",
                                    fontSize = 12.sp,
                                    color = StudioTextSecondary
                                )
                                Text(
                                    text = "$videoDuration secondes",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = accentColor
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                profile.supportedDurations.forEach { dur ->
                                    val isDurSelected = dur == videoDuration
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isDurSelected) accentColor.copy(alpha = 0.25f) else StudioDarkBg,
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isDurSelected) accentColor else StudioBorder
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { onDurationChange(dur) }
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "$dur Secondes",
                                                fontSize = 12.sp,
                                                fontWeight = if (isDurSelected) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isDurSelected) StudioTextPrimary else StudioTextSecondary
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                        }

                        // Motion Dynamism
                        if (profile.supportsMotionScore) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${profile.motionScoreLabel} :",
                                    fontSize = 12.sp,
                                    color = StudioTextSecondary
                                )
                                Text(
                                    text = "$motionScore / 10",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = accentColor
                                )
                            }
                            Slider(
                                value = motionScore.toFloat(),
                                onValueChange = { onMotionScoreChange(it.toInt()) },
                                valueRange = 1f..10f,
                                steps = 8,
                                colors = SliderDefaults.colors(
                                    thumbColor = accentColor,
                                    activeTrackColor = accentColor,
                                    inactiveTrackColor = StudioDarkSurfaceVariant
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // Camera Trajectory
                        if (profile.supportsCameraMotion) {
                            Text(
                                text = "Trajectoire de caméra cinématique :",
                                fontSize = 12.sp,
                                color = StudioTextSecondary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(CameraMotions.list) { motion ->
                                    val isMotionSelected = motion.name == selectedCameraMotion
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isMotionSelected) accentColor.copy(alpha = 0.25f) else StudioDarkBg,
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isMotionSelected) accentColor else StudioBorder
                                        ),
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { onCameraMotionChange(motion.name) }
                                    ) {
                                        Text(
                                            text = motion.name,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                            fontSize = 11.sp,
                                            fontWeight = if (isMotionSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isMotionSelected) Color.White else StudioTextSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 5. NEGATIVE PROMPT (if supported)
                    Spacer(modifier = Modifier.height(14.dp))
                    if (profile.supportsNegativePrompt) {
                        Text(
                            text = "Prompt Négatif (éléments à exclure) :",
                            fontSize = 12.sp,
                            color = StudioTextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = negativePrompt,
                            onValueChange = onNegativePromptChange,
                            placeholder = {
                                Text("flou, mauvaise qualité, déformation, filigrane...", color = StudioTextMuted, fontSize = 11.sp)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = accentColor,
                                unfocusedBorderColor = StudioBorder,
                                focusedContainerColor = StudioDarkBg,
                                unfocusedContainerColor = StudioDarkBg,
                                focusedTextColor = StudioTextPrimary,
                                unfocusedTextColor = StudioTextPrimary
                            )
                        )
                    } else {
                        // Notice for models like FLUX that do not need negative prompts
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = StudioDarkBg,
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = StudioNeonCyan,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Le modèle ${engine.displayName} utilise une distillation directe Flow Matching (pas de prompt négatif requis).",
                                    fontSize = 10.sp,
                                    color = StudioTextSecondary
                                )
                            }
                        }
                    }

                    // 6. SEED CONFIGURATION
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Graine Aléatoire (Seed)",
                                fontSize = 12.sp,
                                color = StudioTextPrimary,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = if (isRandomSeed) "Nouvelle graine générée à chaque essai" else "Graine fixe : #$seed",
                                fontSize = 10.sp,
                                color = StudioTextMuted
                            )
                        }

                        Switch(
                            checked = isRandomSeed,
                            onCheckedChange = onRandomSeedToggle,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = accentColor,
                                checkedTrackColor = accentColor.copy(alpha = 0.3f),
                                uncheckedThumbColor = StudioTextMuted,
                                uncheckedTrackColor = StudioDarkSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    }
}
