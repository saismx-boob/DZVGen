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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PromptTag
import com.example.model.PromptTagCatalog
import com.example.model.TagCategory
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
fun AdvancedPromptBuilder(
    currentPrompt: String,
    onPromptChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(TagCategory.ALL) }

    val activeTags = remember(currentPrompt) {
        PromptTagCatalog.tags.filter { PromptTagCatalog.isTagInPrompt(currentPrompt, it) }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = StudioDarkSurface),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (activeTags.isNotEmpty()) StudioNeonCyan.copy(alpha = 0.6f) else StudioBorder
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row (Clickable to toggle expansion)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .testTag("toggle_advanced_prompt_builder"),
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
                            .background(StudioNeonViolet.copy(alpha = 0.25f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = StudioNeonCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Constructeur de Prompt Avancé",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = StudioTextPrimary
                            )

                            if (activeTags.isNotEmpty()) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = StudioNeonCyan.copy(alpha = 0.2f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioNeonCyan.copy(alpha = 0.6f))
                                ) {
                                    Text(
                                        text = "${activeTags.size} actif(s)",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = StudioNeonCyan,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Suggestions de styles, éclairages, 8K et angles caméra",
                            fontSize = 11.sp,
                            color = StudioTextSecondary
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Réduire" else "Dérouler",
                        tint = StudioTextSecondary
                    )
                }
            }

            // Quick Active Tags Horizontal Preview (always visible even when collapsed if tags are active)
            if (activeTags.isNotEmpty() && !isExpanded) {
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(activeTags) { tag ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = StudioDarkSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioNeonCyan.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = tag.labelFr,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = StudioTextPrimary
                                )
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Retirer",
                                    tint = StudioTextMuted,
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clickable {
                                            onPromptChanged(PromptTagCatalog.toggleTagInPrompt(currentPrompt, tag))
                                        }
                                )
                            }
                        }
                    }
                }
            }

            // Expanded Full Tag Construction Suite
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 14.dp)) {
                    // Quick Action Buttons Toolbar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Shuffle / Randomizer Combo
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = StudioNeonPink.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioNeonPink.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    val randomTags = PromptTagCatalog.tags.shuffled().take(3)
                                    var updated = currentPrompt
                                    randomTags.forEach { tag ->
                                        if (!PromptTagCatalog.isTagInPrompt(updated, tag)) {
                                            updated = PromptTagCatalog.toggleTagInPrompt(updated, tag)
                                        }
                                    }
                                    onPromptChanged(updated)
                                }
                                .testTag("btn_shuffle_prompt_tags")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shuffle,
                                    contentDescription = null,
                                    tint = StudioNeonPink,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Combo Aléatoire",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudioNeonPink
                                )
                            }
                        }

                        // Clear all active tags button
                        if (activeTags.isNotEmpty()) {
                            TextButton(
                                onClick = {
                                    var cleaned = currentPrompt
                                    activeTags.forEach { tag ->
                                        cleaned = PromptTagCatalog.toggleTagInPrompt(cleaned, tag)
                                    }
                                    onPromptChanged(cleaned)
                                },
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text(
                                    text = "Effacer les tags (${activeTags.size})",
                                    fontSize = 11.sp,
                                    color = StudioAmberFlame
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Category Tabs
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(TagCategory.values()) { category ->
                            val isCatSelected = category == selectedCategory
                            FilterChip(
                                selected = isCatSelected,
                                onClick = { selectedCategory = category },
                                label = {
                                    Text(
                                        text = category.label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isCatSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = StudioNeonViolet.copy(alpha = 0.35f),
                                    selectedLabelColor = StudioTextPrimary,
                                    containerColor = StudioDarkSurfaceVariant,
                                    labelColor = StudioTextSecondary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    borderColor = if (isCatSelected) StudioNeonCyan else StudioBorder,
                                    borderWidth = 1.dp,
                                    enabled = true,
                                    selected = isCatSelected
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Tags FlowRow
                    val filteredTags = remember(selectedCategory) {
                        if (selectedCategory == TagCategory.ALL) {
                            PromptTagCatalog.tags
                        } else {
                            PromptTagCatalog.tags.filter { it.category == selectedCategory }
                        }
                    }

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        filteredTags.forEach { tag ->
                            val isActive = PromptTagCatalog.isTagInPrompt(currentPrompt, tag)
                            PromptTagChip(
                                tag = tag,
                                isActive = isActive,
                                onClick = {
                                    onPromptChanged(PromptTagCatalog.toggleTagInPrompt(currentPrompt, tag))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PromptTagChip(
    tag: PromptTag,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val chipBorderColor = if (isActive) {
        when (tag.category) {
            TagCategory.STYLE -> StudioNeonVioletLight
            TagCategory.LIGHTING -> StudioAmberFlame
            TagCategory.QUALITY -> StudioEmeraldGlow
            TagCategory.CAMERA -> StudioNeonCyan
            TagCategory.MOTION -> StudioNeonPink
            else -> StudioNeonCyan
        }
    } else {
        StudioBorder
    }

    val chipBgColor = if (isActive) {
        when (tag.category) {
            TagCategory.STYLE -> StudioNeonViolet.copy(alpha = 0.35f)
            TagCategory.LIGHTING -> StudioAmberFlame.copy(alpha = 0.25f)
            TagCategory.QUALITY -> StudioEmeraldGlow.copy(alpha = 0.25f)
            TagCategory.CAMERA -> StudioNeonCyan.copy(alpha = 0.25f)
            TagCategory.MOTION -> StudioNeonPink.copy(alpha = 0.3f)
            else -> StudioNeonViolet.copy(alpha = 0.3f)
        }
    } else {
        StudioDarkBg
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = chipBgColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, chipBorderColor),
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .testTag("tag_chip_${tag.id}")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            if (isActive) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Actif",
                    tint = chipBorderColor,
                    modifier = Modifier.size(12.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Ajouter",
                    tint = StudioTextMuted,
                    modifier = Modifier.size(12.dp)
                )
            }

            Text(
                text = tag.labelFr,
                fontSize = 11.sp,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                color = if (isActive) StudioTextPrimary else StudioTextSecondary
            )
        }
    }
}
