package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [CreationEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun creationDao(): CreationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vision_ai_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            getInstance(context).creationDao().insertAll(getInitialSeedData())
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }

        fun getInitialSeedData(): List<CreationEntity> {
            val now = System.currentTimeMillis()
            return listOf(
                CreationEntity(
                    id = 1,
                    type = "IMAGE",
                    title = "Cyberpunk Muse 2077",
                    prompt = "Futuristic neon cyberpunk female portrait with glowing digital circuits, holographic reflections, volumetric cinematic purple and cyan lighting, intricate 8k octane render",
                    negativePrompt = "blurry, low quality, artifacts, watermark, cartoonish, low resolution",
                    modelEngine = "Stable Diffusion XL",
                    stylePreset = "Cyberpunk Néon",
                    aspectRatio = "1:1",
                    durationSeconds = 0,
                    mediaUrl = "android.resource://com.example/drawable/sd_showcase_art_1790628237988",
                    thumbnailUrl = "android.resource://com.example/drawable/sd_showcase_art_1790628237988",
                    seed = 492817492L,
                    cfgScale = 7.5f,
                    steps = 35,
                    motionScore = 0,
                    cameraMotion = "Fixe",
                    createdAt = now - 3600000 * 2,
                    isFavorite = true,
                    tags = "Cyberpunk,Portrait,Neon,SDXL"
                ),
                CreationEntity(
                    id = 2,
                    type = "VIDEO",
                    title = "Alien Moonscape Flight",
                    prompt = "Cinematic drone shot flying through a glowing crystal canyon on an alien planet with two moons in a twilight sky, ethereal bioluminescence, smooth 60fps cinematic motion blur aesthetic",
                    negativePrompt = "jitter, stutter, low framerate, sudden cuts, glitch",
                    modelEngine = "Runway Gen-3 Alpha",
                    stylePreset = "Cinématique",
                    aspectRatio = "16:9",
                    durationSeconds = 10,
                    mediaUrl = "android.resource://com.example/drawable/runway_showcase_1790628251106",
                    thumbnailUrl = "android.resource://com.example/drawable/runway_showcase_1790628251106",
                    seed = 88291034L,
                    cfgScale = 8.0f,
                    steps = 50,
                    motionScore = 8,
                    cameraMotion = "Zoom Avant & Travelling",
                    createdAt = now - 3600000 * 5,
                    isFavorite = true,
                    tags = "SciFi,Runway,Drone,Cinematic,Video"
                ),
                CreationEntity(
                    id = 3,
                    type = "IMAGE",
                    title = "Vision Studio Crest",
                    prompt = "Minimalist neon glowing app icon symbol combining camera aperture, spark stars, fluid cinematic ribbon, dark background, vivid violet & cyan",
                    negativePrompt = "noise, blurry, low resolution",
                    modelEngine = "SDXL Turbo (Ultra-Rapide)",
                    stylePreset = "Rendu 3D Octane",
                    aspectRatio = "1:1",
                    durationSeconds = 0,
                    mediaUrl = "android.resource://com.example/drawable/ic_vision_logo_1790628187559",
                    thumbnailUrl = "android.resource://com.example/drawable/ic_vision_logo_1790628187559",
                    seed = 133742L,
                    cfgScale = 6.0f,
                    steps = 20,
                    motionScore = 0,
                    cameraMotion = "Fixe",
                    createdAt = now - 3600000 * 12,
                    isFavorite = false,
                    tags = "Logo,Minimal,Creative,SD"
                ),
                CreationEntity(
                    id = 4,
                    type = "VIDEO",
                    title = "LTX Aurora Flight 60FPS",
                    prompt = "Hyper-fluid camera drone flythrough above a neon glowing futuristic metropolis during an emerald aurora borealis, ultra-smooth motion coherence, 60fps cinematic video",
                    negativePrompt = "blurry, low framerate, jitter, artifacts",
                    modelEngine = "LTX Video (Lightricks)",
                    stylePreset = "Cinématique",
                    aspectRatio = "16:9",
                    durationSeconds = 5,
                    mediaUrl = "android.resource://com.example/drawable/runway_showcase_1790628251106",
                    thumbnailUrl = "android.resource://com.example/drawable/runway_showcase_1790628251106",
                    seed = 77123910L,
                    cfgScale = 3.5f,
                    steps = 25,
                    motionScore = 9,
                    cameraMotion = "Travelling Droit",
                    createdAt = now - 3600000 * 1,
                    isFavorite = true,
                    tags = "LTXVideo,Free,OpenSource,Lightricks,Drone,Video"
                ),
                CreationEntity(
                    id = 5,
                    type = "IMAGE",
                    title = "FLUX Dreamscape Oasis",
                    prompt = "Surreal levitating water sphere over a futuristic desert oasis at sunset, crystalline reflections, dramatic volumetric lighting, ultra-detailed 4k render",
                    negativePrompt = "blurry, distorted",
                    modelEngine = "FLUX.1 Schnell",
                    stylePreset = "Rendu 3D Octane",
                    aspectRatio = "1:1",
                    durationSeconds = 0,
                    mediaUrl = "android.resource://com.example/drawable/sd_showcase_art_1790628237988",
                    thumbnailUrl = "android.resource://com.example/drawable/sd_showcase_art_1790628237988",
                    seed = 99812401L,
                    cfgScale = 4.0f,
                    steps = 4,
                    motionScore = 0,
                    cameraMotion = "Fixe",
                    createdAt = now - 3600000 * 3,
                    isFavorite = false,
                    tags = "FLUX,Free,OpenSource,Art,Image"
                )
            )
        }
    }
}
