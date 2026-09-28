package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.AiEngine
import com.example.model.AspectRatioChoice
import com.example.model.MediaType
import com.example.model.StylePreset
import com.example.model.StylePresets
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

@Composable
fun EngineCardSelector(
    selectedEngine: AiEngine,
    onSelectEngine: (AiEngine) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "MOTEUR D'INTELLIGENCE ARTIFICIELLE",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                ),
                color = StudioTextSecondary,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
            )

            if (selectedEngine.isFree) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = StudioEmeraldGlow.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioEmeraldGlow.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "⚡ MODÈLE GRATUIT",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = StudioEmeraldGlow
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Stable Diffusion & FLUX Card
            val isSd = selectedEngine.mediaType == MediaType.IMAGE
            val sdBorderBrush = if (isSd) {
                Brush.horizontalGradient(listOf(StudioNeonViolet, StudioNeonCyan))
            } else {
                Brush.horizontalGradient(listOf(StudioBorder, StudioBorder))
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .border(if (isSd) 1.5.dp else 1.dp, sdBorderBrush, RoundedCornerShape(14.dp))
                    .clickable {
                        if (!isSd) onSelectEngine(AiEngine.FLUX_SCHNELL)
                    }
                    .testTag("engine_stable_diffusion_button"),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSd) StudioDarkSurfaceElevated else StudioDarkSurface
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(
                                    if (isSd) StudioNeonViolet.copy(alpha = 0.25f) else StudioDarkSurfaceVariant,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Brush,
                                contentDescription = "Images IA",
                                tint = if (isSd) StudioNeonVioletLight else StudioTextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isSd) StudioNeonViolet.copy(alpha = 0.2f) else StudioDarkBg
                        ) {
                            Text(
                                text = "IMAGES",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSd) StudioNeonVioletLight else StudioTextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Images & Arts",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (isSd) StudioTextPrimary else StudioTextSecondary
                    )

                    Text(
                        text = "FLUX.1 (Gratuit) & SDXL",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.sp,
                        color = StudioTextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Runway & LTX Video Card
            val isRunway = selectedEngine.mediaType == MediaType.VIDEO
            val runwayBorderBrush = if (isRunway) {
                Brush.horizontalGradient(listOf(StudioNeonPink, StudioAmberFlame))
            } else {
                Brush.horizontalGradient(listOf(StudioBorder, StudioBorder))
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .border(if (isRunway) 1.5.dp else 1.dp, runwayBorderBrush, RoundedCornerShape(14.dp))
                    .clickable {
                        if (!isRunway) onSelectEngine(AiEngine.LTX_VIDEO_2_5)
                    }
                    .testTag("engine_runway_button"),
                colors = CardDefaults.cardColors(
                    containerColor = if (isRunway) StudioDarkSurfaceElevated else StudioDarkSurface
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(
                                    if (isRunway) StudioNeonPink.copy(alpha = 0.25f) else StudioDarkSurfaceVariant,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = "Vidéos IA",
                                tint = if (isRunway) StudioNeonPink else StudioTextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isRunway) StudioNeonPink.copy(alpha = 0.2f) else StudioDarkBg
                        ) {
                            Text(
                                text = "VIDÉOS",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isRunway) StudioNeonPink else StudioTextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Vidéos & Motion",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (isRunway) StudioTextPrimary else StudioTextSecondary
                    )

                    Text(
                        text = "LTX 2.5 & 2.3 (Gratuit)",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.sp,
                        color = StudioTextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // Sub-model pill chips with Free badges
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val availableEngines = if (selectedEngine.mediaType == MediaType.IMAGE) {
                listOf(
                    AiEngine.FLUX_SCHNELL,
                    AiEngine.SD_TURBO,
                    AiEngine.STABLE_DIFFUSION_XL,
                    AiEngine.STABLE_DIFFUSION_3
                )
            } else {
                listOf(
                    AiEngine.LTX_VIDEO_2_5,
                    AiEngine.LTX_VIDEO_2_3,
                    AiEngine.LTX_VIDEO_2_0,
                    AiEngine.RUNWAY_GEN3,
                    AiEngine.RUNWAY_GEN2,
                    AiEngine.COGVIDEOX
                )
            }

            items(availableEngines) { eng ->
                val isSubSelected = eng == selectedEngine
                FilterChip(
                    selected = isSubSelected,
                    onClick = { onSelectEngine(eng) },
                    label = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = eng.displayName,
                                fontSize = 11.sp,
                                fontWeight = if (isSubSelected) FontWeight.Bold else FontWeight.Normal
                            )
                            if (eng.isFree) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = StudioEmeraldGlow.copy(alpha = if (isSubSelected) 0.9f else 0.3f)
                                ) {
                                    Text(
                                        text = "GRATUIT",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isSubSelected) Color.Black else StudioEmeraldGlow,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = if (eng.mediaType == MediaType.IMAGE) StudioNeonViolet.copy(alpha = 0.35f) else StudioNeonPink.copy(alpha = 0.35f),
                        selectedLabelColor = StudioTextPrimary,
                        containerColor = StudioDarkSurfaceVariant,
                        labelColor = StudioTextSecondary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (isSubSelected) StudioNeonCyan else StudioBorder,
                        borderWidth = 1.dp,
                        enabled = true,
                        selected = isSubSelected
                    )
                )
            }
        }
    }
}

