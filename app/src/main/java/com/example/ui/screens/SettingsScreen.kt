package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.AiEngine
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
import com.example.ui.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.savedMessage) {
        uiState.savedMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.dismissMessage()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDarkBg)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(StudioDarkSurface, CircleShape)
                            .border(1.dp, StudioBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = StudioNeonVioletLight,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Paramètres & Moteurs IA",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = StudioTextPrimary
                        )
                        Text(
                            text = "Modèles gratuits LTX & FLUX, et passerelles cloud",
                            style = MaterialTheme.typography.bodySmall,
                            color = StudioTextSecondary
                        )
                    }
                }
            }

            // Free AI Models Showcase Card (LTX Video, Flux, SD Turbo)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioDarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioEmeraldGlow.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
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
                                    text = "MODÈLES GRATUITS & OPEN-SOURCE",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                                    color = StudioEmeraldGlow
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = StudioEmeraldGlow.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "ILLIMITÉ & SANS CLÉ",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudioEmeraldGlow
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Ces modèles sont utilisables directement sans aucune clé API ni abonnement :",
                            style = MaterialTheme.typography.bodySmall,
                            color = StudioTextSecondary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Model 1: LTX Video
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = StudioDarkSurfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "🎬 LTX Video 2.5 Turbo & 2.3 HD (Lightricks)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = StudioTextPrimary
                                    )
                                    Text(
                                        text = "Dernières versions 60 FPS",
                                        fontSize = 10.sp,
                                        color = StudioNeonCyan,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Text(
                                    text = "Modèles vidéo de dernière génération (v2.5 Turbo & v2.3 HD) avec physique fluide, haute résolution et motion tracking en temps réel.",
                                    fontSize = 11.sp,
                                    color = StudioTextMuted
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Model 2: FLUX.1 Schnell
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = StudioDarkSurfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "🎨 FLUX.1 Schnell (Black Forest Labs)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = StudioTextPrimary
                                    )
                                    Text(
                                        text = "Image 4-Step",
                                        fontSize = 10.sp,
                                        color = StudioNeonVioletLight,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Text(
                                    text = "Génération d'images photoréalistes ultra-détaillées de dernière génération.",
                                    fontSize = 11.sp,
                                    color = StudioTextMuted
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Model 3: SDXL Turbo & CogVideoX
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = StudioDarkSurfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "⚡ SDXL Turbo & CogVideoX",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = StudioTextPrimary
                                )
                                Text(
                                    text = "Rendu ultra-rapide 1-étape et modèle vidéo open-source.",
                                    fontSize = 11.sp,
                                    color = StudioTextMuted
                                )
                            }
                        }
                    }
                }
            }

            // API Keys Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
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
                                text = "CLÉS D'API D'ACCÈS CLOUD",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = StudioTextSecondary
                            )

                            val hasKeys = uiState.stabilityApiKey.isNotEmpty() || uiState.runwayApiKey.isNotEmpty()
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (hasKeys) StudioEmeraldGlow.copy(alpha = 0.2f) else StudioAmberFlame.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = if (hasKeys) "API Connectée" else "Mode Synthèse Locale Rapide",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (hasKeys) StudioEmeraldGlow else StudioAmberFlame
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // LTX Video Key Input (console.ltx.io)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Clé API LTX Video (console.ltx.io) :",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = StudioEmeraldGlow
                            )
                            if (uiState.ltxApiKey.isNotBlank()) {
                                Text(
                                    text = "✓ Configurée",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudioEmeraldGlow
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = uiState.ltxApiKey,
                            onValueChange = { viewModel.onLtxKeyChange(it) },
                            placeholder = {
                                Text("Clé Bearer LTX...", color = StudioTextMuted, fontSize = 12.sp)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Key,
                                    contentDescription = null,
                                    tint = StudioEmeraldGlow,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .testTag("ltx_api_key_input"),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = StudioEmeraldGlow,
                                unfocusedBorderColor = StudioBorder,
                                focusedContainerColor = StudioDarkBg,
                                unfocusedContainerColor = StudioDarkBg,
                                focusedTextColor = StudioTextPrimary,
                                unfocusedTextColor = StudioTextPrimary
                            )
                        )
                        Text(
                            text = "Accès direct à https://api.ltx.io/v2/image-to-video (LTX-2.5 Pro, LTX-2.5 Fast, LTX-2.3).",
                            fontSize = 11.sp,
                            color = StudioTextMuted,
                            modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Stability AI Key Input
                        Text(
                            text = "Clé API Stability AI (Stable Diffusion) :",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = StudioTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = uiState.stabilityApiKey,
                            onValueChange = { viewModel.onStabilityKeyChange(it) },
                            placeholder = {
                                Text("sk-...", color = StudioTextMuted, fontSize = 12.sp)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Key,
                                    contentDescription = null,
                                    tint = StudioNeonVioletLight,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .testTag("stability_api_key_input"),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = StudioNeonViolet,
                                unfocusedBorderColor = StudioBorder,
                                focusedContainerColor = StudioDarkBg,
                                unfocusedContainerColor = StudioDarkBg,
                                focusedTextColor = StudioTextPrimary,
                                unfocusedTextColor = StudioTextPrimary
                            )
                        )
                        Text(
                            text = "Optionnel : laissez vide pour utiliser le moteur de rendu neural ultra-rapide intégré.",
                            fontSize = 11.sp,
                            color = StudioTextMuted,
                            modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Runway Key Input
                        Text(
                            text = "Clé API Runway ML (Gen-3 & Gen-2 Video) :",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = StudioTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = uiState.runwayApiKey,
                            onValueChange = { viewModel.onRunwayKeyChange(it) },
                            placeholder = {
                                Text("runway_sec_...", color = StudioTextMuted, fontSize = 12.sp)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Videocam,
                                    contentDescription = null,
                                    tint = StudioNeonPink,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .testTag("runway_api_key_input"),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = StudioNeonPink,
                                unfocusedBorderColor = StudioBorder,
                                focusedContainerColor = StudioDarkBg,
                                unfocusedContainerColor = StudioDarkBg,
                                focusedTextColor = StudioTextPrimary,
                                unfocusedTextColor = StudioTextPrimary
                            )
                        )
                        Text(
                            text = "Permet de générer des clips cinématiques haute définition avec motion brush.",
                            fontSize = 11.sp,
                            color = StudioTextMuted,
                            modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.saveAll() },
                            colors = ButtonDefaults.buttonColors(containerColor = StudioNeonViolet),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("save_settings_button")
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                            Text("Enregistrer les configurations", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Engine & Performance Preferences
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioDarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "PRÉFÉRENCES DU STUDIO",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = StudioTextSecondary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Default Engine Selector
                        var dropdownExpanded by remember { mutableStateOf(false) }
                        Text(
                            text = "Moteur par défaut :",
                            style = MaterialTheme.typography.bodySmall,
                            color = StudioTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        ExposedDropdownMenuBox(
                            expanded = dropdownExpanded,
                            onExpandedChange = { dropdownExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = uiState.defaultEngine.displayName,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = StudioNeonCyan,
                                    unfocusedBorderColor = StudioBorder,
                                    focusedContainerColor = StudioDarkBg,
                                    unfocusedContainerColor = StudioDarkBg,
                                    focusedTextColor = StudioTextPrimary,
                                    unfocusedTextColor = StudioTextPrimary
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = dropdownExpanded,
                                onDismissRequest = { dropdownExpanded = false },
                                modifier = Modifier.background(StudioDarkSurfaceElevated)
                            ) {
                                AiEngine.values().forEach { engine ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = "${engine.displayName} (${engine.provider})",
                                                color = StudioTextPrimary,
                                                fontSize = 12.sp
                                            )
                                        },
                                        onClick = {
                                            viewModel.onDefaultEngineChange(engine)
                                            dropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Auto-Save Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Sauvegarder dans l'historique",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = StudioTextPrimary
                                )
                                Text(
                                    text = "Enregistre automatiquement chaque création dans la base Room locale",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = StudioTextMuted,
                                    fontSize = 11.sp
                                )
                            }
                            Switch(
                                checked = uiState.autoSave,
                                onCheckedChange = { viewModel.onAutoSaveChange(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = StudioNeonCyan,
                                    checkedTrackColor = StudioNeonViolet.copy(alpha = 0.5f)
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Auto-Enhance Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Optimisation automatique des prompts",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = StudioTextPrimary
                                )
                                Text(
                                    text = "Injecte des descriptifs d'éclairage et de rendu cinématique",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = StudioTextMuted,
                                    fontSize = 11.sp
                                )
                            }
                            Switch(
                                checked = uiState.autoEnhancePrompt,
                                onCheckedChange = { viewModel.onAutoEnhanceChange(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = StudioNeonCyan,
                                    checkedTrackColor = StudioNeonViolet.copy(alpha = 0.5f)
                                )
                            )
                        }
                    }
                }
            }

            // Data Management
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioDarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "GESTION DES DONNÉES LOCALES",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = StudioTextSecondary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = { viewModel.restoreShowcaseSamples() },
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioNeonCyan),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = StudioNeonCyan),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("restore_samples_button")
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Restaurer les créations de démonstration")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedButton(
                            onClick = { viewModel.clearAllData() },
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioNeonPink.copy(alpha = 0.6f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = StudioNeonPink),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("clear_history_button")
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Vider l'historique complet")
                        }
                    }
                }
            }

            // About VisionAI Studio
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioDarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "À PROPOS DE VISIONAI STUDIO",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = StudioTextSecondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "VisionAI Studio combine la puissance de Stable Diffusion (SDXL, SD 3.5, SD Turbo) de Stability AI et la génération vidéo de Runway ML (Gen-3 Alpha, Gen-2).",
                            style = MaterialTheme.typography.bodySmall,
                            color = StudioTextSecondary,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Version 1.0.0 • Architecture Jetpack Compose, Room Database & Android M3",
                            fontSize = 11.sp,
                            color = StudioTextMuted
                        )
                    }
                }
            }
        }
    }
}
