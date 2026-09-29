package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.local.CreationEntity
import com.example.data.remote.LtxApiClient
import com.example.model.CameraMotions
import com.example.ui.components.LiveRenderDialog
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
import com.example.ui.viewmodel.LtxPlaygroundViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LtxPlaygroundScreen(
    viewModel: LtxPlaygroundViewModel,
    onNavigateToDetail: (CreationEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.onImageSelected(uri.toString())
        }
    }

    val motionPresets = listOf(
        "🎥 Dolly In dynamique" to "Cinematic camera dolly in with smooth depth of field",
        "🔄 Orbite 360°" to "Camera orbits 360 degrees around the subject with parallax depth",
        "🚁 Drone Push" to "Smooth forward drone aerial sweep with panoramic perspective",
        "🌊 Fluides & Eau" to "Hyper-realistic fluid water movement with light refraction",
        "💨 Vent Naturel" to "Gentle wind rustling hair and atmospheric particles floating",
        "⚡ Hyperlapse" to "Dynamic timelapse with fast-moving clouds and shifting cinematic shadows"
    )

    val ltxModels = listOf(
        "ltx-2-5-pro" to ("LTX-2.5 Pro" to "60 FPS Natif • Son Synchro • Qualité Maximale"),
        "ltx-2-5-fast" to ("LTX-2.5 Fast" to "60 FPS Turbo • Latence Minimale"),
        "ltx-2-3" to ("LTX-2.3 HD" to "Cinématique HD • Rendu Stable"),
        "ltx-2-0" to ("LTX-2.0" to "DiT Core v2.0 • Temps Réel")
    )

    val resolutionOptions = listOf(
        "1080p" to "1080p Full HD",
        "720p" to "720p HD Rapide",
        "1440p" to "1440p 2K QHD",
        "4k" to "4K Ultra HD"
    )

    val fpsOptions = listOf(24, 25, 48, 50, 60)

    val durationOptions = listOf(5, 10)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDarkBg)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Header with direct link to console.ltx.io
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioDarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioEmeraldGlow.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
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
                                        .size(32.dp)
                                        .background(StudioEmeraldGlow.copy(alpha = 0.2f), CircleShape)
                                        .border(1.dp, StudioEmeraldGlow, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Videocam,
                                        contentDescription = null,
                                        tint = StudioEmeraldGlow,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = "LTX Video Playground",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = StudioTextPrimary
                                    )
                                    Text(
                                        text = "console.ltx.io/playground/image-to-video",
                                        fontSize = 11.sp,
                                        color = StudioEmeraldGlow,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (uiState.apiKey.isNotBlank()) StudioEmeraldGlow.copy(alpha = 0.2f) else StudioAmberFlame.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = if (uiState.apiKey.isNotBlank()) "API CONNECTÉE" else "PRÊT (DÉMO/KEY)",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (uiState.apiKey.isNotBlank()) StudioEmeraldGlow else StudioAmberFlame
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Action Buttons Bar (cURL inspector, Open console in browser, API Key setup)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Open official console in browser
                            OutlinedButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(LtxApiClient.CONSOLE_PLAYGROUND_URL))
                                    context.startActivity(intent)
                                },
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = StudioTextPrimary),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(imageVector = Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Ouvrir LTX", fontSize = 11.sp)
                            }

                            // View exact cURL code
                            OutlinedButton(
                                onClick = { viewModel.toggleCurlDialog(true) },
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = StudioNeonCyan),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(imageVector = Icons.Default.Code, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Code cURL", fontSize = 11.sp)
                            }

                            // Manage API Key
                            OutlinedButton(
                                onClick = { viewModel.toggleApiKeyDialog(true) },
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = StudioNeonVioletLight),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(imageVector = Icons.Default.Key, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Clé API", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            // 2. First Frame Image Dropzone (Image-to-Video)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioDarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "1. IMAGE SOURCE (FIRST FRAME)",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = StudioEmeraldGlow
                            )

                            Text(
                                text = "image_uri",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = StudioTextMuted
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (!uiState.imageUri.isNullOrEmpty()) {
                            // Image is selected
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(StudioDarkBg)
                                    .border(1.dp, StudioEmeraldGlow, RoundedCornerShape(12.dp))
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(uiState.imageUri)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "Image source",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )

                                // Action buttons overlay
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color.Black.copy(alpha = 0.7f),
                                        modifier = Modifier.clickable {
                                            photoPickerLauncher.launch(
                                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                            )
                                        }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = StudioEmeraldGlow, modifier = Modifier.size(14.dp))
                                            Text("Remplacer", fontSize = 11.sp, color = StudioTextPrimary)
                                        }
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color.Black.copy(alpha = 0.7f),
                                        modifier = Modifier.clickable { viewModel.onImageSelected(null) }
                                    ) {
                                        Box(modifier = Modifier.padding(6.dp)) {
                                            Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = StudioAmberFlame, modifier = Modifier.size(14.dp))
                                        }
                                    }
                                }
                            }
                        } else {
                            // Dropzone to pick image
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = StudioDarkBg,
                                border = androidx.compose.foundation.BorderStroke(1.dp, StudioEmeraldGlow.copy(alpha = 0.4f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        photoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    }
                                    .testTag("ltx_playground_pick_image")
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .background(StudioEmeraldGlow.copy(alpha = 0.15f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AddPhotoAlternate,
                                            contentDescription = null,
                                            tint = StudioEmeraldGlow,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(
                                        text = "Importer l'image à animer",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = StudioTextPrimary
                                    )

                                    Text(
                                        text = "Prend en charge JPG, PNG, WEBP, HEIC",
                                        fontSize = 11.sp,
                                        color = StudioTextMuted
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 3. Motion Prompt Box
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioDarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "2. PROMPT DE MOUVEMENT (MOTION PROMPT)",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = StudioEmeraldGlow
                            )

                            Text(
                                text = "prompt",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = StudioTextMuted
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = uiState.prompt,
                            onValueChange = { viewModel.onPromptChange(it) },
                            placeholder = {
                                Text("Décrivez précisément les mouvements de caméra et du sujet...", color = StudioTextMuted, fontSize = 12.sp)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(95.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = StudioEmeraldGlow,
                                unfocusedBorderColor = StudioBorder,
                                focusedContainerColor = StudioDarkBg,
                                unfocusedContainerColor = StudioDarkBg,
                                focusedTextColor = StudioTextPrimary,
                                unfocusedTextColor = StudioTextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Suggestions rapides de dynamique LTX Studio :",
                            fontSize = 11.sp,
                            color = StudioTextSecondary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            motionPresets.forEach { (label, promptText) ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = StudioDarkSurfaceVariant,
                                    border = androidx.compose.foundation.BorderStroke(0.5.dp, StudioBorder),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { viewModel.applyMotionPreset(promptText) }
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        color = StudioTextPrimary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. Model Selector (ltx-2-5-pro, ltx-2-5-fast, ltx-2-3, ltx-2-0)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioDarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "3. MODÈLE LTX (MODEL)",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = StudioEmeraldGlow
                            )

                            Text(
                                text = "model",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = StudioTextMuted
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            ltxModels.forEach { (slug, info) ->
                                val isSelected = uiState.selectedModel == slug
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) StudioEmeraldGlow.copy(alpha = 0.15f) else StudioDarkBg,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) StudioEmeraldGlow else StudioBorder
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { viewModel.onModelChange(slug) }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Text(
                                                    text = info.first,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 13.sp,
                                                    color = StudioTextPrimary
                                                )
                                                Text(
                                                    text = "($slug)",
                                                    fontSize = 10.sp,
                                                    fontFamily = FontFamily.Monospace,
                                                    color = if (isSelected) StudioEmeraldGlow else StudioTextMuted
                                                )
                                            }
                                            Text(
                                                text = info.second,
                                                fontSize = 11.sp,
                                                color = StudioTextSecondary
                                            )
                                        }

                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = StudioEmeraldGlow,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 5. Playground Output Controls: Duration, FPS, Resolution, Camera Motion
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioDarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "4. PARAMÈTRES TECHNIQUES DE RENDU",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = StudioEmeraldGlow
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Duration (5s, 10s)
                        Text(text = "Durée du clip (duration) :", fontSize = 12.sp, color = StudioTextSecondary)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            durationOptions.forEach { dur ->
                                val isSelected = uiState.durationSeconds == dur
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) StudioEmeraldGlow.copy(alpha = 0.25f) else StudioDarkBg,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) StudioEmeraldGlow else StudioBorder),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { viewModel.onDurationChange(dur) }
                                ) {
                                    Box(modifier = Modifier.padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                                        Text(text = "$dur Secondes", fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = if (isSelected) StudioTextPrimary else StudioTextSecondary)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Frame Rate (FPS)
                        Text(text = "Fréquence d'images (fps) :", fontSize = 12.sp, color = StudioTextSecondary)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            fpsOptions.forEach { rate ->
                                val isSelected = uiState.fps == rate
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) StudioEmeraldGlow.copy(alpha = 0.25f) else StudioDarkBg,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) StudioEmeraldGlow else StudioBorder),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { viewModel.onFpsChange(rate) }
                                ) {
                                    Box(modifier = Modifier.padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                                        Text(text = "$rate FPS", fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = if (isSelected) StudioTextPrimary else StudioTextSecondary)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Resolution (720p, 1080p, 1440p, 4K)
                        Text(text = "Résolution (resolution) :", fontSize = 12.sp, color = StudioTextSecondary)
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(resolutionOptions) { (key, label) ->
                                val isSelected = uiState.resolution == key
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) StudioEmeraldGlow.copy(alpha = 0.25f) else StudioDarkBg,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) StudioEmeraldGlow else StudioBorder),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { viewModel.onResolutionChange(key) }
                                ) {
                                    Text(text = label, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = if (isSelected) StudioTextPrimary else StudioTextSecondary, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Aspect Ratio
                        Text(text = "Format d'affichage (aspect_ratio) :", fontSize = 12.sp, color = StudioTextSecondary)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("16:9" to "16:9 Paysage", "9:16" to "9:16 Portrait", "1:1" to "1:1 Carré").forEach { (ratio, label) ->
                                val isSelected = uiState.aspectRatio == ratio
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) StudioEmeraldGlow.copy(alpha = 0.25f) else StudioDarkBg,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) StudioEmeraldGlow else StudioBorder),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { viewModel.onAspectRatioChange(ratio) }
                                ) {
                                    Box(modifier = Modifier.padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                                        Text(text = label, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = if (isSelected) StudioTextPrimary else StudioTextSecondary)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Camera Motion
                        Text(text = "Trajectoire caméra (camera_motion) :", fontSize = 12.sp, color = StudioTextSecondary)
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(CameraMotions.list) { motion ->
                                val isSelected = uiState.cameraMotion == motion.name
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) StudioEmeraldGlow.copy(alpha = 0.25f) else StudioDarkBg,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) StudioEmeraldGlow else StudioBorder),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { viewModel.onCameraMotionChange(motion.name) }
                                ) {
                                    Text(text = motion.name, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = if (isSelected) StudioTextPrimary else StudioTextSecondary, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                                }
                            }
                        }
                    }
                }
            }

            // 6. Action Button: GENERATE LTX VIDEO
            item {
                Button(
                    onClick = { viewModel.generateLtxVideo() },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = StudioEmeraldGlow),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("ltx_generate_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "GÉNÉRER SUR LTX STUDIO (60 FPS)",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            // 7. Last Generated Result Preview
            uiState.lastGeneratedCreation?.let { creation ->
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToDetail(creation) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = StudioDarkSurface),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, StudioEmeraldGlow)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(StudioEmeraldGlow, CircleShape)
                                    )
                                    Text(
                                        text = "VIDÉO LTX RENDUE AVEC SUCCÈS",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = StudioEmeraldGlow
                                    )
                                }

                                Text(
                                    text = "Voir détails ➔",
                                    fontSize = 11.sp,
                                    color = StudioNeonCyan
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 9f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(StudioDarkBg)
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(creation.thumbnailUrl.ifEmpty { creation.mediaUrl })
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = creation.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )

                                Surface(
                                    shape = CircleShape,
                                    color = Color.Black.copy(alpha = 0.6f),
                                    modifier = Modifier.align(Alignment.Center)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Lecture",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .padding(12.dp)
                                            .size(32.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = creation.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = StudioTextPrimary
                            )
                            Text(
                                text = "${creation.modelEngine} • ${creation.durationSeconds}s • ${creation.fps} FPS • ${creation.cameraMotion}",
                                fontSize = 11.sp,
                                color = StudioTextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Live Render Progress Dialog
        if (uiState.isGenerating) {
            LiveRenderDialog(
                stepText = uiState.currentStepText,
                progressPercent = uiState.progressPercent,
                engineName = "LTX Video (${uiState.selectedModel.uppercase()})",
                onCancel = { viewModel.cancelGeneration() }
            )
        }

        // Error message popup
        uiState.errorMessage?.let { error ->
            Dialog(onDismissRequest = { viewModel.clearError() }) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioDarkSurfaceElevated),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioAmberFlame)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Notification LTX",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = StudioAmberFlame
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = error,
                            fontSize = 12.sp,
                            color = StudioTextPrimary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.clearError() },
                            colors = ButtonDefaults.buttonColors(containerColor = StudioAmberFlame)
                        ) {
                            Text("Compris", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Exact cURL Inspector Dialog
        if (uiState.showCurlDialog) {
            val curlSnippet = LtxApiClient(context).generateCurlSnippet(
                imageUriDisplay = uiState.imageUri ?: "https://example.com/source_image.jpg",
                prompt = uiState.prompt,
                negativePrompt = uiState.negativePrompt,
                model = uiState.selectedModel,
                duration = uiState.durationSeconds,
                fps = uiState.fps,
                resolution = uiState.resolution,
                cameraMotion = uiState.cameraMotion,
                seed = uiState.seed,
                apiKey = uiState.apiKey
            )

            Dialog(onDismissRequest = { viewModel.toggleCurlDialog(false) }) {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioDarkSurfaceElevated),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioNeonCyan),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Requête cURL LTX API",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = StudioNeonCyan
                            )
                            IconButton(onClick = { viewModel.toggleCurlDialog(false) }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Fermer", tint = StudioTextMuted)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Commande exacte pour https://api.ltx.io/v2/image-to-video :",
                            fontSize = 11.sp,
                            color = StudioTextSecondary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(StudioDarkBg)
                                .padding(10.dp)
                        ) {
                            Text(
                                text = curlSnippet,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = StudioTextPrimary,
                                lineHeight = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("LTX cURL", curlSnippet)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "cURL copié dans le presse-papier !", Toast.LENGTH_SHORT).show()
                                viewModel.toggleCurlDialog(false)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = StudioNeonCyan),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Copier la commande cURL", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // API Key Quick Setup Dialog
        if (uiState.showApiKeyDialog) {
            var inputKey by remember { mutableStateOf(uiState.apiKey) }

            Dialog(onDismissRequest = { viewModel.toggleApiKeyDialog(false) }) {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioDarkSurfaceElevated),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioEmeraldGlow),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Clé API LTX (console.ltx.io)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = StudioEmeraldGlow
                            )
                            IconButton(onClick = { viewModel.toggleApiKeyDialog(false) }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Fermer", tint = StudioTextMuted)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Obtenez votre clé sur console.ltx.io/keys pour exécuter vos générations directement sur le cloud LTX.",
                            fontSize = 11.sp,
                            color = StudioTextSecondary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = inputKey,
                            onValueChange = { inputKey = it },
                            placeholder = { Text("ltx_api_key_...", color = StudioTextMuted, fontSize = 12.sp) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = StudioEmeraldGlow,
                                unfocusedBorderColor = StudioBorder,
                                focusedContainerColor = StudioDarkBg,
                                unfocusedContainerColor = StudioDarkBg,
                                focusedTextColor = StudioTextPrimary,
                                unfocusedTextColor = StudioTextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(LtxApiClient.CONSOLE_KEYS_URL))
                                    context.startActivity(intent)
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Obtenir clé", fontSize = 11.sp)
                            }

                            Button(
                                onClick = { viewModel.saveApiKey(inputKey) },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = StudioEmeraldGlow),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Enregistrer", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
