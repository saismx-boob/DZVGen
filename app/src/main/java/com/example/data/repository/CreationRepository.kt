package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.CreationDao
import com.example.data.local.CreationEntity
import com.example.data.remote.AiGenerationService
import com.example.data.remote.GenerationProgress
import com.example.model.AiEngine
import kotlinx.coroutines.flow.Flow

class CreationRepository(
    private val creationDao: CreationDao,
    private val aiService: AiGenerationService
) {
    val allCreations: Flow<List<CreationEntity>> = creationDao.getAllCreations()
    val imageCreations: Flow<List<CreationEntity>> = creationDao.getCreationsByType("IMAGE")
    val videoCreations: Flow<List<CreationEntity>> = creationDao.getCreationsByType("VIDEO")
    val favoriteCreations: Flow<List<CreationEntity>> = creationDao.getFavoriteCreations()

    fun getCreationById(id: Long): Flow<CreationEntity?> = creationDao.getCreationById(id)

    fun search(query: String): Flow<List<CreationEntity>> = creationDao.searchCreations(query)

    suspend fun saveCreation(creation: CreationEntity): Long {
        return creationDao.insertCreation(creation)
    }

    suspend fun toggleFavorite(id: Long, isFavorite: Boolean) {
        creationDao.updateFavorite(id, isFavorite)
    }

    suspend fun deleteCreation(id: Long) {
        creationDao.deleteById(id)
    }

    suspend fun clearHistory() {
        creationDao.clearAll()
    }

    suspend fun restoreSamples() {
        creationDao.insertAll(AppDatabase.getInitialSeedData())
    }

    fun generateCreative(
        prompt: String,
        negativePrompt: String,
        engine: AiEngine,
        stylePreset: String,
        aspectRatio: String,
        steps: Int,
        cfgScale: Float,
        seed: Long,
        durationSeconds: Int,
        cameraMotion: String,
        motionScore: Int,
        sourceImageUrl: String = "",
        inputMode: String = "TEXT_TO_IMAGE",
        imageStrength: Float = 0.75f
    ): Flow<GenerationProgress> {
        return aiService.generate(
            prompt = prompt,
            negativePrompt = negativePrompt,
            engine = engine,
            stylePreset = stylePreset,
            aspectRatio = aspectRatio,
            steps = steps,
            cfgScale = cfgScale,
            seed = seed,
            durationSeconds = durationSeconds,
            cameraMotion = cameraMotion,
            motionScore = motionScore,
            sourceImageUrl = sourceImageUrl,
            inputMode = inputMode,
            imageStrength = imageStrength
        )
    }
}
