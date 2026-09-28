package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.local.CreationEntity
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
import com.example.util.MediaDownloadManager
import com.example.util.SocialShareManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@Composable
fun CreationCard(
    creation: CreationEntity,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onQuickShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val isVideo = creation.type == "VIDEO"
    val ratio = when (creation.aspectRatio) {
        "16:9" -> 16f / 9f
        "9:16" -> 9f / 16f
        "4:3" -> 4f / 3f
        "3:4" -> 3f / 4f
        else -> 1f
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, StudioBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("creation_card_${creation.id}"),
        colors = CardDefaults.cardColors(containerColor = StudioDarkSurface)
    ) {
        Column {
            // Media Image container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(ratio.coerceIn(0.8f, 1.4f))
                    .background(StudioDarkBg)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(creation.mediaUrl.ifEmpty { creation.thumbnailUrl })
                        .crossfade(true)
                        .build(),
                    contentDescription = creation.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Top badges gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Black.copy(alpha = 0.75f), Color.Transparent)
                            )
                        )
                )

                // Type & Model Badge
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isVideo) StudioNeonPink.copy(alpha = 0.85f) else StudioNeonViolet.copy(alpha = 0.85f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            if (isVideo) {
                                Icon(
                                    imageVector = Icons.Default.Videocam,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "${creation.durationSeconds}s",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            } else {
                                Text(
                                    text = "IMAGE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // Favorite button
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            .clickable { onToggleFavorite() }
                            .testTag("fav_button_${creation.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (creation.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favori",
                            tint = if (creation.isFavorite) StudioNeonPink else Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // If Video, center subtle play icon
                if (isVideo) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(36.dp)
                            .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                            .border(1.dp, StudioNeonCyan.copy(alpha = 0.7f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Lire la vidéo",
                            tint = StudioNeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Info details
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = creation.title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = StudioTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = creation.prompt,
                    style = MaterialTheme.typography.bodySmall,
                    color = StudioTextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = StudioDarkSurfaceVariant
                    ) {
                        Text(
                            text = creation.modelEngine,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 10.sp,
                            color = StudioNeonVioletLight,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    MediaDownloadManager.downloadMediaToLocal(context, creation)
                                }
                            },
                            modifier = Modifier
                                .size(28.dp)
                                .testTag("quick_download_${creation.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Télécharger",
                                tint = StudioEmeraldGlow,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        IconButton(
                            onClick = onQuickShare,
                            modifier = Modifier
                                .size(28.dp)
                                .testTag("quick_share_${creation.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Partager",
                                tint = StudioTextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VideoPlayerSimView(
    creation: CreationEntity,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(true) }
    var currentProgress by remember { mutableFloatStateOf(0f) }
    val durationSec = if (creation.durationSeconds > 0) creation.durationSeconds else 5

    // Smooth timeline ticker
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(50)
            currentProgress += 0.05f / durationSec
            if (currentProgress >= 1f) {
                currentProgress = 0f
            }
        }
    }

    // Camera motion animation simulation effect
    val zoomFactor = when (creation.cameraMotion) {
        "Zoom Avant", "Zoom Avant & Travelling" -> 1f + (currentProgress * 0.12f)
        "Zoom Arrière" -> 1.12f - (currentProgress * 0.12f)
        else -> 1f + (currentProgress * 0.04f)
    }

    val panOffsetX = when (creation.cameraMotion) {
        "Travelling Droit" -> (currentProgress * 20f)
        "Orbite Circulaire" -> (kotlin.math.sin(currentProgress * 6.28) * 15f).toFloat()
        else -> 0f
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(StudioDarkBg)
            .border(1.dp, StudioBorder, RoundedCornerShape(16.dp))
    ) {
        // Animated Frame View
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(creation.mediaUrl)
                .crossfade(true)
                .build(),
            contentDescription = creation.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = zoomFactor
                    scaleY = zoomFactor
                    translationX = panOffsetX
                }
        )

        // Vignette gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent, Color.Black.copy(alpha = 0.85f))
                    )
                )
        )

        // Overlay status & camera badge
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = StudioNeonPink.copy(alpha = 0.8f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color.White, CircleShape)
                    )
                    Text(
                        text = "RUNWAY GEN-3 60FPS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = StudioDarkBg.copy(alpha = 0.7f),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
            ) {
                Text(
                    text = "Caméra : ${creation.cameraMotion}",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontSize = 11.sp,
                    color = StudioNeonCyan
                )
            }
        }

        // Bottom Controls
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(12.dp)
        ) {
            // Slider / Progress
            Slider(
                value = currentProgress,
                onValueChange = {
                    currentProgress = it
                },
                colors = SliderDefaults.colors(
                    thumbColor = StudioNeonCyan,
                    activeTrackColor = StudioNeonCyan,
                    inactiveTrackColor = StudioDarkSurfaceVariant
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { isPlaying = !isPlaying },
                        modifier = Modifier
                            .size(36.dp)
                            .background(StudioNeonViolet.copy(alpha = 0.4f), CircleShape)
                            .testTag("video_play_pause_button")
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Lecture",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    val currentSec = (currentProgress * durationSec).toInt()
                    Text(
                        text = "00:${String.format("%02d", currentSec)} / 00:${String.format("%02d", durationSec)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = StudioTextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = StudioDarkSurfaceElevated
                ) {
                    Text(
                        text = "Motion Score: ${creation.motionScore}/10",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 10.sp,
                        color = StudioAmberFlame,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialShareBottomSheet(
    creation: CreationEntity,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = StudioDarkSurfaceElevated,
        contentColor = StudioTextPrimary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Partager sur les réseaux",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = StudioTextPrimary
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = StudioNeonCyan.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = creation.type,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioNeonCyan
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Direct Social Action Buttons
            Text(
                text = "APPLICATIONS CIBLES",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = StudioTextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SocialIconTarget(
                    label = "Instagram",
                    bgColor = Color(0xFFE1306C),
                    onClick = {
                        SocialShareManager.shareToSocial(context, creation, SocialShareManager.SocialPlatform.INSTAGRAM)
                        onDismiss()
                    }
                )

                SocialIconTarget(
                    label = "TikTok",
                    bgColor = Color(0xFF000000),
                    onClick = {
                        SocialShareManager.shareToSocial(context, creation, SocialShareManager.SocialPlatform.TIKTOK)
                        onDismiss()
                    }
                )

                SocialIconTarget(
                    label = "WhatsApp",
                    bgColor = Color(0xFF25D366),
                    onClick = {
                        SocialShareManager.shareToSocial(context, creation, SocialShareManager.SocialPlatform.WHATSAPP)
                        onDismiss()
                    }
                )

                SocialIconTarget(
                    label = "X / Twitter",
                    bgColor = Color(0xFF1D9BF0),
                    onClick = {
                        SocialShareManager.shareToSocial(context, creation, SocialShareManager.SocialPlatform.X_TWITTER)
                        onDismiss()
                    }
                )

                SocialIconTarget(
                    label = "Général",
                    bgColor = StudioNeonViolet,
                    onClick = {
                        SocialShareManager.shareToSocial(context, creation, SocialShareManager.SocialPlatform.GENERIC)
                        onDismiss()
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quick Copy actions
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = StudioDarkSurfaceVariant),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                coroutineScope.launch {
                                    MediaDownloadManager.downloadMediaToLocal(context, creation)
                                    onDismiss()
                                }
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Télécharger en local",
                            tint = StudioEmeraldGlow
                        )
                        Column {
                            Text(
                                text = "Télécharger sur l'appareil (Galerie)",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = StudioTextPrimary
                            )
                            Text(
                                text = "Enregistre le média en haute résolution dans vos dossiers publics",
                                style = MaterialTheme.typography.bodySmall,
                                color = StudioTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                SocialShareManager.copyCaptionToClipboard(context, creation)
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copier la légende",
                            tint = StudioNeonCyan
                        )
                        Column {
                            Text(
                                text = "Copier la légende & les hashtags",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = StudioTextPrimary
                            )
                            Text(
                                text = "Formatée avec moteur IA, style et mots-clés",
                                style = MaterialTheme.typography.bodySmall,
                                color = StudioTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                SocialShareManager.copyPromptToClipboard(context, creation.prompt)
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copier le prompt",
                            tint = StudioNeonVioletLight
                        )
                        Column {
                            Text(
                                text = "Copier le prompt original",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = StudioTextPrimary
                            )
                            Text(
                                text = "Pour réutilisation ou itération",
                                style = MaterialTheme.typography.bodySmall,
                                color = StudioTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SocialIconTarget(
    label: String,
    bgColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .background(bgColor, CircleShape)
                .border(1.dp, StudioBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = StudioTextSecondary,
            fontWeight = FontWeight.Medium
        )
    }
}
