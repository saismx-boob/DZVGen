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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.CreationEntity
import com.example.ui.components.CreationCard
import com.example.ui.components.SocialShareBottomSheet
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
import com.example.ui.viewmodel.HistoryFilter
import com.example.ui.viewmodel.HistoryViewModel

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onNavigateToDetail: (CreationEntity) -> Unit,
    onNavigateToStudio: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val creations by viewModel.creations.collectAsStateWithLifecycle()

    var shareItem by remember { mutableStateOf<CreationEntity?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDarkBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Galerie & Historique",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = StudioTextPrimary
                    )
                    Text(
                        text = "${creations.size} création(s) enregistrée(s)",
                        style = MaterialTheme.typography.bodySmall,
                        color = StudioTextSecondary
                    )
                }

                IconButton(
                    onClick = { viewModel.toggleViewMode() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(StudioDarkSurface, CircleShape)
                        .border(1.dp, StudioBorder, CircleShape)
                ) {
                    Icon(
                        imageVector = if (uiState.isGridView) Icons.Default.ViewList else Icons.Default.GridView,
                        contentDescription = "Basculer l'affichage",
                        tint = StudioNeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                placeholder = {
                    Text("Rechercher un prompt, style, tag...", color = StudioTextMuted, fontSize = 13.sp)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Rechercher",
                        tint = StudioTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Effacer",
                                tint = StudioTextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("history_search_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = StudioNeonViolet,
                    unfocusedBorderColor = StudioBorder,
                    focusedContainerColor = StudioDarkSurface,
                    unfocusedContainerColor = StudioDarkSurface,
                    focusedTextColor = StudioTextPrimary,
                    unfocusedTextColor = StudioTextPrimary
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Filter Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterTabItem(
                    title = "Tous",
                    isSelected = uiState.filter == HistoryFilter.ALL,
                    onClick = { viewModel.setFilter(HistoryFilter.ALL) }
                )
                FilterTabItem(
                    title = "🎨 Images",
                    isSelected = uiState.filter == HistoryFilter.IMAGES,
                    onClick = { viewModel.setFilter(HistoryFilter.IMAGES) }
                )
                FilterTabItem(
                    title = "🎬 Vidéos",
                    isSelected = uiState.filter == HistoryFilter.VIDEOS,
                    onClick = { viewModel.setFilter(HistoryFilter.VIDEOS) }
                )
                FilterTabItem(
                    title = "⭐ Favoris",
                    isSelected = uiState.filter == HistoryFilter.FAVORITES,
                    onClick = { viewModel.setFilter(HistoryFilter.FAVORITES) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Content List or Grid
            if (creations.isEmpty()) {
                EmptyCreationsView(
                    isSearch = uiState.searchQuery.isNotBlank(),
                    onNavigateToStudio = onNavigateToStudio,
                    onRestoreSamples = { viewModel.restoreSamples() }
                )
            } else if (uiState.isGridView) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(creations, key = { it.id }) { item ->
                        CreationCard(
                            creation = item,
                            onClick = { onNavigateToDetail(item) },
                            onToggleFavorite = { viewModel.toggleFavorite(item) },
                            onQuickShare = { shareItem = item }
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(creations, key = { it.id }) { item ->
                        CreationCard(
                            creation = item,
                            onClick = { onNavigateToDetail(item) },
                            onToggleFavorite = { viewModel.toggleFavorite(item) },
                            onQuickShare = { shareItem = item }
                        )
                    }
                }
            }
        }

        // Social Share Sheet if triggered
        shareItem?.let { creation ->
            SocialShareBottomSheet(
                creation = creation,
                onDismiss = { shareItem = null }
            )
        }
    }
}

@Composable
private fun FilterTabItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = StudioNeonViolet.copy(alpha = 0.35f),
            selectedLabelColor = StudioTextPrimary,
            containerColor = StudioDarkSurface,
            labelColor = StudioTextSecondary
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = if (isSelected) StudioNeonCyan else StudioBorder,
            borderWidth = 1.dp,
            enabled = true,
            selected = isSelected
        )
    )
}

@Composable
private fun EmptyCreationsView(
    isSearch: Boolean,
    onNavigateToStudio: () -> Unit,
    onRestoreSamples: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(StudioDarkSurfaceElevated, CircleShape)
                .border(1.dp, StudioBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isSearch) Icons.Default.Search else Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = StudioNeonVioletLight,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isSearch) "Aucun résultat trouvé" else "Votre galerie est vide",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = StudioTextPrimary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = if (isSearch) {
                "Essayez avec d'autres mots-clés ou modifiez vos filtres de recherche."
            } else {
                "Libérez votre créativité avec Stable Diffusion et Runway pour créer vos premières images et vidéos."
            },
            style = MaterialTheme.typography.bodySmall,
            color = StudioTextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (!isSearch) {
            Button(
                onClick = onNavigateToStudio,
                colors = ButtonDefaults.buttonColors(containerColor = StudioNeonViolet),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("empty_state_create_button")
            ) {
                Text("Créer maintenant", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.Transparent,
                modifier = Modifier.clickable { onRestoreSamples() }
            ) {
                Text(
                    text = "Charger les exemples de démonstration",
                    color = StudioNeonCyan,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(6.dp)
                )
            }
        }
    }
}
