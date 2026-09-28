package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "creations")
data class CreationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String, // "IMAGE" or "VIDEO"
    val title: String,
    val prompt: String,
    val negativePrompt: String = "",
    val modelEngine: String, // e.g. "Stable Diffusion XL", "Runway Gen-3 Alpha"
    val stylePreset: String = "",
    val aspectRatio: String = "1:1",
    val durationSeconds: Int = 0, // 0 for images, 5 or 10 for videos
    val mediaUrl: String, // Local URI or drawable identifier or web URL
    val thumbnailUrl: String = "",
    val seed: Long = 0L,
    val cfgScale: Float = 7.5f,
    val steps: Int = 30,
    val motionScore: Int = 5, // 1 to 10 for Runway videos
    val cameraMotion: String = "Cinematic Zoom",
    val createdAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val tags: String = "AI,Art"
)