@Composable
fun AspectRatioSelector(
    selectedRatio: AspectRatioChoice,
    onSelectRatio: (AspectRatioChoice) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "FORMAT & RATIO",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            ),
            color = StudioTextSecondary,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AspectRatioChoice.values().forEach { ratio ->
                val isSelected = ratio == selectedRatio
                val bg = if (isSelected) StudioNeonViolet.copy(alpha = 0.25f) else StudioDarkSurface
                val border = if (isSelected) StudioNeonVioletLight else StudioBorder

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, border, RoundedCornerShape(10.dp))
                        .clickable { onSelectRatio(ratio) }
                        .testTag("ratio_${ratio.label.replace(":", "_")}"),
                    colors = CardDefaults.cardColors(containerColor = bg)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Visual box showing aspect ratio representation
                        val (boxW, boxH) = when (ratio) {
                            AspectRatioChoice.SQUARE -> Pair(16.dp, 16.dp)
                            AspectRatioChoice.LANDSCAPE -> Pair(22.dp, 12.dp)
                            AspectRatioChoice.PORTRAIT -> Pair(12.dp, 22.dp)
                            AspectRatioChoice.STANDARD_LANDSCAPE -> Pair(19.dp, 14.dp)
                            AspectRatioChoice.STANDARD_PORTRAIT -> Pair(14.dp, 19.dp)
                        }

                        Box(
                            modifier = Modifier
                                .size(width = boxW, height = boxH)
                                .border(
                                    1.dp,
                                    if (isSelected) StudioNeonCyan else StudioTextMuted,
                                    RoundedCornerShape(2.dp)
                                )
                                .background(
                                    if (isSelected) StudioNeonCyan.copy(alpha = 0.2f) else Color.Transparent
                                )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = ratio.label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) StudioTextPrimary else StudioTextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StylePresetSelector(
    selectedPreset: StylePreset,
    onSelectPreset: (StylePreset) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "STYLES CRÉATIFS",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            ),
            color = StudioTextSecondary,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(StylePresets.list) { preset ->
                val isSelected = preset.id == selectedPreset.id
                val icon = when (preset.iconName) {
                    "Movie" -> Icons.Default.Movie
                    "CameraAlt" -> Icons.Default.CameraAlt
                    "Bolt" -> Icons.Default.Bolt
                    "Brush" -> Icons.Default.Brush
                    "Category" -> Icons.Default.Category
                    "AutoAwesome" -> Icons.Default.AutoAwesome
                    else -> Icons.Default.Layers
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) StudioNeonViolet.copy(alpha = 0.3f) else StudioDarkSurface,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) StudioNeonVioletLight else StudioBorder
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onSelectPreset(preset) }
                        .testTag("preset_${preset.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = preset.label,
                            tint = if (isSelected) StudioNeonCyan else StudioTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = preset.label,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp
                            ),
                            color = if (isSelected) StudioTextPrimary else StudioTextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LiveRenderDialog(
    stepText: String,
    progressPercent: Int,
    engineName: String,
    onCancel: () -> Unit
) {
    val shimmerBrush = rememberShimmerBrush()
    val isVideo = engineName.contains("Video", ignoreCase = true) || engineName.contains("Runway", ignoreCase = true) || engineName.contains("Cog", ignoreCase = true)

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val scanlineY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scanlineY"
    )

    val currentStageIndex = when {
        progressPercent < 30 -> 0
        progressPercent < 65 -> 1
        progressPercent < 88 -> 2
        else -> 3
    }

    val stages = if (isVideo) {
        listOf("Initialisation", "Dynamique 3D", "Flow Matching", "Rendu 60 FPS")
    } else {
        listOf("Embeddings", "Diffusion DiT", "Décodage VAE", "Textures HD")
    }

    Dialog(onDismissRequest = { /* Modal during render */ }) {
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = StudioDarkSurfaceElevated),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, Brush.horizontalGradient(listOf(StudioNeonViolet, StudioNeonCyan)), RoundedCornerShape(22.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .scale(pulseScale)
                                .background(if (isVideo) StudioNeonPink else StudioNeonCyan, CircleShape)
                        )
                        Text(
                            text = if (isVideo) "Synthèse Vidéo IA" else "Génération Image IA",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = StudioTextPrimary
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = StudioNeonCyan.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioNeonCyan.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = "$progressPercent%",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = StudioNeonCyan
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Interactive Animated Skeleton Preview Canvas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(shimmerBrush)
                        .border(1.dp, StudioBorder, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // Neural Grid & Scanline Simulation
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Transparent,
                                        (if (isVideo) StudioNeonPink else StudioNeonCyan).copy(alpha = 0.15f),
                                        Color.Transparent
                                    ),
                                    startY = scanlineY * 200f,
                                    endY = (scanlineY * 200f) + 80f
                                )
                            )
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .scale(pulseScale)
                                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                .border(1.dp, if (isVideo) StudioNeonPink else StudioNeonCyan, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isVideo) Icons.Default.Videocam else Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = if (isVideo) StudioNeonPink else StudioNeonCyan,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.Black.copy(alpha = 0.7f)
                        ) {
                            Text(
                                text = engineName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = StudioTextPrimary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Multi-Stage Progress Stepper Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    stages.forEachIndexed { index, stageName ->
                        val isDone = index < currentStageIndex
                        val isCurrent = index == currentStageIndex
                        val stageColor = when {
                            isDone -> StudioEmeraldGlow
                            isCurrent -> StudioNeonCyan
                            else -> StudioDarkSurfaceVariant
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(stageColor)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stageName,
                                fontSize = 9.sp,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                color = if (isCurrent) StudioNeonCyan else if (isDone) StudioEmeraldGlow else StudioTextMuted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Progress Bar with Neon Glow
                LinearProgressIndicator(
                    progress = { progressPercent / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = if (isVideo) StudioNeonPink else StudioNeonCyan,
                    trackColor = StudioDarkSurfaceVariant,
                    strokeCap = StrokeCap.Round
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Live Step Details Text
                Text(
                    text = stepText,
                    style = MaterialTheme.typography.bodySmall,
                    color = StudioTextSecondary,
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Cancel Button
                OutlinedButton(
                    onClick = onCancel,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = StudioTextMuted),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(38.dp)
                        .testTag("cancel_generation_button")
                ) {
                    Text("Annuler", fontSize = 12.sp)
                }
            }
        }
    }
}
