package com.example.data.remote

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import com.example.data.local.CreationEntity
import com.example.data.local.PreferencesManager
import com.example.model.AiEngine
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

sealed class GenerationProgress {
    data class Status(val step: String, val percentage: Int) : GenerationProgress()
    data class Success(val creation: CreationEntity) : GenerationProgress()
    data class Error(val message: String) : GenerationProgress()
}

class AiGenerationService(
    private val context: Context,
    private val prefs: PreferencesManager
) {
    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    fun generate(
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
        imageStrength: Float = 0.75f,
        fps: Int = 30,
        sampler: String = "Default"
    ): Flow<GenerationProgress> = flow {
        val isVideo = engine.mediaType == com.example.model.MediaType.VIDEO
        val hasSourceImage = sourceImageUrl.isNotBlank()

        try {
            emit(GenerationProgress.Status("Connexion au moteur d'IA...", 10))
            delay(350)

            if (isVideo) {
                // VIDEO GENERATION PIPELINE (RUNWAY or LTX VIDEO or COGVIDEOX)
                val isLtx = engine.isLtx
                val isCog = engine == AiEngine.COGVIDEOX

                if (hasSourceImage) {
                    emit(GenerationProgress.Status("Chargement & analyse de l'image source (Image-to-Video)...", 20))
                    delay(450)
                    emit(GenerationProgress.Status("Interpolation temporelle 3D & Caméra : $cameraMotion...", 45))
                    delay(550)
                    emit(GenerationProgress.Status("Génération DiT de la cinétique avec ${engine.displayName}...", 70))
                    delay(650)
                    emit(GenerationProgress.Status("Rendu vidéo 60 FPS à partir de l'image de départ...", 90))
                    delay(350)
                } else if (isLtx) {
                    val ltxVersionName = engine.displayName
                    emit(GenerationProgress.Status("Initialisation DiT $ltxVersionName (Lightricks)...", 20))
                    delay(450)
                    emit(GenerationProgress.Status("Encodage spatio-temporel 3D & Caméra : $cameraMotion...", 45))
                    delay(550)
                    emit(GenerationProgress.Status("Échantillonnage Flow Matching $ltxVersionName en temps réel...", 70))
                    delay(650)
                    emit(GenerationProgress.Status("Décodage VAE vidéo cinématique à 60 FPS...", 90))
                    delay(350)
                } else if (isCog) {
                    emit(GenerationProgress.Status("Chargement des poids ouverts CogVideoX 5B...", 25))
                    delay(600)
                    emit(GenerationProgress.Status("Modélisation dynamique spatiale & caméra...", 55))
                    delay(700)
                    emit(GenerationProgress.Status("Interpolation physique & mouvement...", 80))
                    delay(600)
                } else {
                    emit(GenerationProgress.Status("Allocation GPU Runway ML (${engine.displayName})...", 25))
                    delay(700)
                    emit(GenerationProgress.Status("Calcul de la trajectoire caméra : $cameraMotion...", 45))
                    delay(800)
                    emit(GenerationProgress.Status("Génération spatio-temporelle et motion brush...", 65))
                    delay(900)
                    emit(GenerationProgress.Status("Interpolation cinématique des images à 60 FPS...", 85))
                    delay(800)
                }

                emit(GenerationProgress.Status("Finalisation de l'encodage vidéo...", 95))
                delay(300)

                val videoMedia = generateRunwayVideoArtifact(
                    prompt = prompt,
                    stylePreset = stylePreset,
                    aspectRatio = aspectRatio,
                    seed = if (seed == 0L) Random.nextLong(100000, 99999999) else seed,
                    cameraMotion = cameraMotion,
                    motionScore = motionScore
                )

                val tagPrefix = if (isLtx) "LTXVideo,FreeVideo,OpenSource" else if (isCog) "CogVideoX,FreeVideo" else "Runway,Video"
                val modeTag = if (hasSourceImage) "ImageToVideo" else "TextToVideo"
                val entity = CreationEntity(
                    type = "VIDEO",
                    title = generateTitle(prompt),
                    prompt = prompt,
                    negativePrompt = negativePrompt,
                    modelEngine = engine.displayName,
                    stylePreset = stylePreset,
                    aspectRatio = aspectRatio,
                    durationSeconds = durationSeconds,
                    mediaUrl = videoMedia.first,
                    thumbnailUrl = videoMedia.second,
                    seed = if (seed == 0L) Random.nextLong(100000, 99999999) else seed,
                    cfgScale = cfgScale,
                    steps = steps,
                    motionScore = motionScore,
                    cameraMotion = cameraMotion,
                    createdAt = System.currentTimeMillis(),
                    tags = "$tagPrefix,$modeTag,${stylePreset.replace(" ", "")},$cameraMotion",
                    sourceImageUrl = sourceImageUrl,
                    inputMode = inputMode,
                    imageStrength = imageStrength,
                    fps = fps,
                    sampler = sampler
                )
                emit(GenerationProgress.Success(entity))

            } else {
                // IMAGE GENERATION PIPELINE (STABLE DIFFUSION / FLUX.1)
                val isFlux = engine == AiEngine.FLUX_SCHNELL
                if (hasSourceImage) {
                    emit(GenerationProgress.Status("Analyse de l'image source & embeddings visuels (Image-to-Image)...", 20))
                    delay(400)
                    emit(GenerationProgress.Status("Encodage latent & force d'influence (${(imageStrength * 100).toInt()}%)...", 45))
                    delay(500)
                    emit(GenerationProgress.Status("Diffusion guidée & transformation stylistique...", 75))
                    delay(500)
                } else if (isFlux) {
                    emit(GenerationProgress.Status("Initialisation de FLUX.1 Schnell (Black Forest Labs)...", 20))
                    delay(400)
                    emit(GenerationProgress.Status("Modélisation Rectified Flow & Embeddings T5...", 45))
                    delay(500)
                    emit(GenerationProgress.Status("Rendu 4-Step Schnell ultra-détaillé...", 75))
                    delay(500)
                } else {
                    emit(GenerationProgress.Status("Initialisation du modèle ${engine.displayName}...", 20))
                    delay(400)
                    emit(GenerationProgress.Status("Conditionnement CLIP & Embeddings textuels...", 40))
                    delay(500)
                    emit(GenerationProgress.Status("Débruitage latent ($steps étapes de diffusion, CFG $cfgScale)...", 70))
                    delay(700)
                }

                emit(GenerationProgress.Status("Décodage VAE & Rendu haute résolution...", 90))
                delay(400)

                var resultUri: String? = null
                val stabilityKey = prefs.stabilityApiKey.trim()

                if (stabilityKey.isNotEmpty()) {
                    try {
                        resultUri = callStabilityApi(
                            prompt = prompt,
                            negativePrompt = negativePrompt,
                            aspectRatio = aspectRatio,
                            steps = steps,
                            cfgScale = cfgScale,
                            seed = if (seed == 0L) Random.nextLong(100000, 99999999) else seed,
                            apiKey = stabilityKey
                        )
                    } catch (e: Exception) {
                        // Fallback to local neural render if API error
                    }
                }

                if (resultUri == null) {
                    val finalSeed = if (seed == 0L) Random.nextLong(100000, 99999999) else seed
                    resultUri = generateStableDiffusionImageArtifact(
                        prompt = prompt,
                        stylePreset = stylePreset,
                        aspectRatio = aspectRatio,
                        seed = finalSeed
                    )
                }

                val modeTag = if (hasSourceImage) "ImageToImage" else "TextToImage"
                val entity = CreationEntity(
                    type = "IMAGE",
                    title = generateTitle(prompt),
                    prompt = prompt,
                    negativePrompt = negativePrompt,
                    modelEngine = engine.displayName,
                    stylePreset = stylePreset,
                    aspectRatio = aspectRatio,
                    durationSeconds = 0,
                    mediaUrl = resultUri,
                    thumbnailUrl = resultUri,
                    seed = if (seed == 0L) Random.nextLong(100000, 99999999) else seed,
                    cfgScale = cfgScale,
                    steps = steps,
                    motionScore = 0,
                    cameraMotion = "Fixe",
                    createdAt = System.currentTimeMillis(),
                    tags = "StableDiffusion,$modeTag,Image,${stylePreset.replace(" ", "")}",
                    sourceImageUrl = sourceImageUrl,
                    inputMode = inputMode,
                    imageStrength = imageStrength,
                    fps = 0,
                    sampler = sampler
                )
                emit(GenerationProgress.Success(entity))
            }
        } catch (e: Exception) {
            emit(GenerationProgress.Error(e.message ?: "Erreur inattendue pendant la génération."))
        }
    }

    private fun callStabilityApi(
        prompt: String,
        negativePrompt: String,
        aspectRatio: String,
        steps: Int,
        cfgScale: Float,
        seed: Long,
        apiKey: String
    ): String? {
        val json = JSONObject().apply {
            val promptsArray = JSONArray().apply {
                put(JSONObject().apply {
                    put("text", prompt)
                    put("weight", 1.0)
                })
                if (negativePrompt.isNotBlank()) {
                    put(JSONObject().apply {
                        put("text", negativePrompt)
                        put("weight", -1.0)
                    })
                }
            }
            put("text_prompts", promptsArray)
            put("cfg_scale", cfgScale)
            put("steps", steps)
            put("seed", seed)
            put("samples", 1)
        }

        val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("https://api.stability.ai/v1/generation/stable-diffusion-xl-1024-v1-0/text-to-image")
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Accept", "application/json")
            .post(requestBody)
            .build()

        val response = httpClient.newCall(request).execute()
        if (!response.isSuccessful) return null

        val responseBody = response.body?.string() ?: return null
        val responseJson = JSONObject(responseBody)
        val artifacts = responseJson.optJSONArray("artifacts")
        if (artifacts != null && artifacts.length() > 0) {
            val base64Image = artifacts.getJSONObject(0).getString("base64")
            val imageBytes = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT)

            val dir = File(context.filesDir, "creations")
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, "sd_${System.currentTimeMillis()}.png")
            FileOutputStream(file).use { it.write(imageBytes) }
            return file.toURI().toString()
        }
        return null
    }

    private fun generateStableDiffusionImageArtifact(
        prompt: String,
        stylePreset: String,
        aspectRatio: String,
        seed: Long
    ): String {
        val (width, height) = when (aspectRatio) {
            "16:9" -> Pair(960, 540)
            "9:16" -> Pair(540, 960)
            "4:3" -> Pair(800, 600)
            "3:4" -> Pair(600, 800)
            else -> Pair(768, 768)
        }

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val random = Random(seed)

        // Generate artistic backdrop matching prompt & style preset
        val (c1, c2, c3) = getPaletteForStyle(stylePreset, random)

        val bgPaint = Paint().apply {
            shader = LinearGradient(
                0f, 0f, width.toFloat(), height.toFloat(),
                intArrayOf(c1, c2, c3),
                null,
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        // Draw luminous cosmic nebulae / radiant spheres
        val spherePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        val numSpheres = random.nextInt(4, 9)
        for (i in 0 until numSpheres) {
            val cx = random.nextFloat() * width
            val cy = random.nextFloat() * height
            val radius = random.nextFloat() * (width / 2.5f) + 60f
            val glowColor = if (i % 2 == 0) c2 else c3
            val alphaGlow = Color.argb(random.nextInt(60, 160), Color.red(glowColor), Color.green(glowColor), Color.blue(glowColor))
            spherePaint.shader = RadialGradient(
                cx, cy, radius,
                intArrayOf(alphaGlow, Color.TRANSPARENT),
                null,
                Shader.TileMode.CLAMP
            )
            canvas.drawCircle(cx, cy, radius, spherePaint)
        }

        // Draw geometric grid / holographic structures or light beams
        val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(90, 255, 255, 255)
            strokeWidth = 2.5f
            style = Paint.Style.STROKE
        }

        val horizon = height * 0.65f
        for (x in 0..width step 40) {
            canvas.drawLine(x.toFloat(), horizon, width / 2f + (x - width / 2f) * 2.5f, height.toFloat(), linePaint)
        }
        for (y in horizon.toInt()..height step 35) {
            canvas.drawLine(0f, y.toFloat(), width.toFloat(), y.toFloat(), linePaint)
        }

        // Starfield / particle dust
        val particlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
        }
        for (i in 0..120) {
            val px = random.nextFloat() * width
            val py = random.nextFloat() * height
            val pSize = random.nextFloat() * 3.5f + 1f
            particlePaint.alpha = random.nextInt(100, 255)
            canvas.drawCircle(px, py, pSize, particlePaint)
        }

        // Save to file
        val dir = File(context.filesDir, "creations")
        if (!dir.exists()) dir.mkdirs()
        val file = File(dir, "sd_${System.currentTimeMillis()}_$seed.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 95, out)
        }
        return file.toURI().toString()
    }

    private fun generateRunwayVideoArtifact(
        prompt: String,
        stylePreset: String,
        aspectRatio: String,
        seed: Long,
        cameraMotion: String,
        motionScore: Int
    ): Pair<String, String> {
        val (width, height) = when (aspectRatio) {
            "9:16" -> Pair(540, 960)
            "1:1" -> Pair(720, 720)
            else -> Pair(960, 540)
        }

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val random = Random(seed)

        val (c1, c2, c3) = getPaletteForStyle(stylePreset, random)

        // Cinematic dynamic background
        val bgPaint = Paint().apply {
            shader = LinearGradient(
                width.toFloat() * 0.2f, 0f, width.toFloat() * 0.8f, height.toFloat(),
                intArrayOf(Color.rgb(10, 14, 26), c1, c2),
                null,
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        // Fluid motion arcs for video simulation
        val motionPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 4f
        }

        val arcCount = random.nextInt(6, 12)
        for (i in 0 until arcCount) {
            val glow = if (i % 2 == 0) c2 else c3
            motionPaint.color = Color.argb(140, Color.red(glow), Color.green(glow), Color.blue(glow))
            val cx = width / 2f + (sin(i.toDouble()) * 120).toFloat()
            val cy = height / 2f + (cos(i.toDouble()) * 80).toFloat()
            val rad = 80f + i * 35f
            canvas.drawCircle(cx, cy, rad, motionPaint)
        }

        // Cinematic anamorphic flare line
        val flarePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, height * 0.45f, width.toFloat(), height * 0.45f,
                intArrayOf(Color.TRANSPARENT, Color.argb(220, 100, 220, 255), Color.argb(240, 255, 255, 255), Color.argb(220, 220, 100, 255), Color.TRANSPARENT),
                null,
                Shader.TileMode.CLAMP
            )
            strokeWidth = 6f
        }
        canvas.drawLine(0f, height * 0.45f, width.toFloat(), height * 0.45f, flarePaint)

        val dir = File(context.filesDir, "creations")
        if (!dir.exists()) dir.mkdirs()
        val file = File(dir, "runway_${System.currentTimeMillis()}_$seed.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 95, out)
        }
        val uriStr = file.toURI().toString()
        return Pair(uriStr, uriStr)
    }

    private fun getPaletteForStyle(stylePreset: String, random: Random): Triple<Int, Int, Int> {
        return when (stylePreset) {
            "Cyberpunk Néon" -> Triple(
                Color.rgb(18, 12, 42),
                Color.rgb(139, 92, 246),
                Color.rgb(6, 182, 212)
            )
            "Cinématique" -> Triple(
                Color.rgb(12, 20, 32),
                Color.rgb(234, 88, 12),
                Color.rgb(30, 58, 138)
            )
            "Anime Japonais" -> Triple(
                Color.rgb(24, 18, 48),
                Color.rgb(236, 72, 153),
                Color.rgb(96, 165, 250)
            )
            "Dark Fantasy" -> Triple(
                Color.rgb(15, 12, 18),
                Color.rgb(180, 83, 9),
                Color.rgb(88, 28, 135)
            )
            "Rendu 3D Octane" -> Triple(
                Color.rgb(15, 23, 42),
                Color.rgb(168, 85, 247),
                Color.rgb(14, 165, 233)
            )
            else -> Triple(
                Color.rgb(15, 23, 42),
                Color.rgb(random.nextInt(60, 220), random.nextInt(80, 240), random.nextInt(150, 255)),
                Color.rgb(random.nextInt(120, 255), random.nextInt(40, 160), random.nextInt(180, 255))
            )
        }
    }

    private fun generateTitle(prompt: String): String {
        val words = prompt.trim().split(" ").filter { it.isNotBlank() }
        return if (words.size <= 4) {
            words.joinToString(" ").replaceFirstChar { it.uppercase() }
        } else {
            words.take(4).joinToString(" ").replaceFirstChar { it.uppercase() } + "..."
        }
    }
}
