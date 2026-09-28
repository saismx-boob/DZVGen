package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.local.CreationEntity
import com.example.ui.components.SocialShareBottomSheet
import com.example.ui.components.VideoPlayerSimView
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DetailScreen(
    creation: CreationEntity,
    onBack: () -> Unit,
    onRemix: (CreationEntity) -> Unit,
    onToggleFavorite: (CreationEntity) -> Unit,
    onDelete: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    BackHandler { onBack() }

    var showShareSheet by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var isDownloading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val isVideo = creation.type == "VIDEO"
    val scrollState = rememberScrollState()

    val dateStr = remember(creation.createdAt) {
        val sdf = SimpleDateFormat("dd MMMM yyyy à HH:mm", Locale.FRENCH)
        sdf.format(Date(creation.createdAt))
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDarkBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 32.dp)
        ) {
            // Top Navigation Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .background(StudioDarkSurface, CircleShape)
                        .border(1.dp, StudioBorder, CircleShape)
                        .testTag("detail_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Retour",
                        tint = StudioTextPrimary
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Favorite Toggle
                    IconButton(
                        onClick = { onToggleFavorite(creation) },
                        modifier = Modifier
                            .size(40.dp)
                            .background(StudioDarkSurface, CircleShape)
                            .border(1.dp, StudioBorder, CircleShape)
                            .testTag("detail_fav_button")
                    ) {
                        Icon(
                            imageVector = if (creation.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favori",
                            tint = if (creation.isFavorite) StudioNeonPink else StudioTextSecondary
                        )
                    }

                    // Download Button in Top Bar
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                isDownloading = true
                                MediaDownloadManager.downloadMediaToLocal(context, creation)
                                isDownloading = false
                            }
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .background(StudioEmeraldGlow.copy(alpha = 0.2f), CircleShape)
                            .border(1.dp, StudioEmeraldGlow.copy(alpha = 0.6f), CircleShape)
                            .testTag("detail_download_button")
                    ) {
                        if (isDownloading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = StudioEmeraldGlow,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Télécharger en local",
                                tint = StudioEmeraldGlow
                            )
                        }
                    }

                    // Share Button
                    IconButton(
                        onClick = { showShareSheet = true },
                        modifier = Modifier
                            .size(40.dp)
                            .background(StudioNeonViolet.copy(alpha = 0.3f), CircleShape)
                            .border(1.dp, StudioNeonViolet, CircleShape)
                            .testTag("detail_share_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Partager",
                            tint = StudioNeonCyan
                        )
                    }

                    // Delete Button
                    IconButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier
                            .size(40.dp)
                            .background(StudioDarkSurface, CircleShape)
                            .border(1.dp, StudioBorder, CircleShape)
                            .testTag("detail_delete_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Supprimer",
                            tint = StudioTextMuted
                        )
                    }
                }
            }

            // Media Player / Viewer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                if (isVideo) {
                    VideoPlayerSimView(
                        creation = creation,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                    )
                } else {
                    val ratio = when (creation.aspectRatio) {
                        "16:9" -> 16f / 9f
                        "9:16" -> 9f / 16f
                        "4:3" -> 4f / 3f
                        "3:4" -> 3f / 4f
                        else -> 1f
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(ratio)
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, StudioBorder, RoundedCornerShape(16.dp))
                            .background(StudioDarkSurface)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(creation.mediaUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = creation.title,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Action Buttons: Share & Remix
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Share to Social Button
                Button(
                    onClick = { showShareSheet = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("action_share_social_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = StudioNeonViolet)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Partager Réseaux",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Remix in Studio Button
                OutlinedButton(
                    onClick = { onRemix(creation) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("action_remix_button"),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioNeonCyan),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = StudioNeonCyan)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoFixHigh,
                        contentDescription = null,
                        tint = StudioNeonCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Remixer (Studio)",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Primary Download Button
            Button(
                onClick = {
                    coroutineScope.launch {
                        isDownloading = true
                        MediaDownloadManager.downloadMediaToLocal(context, creation)
                        isDownloading = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(48.dp)
                    .testTag("action_download_local_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StudioEmeraldGlow)
            ) {
                if (isDownloading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.Black,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Téléchargement en cours...",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isVideo) "Télécharger la Vidéo (Galerie/Films)" else "Télécharger l'Image (Galerie/Photos)",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Information & Prompt Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = StudioDarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = creation.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = StudioTextPrimary
                        )

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isVideo) StudioNeonPink.copy(alpha = 0.25f) else StudioNeonViolet.copy(alpha = 0.25f)
                        ) {
                            Text(
                                text = creation.modelEngine,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isVideo) StudioNeonPink else StudioNeonVioletLight
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Prompt section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PROMPT UTILISÉ",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = StudioTextSecondary
                        )

                        IconButton(
                            onClick = { SocialShareManager.copyPromptToClipboard(context, creation.prompt) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copier le prompt",
                                tint = StudioNeonCyan,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = StudioDarkBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = creation.prompt,
                            style = MaterialTheme.typography.bodySmall,
                            color = StudioTextPrimary,
                            modifier = Modifier.padding(12.dp),
                            lineHeight = 18.sp
                        )
                    }

                    if (creation.negativePrompt.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "PROMPT NÉGATIF",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = StudioTextMuted
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = StudioDarkBg,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = creation.negativePrompt,
                                style = MaterialTheme.typography.bodySmall,
                                color = StudioTextMuted,
                                modifier = Modifier.padding(10.dp),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Technical Specs Grid
                    Text(
                        text = "PARAMÈTRES TECHNIQUES DE GÉNÉRATION",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = StudioTextSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            TechParamItem(
                                label = "Format & Ratio",
                                value = creation.aspectRatio,
                                modifier = Modifier.weight(1f)
                            )
                            TechParamItem(
                                label = "Style Artistique",
                                value = creation.stylePreset.ifEmpty { "Standard" },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(modifier = Modifier.fillMaxWidth()) {
                            if (isVideo) {
                                TechParamItem(
                                    label = "Durée Vidéo",
                                    value = "${creation.durationSeconds} secondes (60 FPS)",
                                    modifier = Modifier.weight(1f)
                                )
                                TechParamItem(
                                    label = "Mouvement Caméra",
                                    value = creation.cameraMotion,
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                TechParamItem(
                                    label = "Diffusion Steps",
                                    value = "${creation.steps} itérations",
                                    modifier = Modifier.weight(1f)
                                )
                                TechParamItem(
                                    label = "Échelle CFG",
                                    value = "${creation.cfgScale}",
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        Row(modifier = Modifier.fillMaxWidth()) {
                            TechParamItem(
                                label = "Graine (Seed)",
                                value = "#${creation.seed}",
                                modifier = Modifier.weight(1f)
                            )
                            TechParamItem(
                                label = "Date de création",
                                value = dateStr,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        // Social Share Sheet
        if (showShareSheet) {
            SocialShareBottomSheet(
                creation = creation,
                onDismiss = { showShareSheet = false }
            )
        }

        // Delete Confirmation Dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                containerColor = StudioDarkSurfaceElevated,
                title = {
                    Text("Supprimer la création ?", color = StudioTextPrimary)
                },
                text = {
                    Text(
                        "Cette action est irréversible et supprimera le média de votre historique local.",
                        color = StudioTextSecondary
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDeleteDialog = false
                            onDelete(creation.id)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StudioNeonPink)
                    ) {
                        Text("Supprimer")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Annuler", color = StudioTextSecondary)
                    }
                }
            )
        }
    }
}

@Composable
private fun TechParamItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = StudioDarkSurfaceVariant,
        modifier = modifier.padding(2.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
            Text(
                text = label,
                fontSize = 10.sp,
                color = StudioTextMuted
            )
            Text(
                text = value,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = StudioTextPrimary
            )
        }
    }
}
