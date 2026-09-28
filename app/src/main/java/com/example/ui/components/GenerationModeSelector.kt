package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.model.GenerationInputMode
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
fun GenerationModeSelector(
    selectedMode: GenerationInputMode,
    onSelectMode: (GenerationInputMode) -> Unit,
    sourceImageUri: String?,
    onSourceImageSelected: (String?) -> Unit,
    imageStrength: Float,
    onImageStrengthChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Zero-permission Android Photo Picker compliant with Google Play Policy
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            onSourceImageSelected(uri.toString())
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = StudioDarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header
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
                            .size(28.dp)
                            .background(StudioNeonCyan.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (selectedMode.isVideo) Icons.Default.Videocam else Icons.Default.Image,
                            contentDescription = null,
                            tint = StudioNeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Text(
                        text = "MODE DE GÉNÉRATION",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = StudioTextSecondary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (selectedMode.isImageInput) StudioAmberFlame.copy(alpha = 0.2f) else StudioNeonViolet.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = if (selectedMode.isImageInput) "SOURCE IMAGE" else "PROMPT TEXTUEL",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedMode.isImageInput) StudioAmberFlame else StudioNeonVioletLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4 Mode Buttons in 2x2 Grid or horizontal scroll
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                GenerationInputMode.values().forEach { mode ->
                    val isSelected = mode == selectedMode
                    val activeColor = when (mode) {
                        GenerationInputMode.TEXT_TO_IMAGE -> StudioNeonCyan
                        GenerationInputMode.IMAGE_TO_IMAGE -> StudioNeonVioletLight
                        GenerationInputMode.TEXT_TO_VIDEO -> StudioNeonPink
                        GenerationInputMode.IMAGE_TO_VIDEO -> StudioEmeraldGlow
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) activeColor.copy(alpha = 0.2f) else StudioDarkBg,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) activeColor else StudioBorder
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onSelectMode(mode) }
                            .testTag("mode_${mode.name.lowercase()}")
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = when (mode) {
                                    GenerationInputMode.TEXT_TO_IMAGE -> Icons.Default.Image
                                    GenerationInputMode.IMAGE_TO_IMAGE -> Icons.Default.Tune
                                    GenerationInputMode.TEXT_TO_VIDEO -> Icons.Default.Videocam
                                    GenerationInputMode.IMAGE_TO_VIDEO -> Icons.Default.AutoAwesome
                                },
                                contentDescription = mode.label,
                                tint = if (isSelected) activeColor else StudioTextMuted,
                                modifier = Modifier.size(16.dp)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = mode.shortLabel,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) StudioTextPrimary else StudioTextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Short Description of currently selected mode
            Text(
                text = selectedMode.description,
                fontSize = 11.sp,
                color = StudioTextMuted,
                lineHeight = 15.sp
            )

            // Animated Source Image Picker Drawer (visible for I2I and I2V)
            AnimatedVisibility(
                visible = selectedMode.isImageInput,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 14.dp)) {
                    Text(
                        text = if (selectedMode == GenerationInputMode.IMAGE_TO_VIDEO) "IMAGE SOURCE À ANIMER EN VIDÉO" else "IMAGE SOURCE À TRANSFORMER",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = StudioNeonCyan
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (!sourceImageUri.isNullOrEmpty()) {
                        // Image already selected - show thumbnail preview card
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = StudioDarkSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioNeonCyan),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(StudioDarkBg)
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(sourceImageUri)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "Image source",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.matchParentSize()
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Photo source chargée",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = StudioTextPrimary
                                    )
                                    Text(
                                        text = if (selectedMode.isVideo) "Prête pour animation vidéo 60 FPS" else "Prête pour variation & restylage",
                                        fontSize = 11.sp,
                                        color = StudioEmeraldGlow
                                    )
                                }

                                Row {
                                    IconButton(
                                        onClick = {
                                            photoPickerLauncher.launch(
                                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                            )
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Refresh,
                                            contentDescription = "Remplacer",
                                            tint = StudioNeonCyan,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    IconButton(
                                        onClick = { onSourceImageSelected(null) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Supprimer",
                                            tint = StudioAmberFlame,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        // Drop zone to pick image
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = StudioDarkBg,
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioNeonCyan.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                                .testTag("pick_source_image_button")
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(StudioNeonCyan.copy(alpha = 0.15f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AddPhotoAlternate,
                                        contentDescription = "Ajouter une image",
                                        tint = StudioNeonCyan,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = if (selectedMode == GenerationInputMode.IMAGE_TO_VIDEO) "Choisir une photo à animer en vidéo" else "Choisir une photo source à restyler",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = StudioTextPrimary
                                )

                                Text(
                                    text = "Ouvre la Galerie Android sécurisée (zéro permission requise)",
                                    fontSize = 11.sp,
                                    color = StudioTextMuted
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Influence / Denoising Strength Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Influence de l'image source :",
                            fontSize = 12.sp,
                            color = StudioTextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${(imageStrength * 100).toInt()}% (${if (imageStrength < 0.4f) "Créativité forte" else if (imageStrength < 0.75f) "Équilibré" else "Fidélité stricte"})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioNeonCyan
                        )
                    }

                    Slider(
                        value = imageStrength,
                        onValueChange = onImageStrengthChange,
                        valueRange = 0.15f..0.95f,
                        steps = 15,
                        colors = SliderDefaults.colors(
                            thumbColor = StudioNeonCyan,
                            activeTrackColor = StudioNeonCyan,
                            inactiveTrackColor = StudioDarkSurfaceVariant
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("slider_image_strength")
                    )
                }
            }
        }
    }
}
