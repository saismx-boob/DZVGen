package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioDarkSurface
import com.example.ui.theme.StudioDarkSurfaceElevated
import com.example.ui.theme.StudioDarkSurfaceVariant
import com.example.ui.theme.StudioNeonCyan
import com.example.ui.theme.StudioNeonViolet

@Composable
fun rememberShimmerBrush(): Brush {
    val shimmerColors = listOf(
        StudioDarkSurfaceVariant.copy(alpha = 0.6f),
        StudioNeonViolet.copy(alpha = 0.25f),
        StudioNeonCyan.copy(alpha = 0.35f),
        StudioDarkSurfaceVariant.copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition(label = "shimmerTransition")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1300, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslate"
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 400f, translateAnim - 400f),
        end = Offset(translateAnim, translateAnim)
    )
}

@Composable
fun SkeletonBox(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 8.dp,
    brush: Brush = rememberShimmerBrush()
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(brush)
    )
}

@Composable
fun SkeletonCreationCard(
    modifier: Modifier = Modifier,
    aspectRatio: Float = 1f
) {
    val shimmer = rememberShimmerBrush()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, StudioBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = StudioDarkSurface)
    ) {
        Column {
            // Main media placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio)
                    .background(shimmer)
            )

            // Info rows
            Column(modifier = Modifier.padding(12.dp)) {
                SkeletonBox(
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .height(16.dp),
                    brush = shimmer
                )

                Spacer(modifier = Modifier.height(8.dp))

                SkeletonBox(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(12.dp),
                    brush = shimmer
                )

                Spacer(modifier = Modifier.height(4.dp))

                SkeletonBox(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(12.dp),
                    brush = shimmer
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SkeletonBox(
                        modifier = Modifier
                            .width(80.dp)
                            .height(20.dp),
                        cornerRadius = 6.dp,
                        brush = shimmer
                    )

                    SkeletonBox(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape),
                        brush = shimmer
                    )
                }
            }
        }
    }
}
