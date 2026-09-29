package com.example.data.remote

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import com.example.data.local.CreationEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.concurrent.TimeUnit

data class LtxJobResult(
    val jobId: String,
    val videoUrl: String,
    val thumbnailUrl: String,
    val status: String,
    val durationSeconds: Int,
    val fps: Int,
    val resolution: String,
    val model: String
)

class LtxApiClient(
    private val context: Context,
    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()
) {
    companion object {
        const val BASE_URL = "https://api.ltx.io/v2"
        const val ENDPOINT_IMAGE_TO_VIDEO = "https://api.ltx.io/v2/image-to-video"
        const val ENDPOINT_TEXT_TO_VIDEO = "https://api.ltx.io/v2/text-to-video"
        const val CONSOLE_PLAYGROUND_URL = "https://console.ltx.io/playground/image-to-video"
        const val CONSOLE_KEYS_URL = "https://console.ltx.io/keys"
    }

    /**
     * Converts an image file URI (content:// or file://) to a Base64 Data URI
     * for direct transmission to the LTX API.
     */
    fun convertImageToDataUri(imageUriString: String): String? {
        return try {
            val uri = Uri.parse(imageUriString)
            val inputStream: InputStream? = if (imageUriString.startsWith("content://")) {
                context.contentResolver.openInputStream(uri)
            } else if (imageUriString.startsWith("file://")) {
                File(uri.path ?: "").inputStream()
            } else {
                File(imageUriString).inputStream()
            }

            inputStream?.use { stream ->
                val bitmap = BitmapFactory.decodeStream(stream) ?: return null
                // Resize if bitmap is exceedingly large to ensure fast and reliable network transmission (< 4MB)
                val maxDim = 1920
                val scaledBitmap = if (bitmap.width > maxDim || bitmap.height > maxDim) {
                    val ratio = bitmap.width.toFloat() / bitmap.height.toFloat()
                    val targetW = if (ratio >= 1f) maxDim else (maxDim * ratio).toInt()
                    val targetH = if (ratio >= 1f) (maxDim / ratio).toInt() else maxDim
                    Bitmap.createScaledBitmap(bitmap, targetW, targetH, true)
                } else {
                    bitmap
                }

                val baos = ByteArrayOutputStream()
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
                val base64 = Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP)
                "data:image/jpeg;base64,$base64"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Generates the exact cURL command matching what console.ltx.io displays in its Code/cURL inspector.
     */
    fun generateCurlSnippet(
        imageUriDisplay: String,
        prompt: String,
        negativePrompt: String,
        model: String,
        duration: Int,
        fps: Int,
        resolution: String,
        cameraMotion: String,
        seed: Long,
        apiKey: String
    ): String {
        val safeKey = if (apiKey.isNotBlank()) apiKey else "\$LTX_API_KEY"
        val payload = JSONObject().apply {
            put("image_uri", if (imageUriDisplay.startsWith("data:")) "<base64_data_uri_of_image>" else imageUriDisplay)
            put("prompt", prompt)
            if (negativePrompt.isNotBlank()) put("negative_prompt", negativePrompt)
            put("model", model)
            put("duration", duration)
            put("fps", fps)
            put("resolution", resolution)
            if (cameraMotion.isNotBlank()) put("camera_motion", cameraMotion)
            if (seed > 0) put("seed", seed)
        }.toString(2)

        return """
curl -X POST https://api.ltx.io/v2/image-to-video \
  -H "Authorization: Bearer $safeKey" \
  -H "Content-Type: application/json" \
  -d '$payload'
        """.trimIndent()
    }

    /**
     * Executes the Image-to-Video generation using the official LTX API.
     * Submits the job to https://api.ltx.io/v2/image-to-video, polls status,
     * and downloads the final video to local storage.
     */
    fun executeImageToVideo(
        imageUriString: String,
        prompt: String,
        negativePrompt: String,
        model: String,
        duration: Int,
        fps: Int,
        resolution: String,
        cameraMotion: String,
        seed: Long,
        apiKey: String
    ): Flow<GenerationProgress> = flow {
        if (apiKey.isBlank()) {
            emit(GenerationProgress.Error("Clé API LTX manquante. Rendez-vous sur https://console.ltx.io pour obtenir votre clé."))
            return@flow
        }

        emit(GenerationProgress.Status("Préparation de l'image pour l'API LTX...", 15))
        val dataUri = convertImageToDataUri(imageUriString)
        if (dataUri == null && !imageUriString.startsWith("http")) {
            emit(GenerationProgress.Error("Impossible de lire ou encoder l'image source."))
            return@flow
        }

        val requestPayload = JSONObject().apply {
            put("image_uri", dataUri ?: imageUriString)
            put("prompt", prompt)
            if (negativePrompt.isNotBlank()) put("negative_prompt", negativePrompt)
            put("model", model)
            put("duration", duration)
            put("fps", fps)
            put("resolution", resolution)
            if (cameraMotion.isNotBlank()) put("camera_motion", cameraMotion)
            if (seed > 0) put("seed", seed)
        }

        emit(GenerationProgress.Status("Envoi de la requête à https://api.ltx.io/v2/image-to-video...", 30))

        val body = requestPayload.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val postRequest = Request.Builder()
            .url(ENDPOINT_IMAGE_TO_VIDEO)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build()

        val postResponse = try {
            httpClient.newCall(postRequest).execute()
        } catch (e: Exception) {
            emit(GenerationProgress.Error("Erreur de connexion à api.ltx.io: ${e.localizedMessage}"))
            return@flow
        }

        val postResponseBody = postResponse.body?.string() ?: ""
        if (!postResponse.isSuccessful) {
            val errorMsg = try {
                val json = JSONObject(postResponseBody)
                json.optString("message", json.optString("error", "Code HTTP ${postResponse.code}"))
            } catch (_: Exception) {
                "Code HTTP ${postResponse.code}: $postResponseBody"
            }
            emit(GenerationProgress.Error("Échec LTX API: $errorMsg"))
            return@flow
        }

        val responseJson = JSONObject(postResponseBody)
        val jobId = responseJson.optString("id", responseJson.optString("job_id", ""))
        var videoUrl = responseJson.optJSONObject("output")?.optString("video_url")
            ?: responseJson.optString("video_url")

        if (videoUrl.isNotBlank()) {
            emit(GenerationProgress.Status("Vidéo générée immédiatement par LTX !", 85))
        } else if (jobId.isNotBlank()) {
            // Asynchronous job pattern: poll status
            emit(GenerationProgress.Status("Tâche LTX créée (#$jobId), traitement en cours...", 40))

            var attempts = 0
            val maxAttempts = 60 // 60 * 2s = 120s max
            var isFinished = false

            while (attempts < maxAttempts && !isFinished) {
                delay(2500)
                attempts++
                val progressPercent = (40 + (attempts * 45 / maxAttempts)).coerceAtMost(88)
                emit(GenerationProgress.Status("Génération de l'animation DiT 60 FPS ($attempts/60)...", progressPercent))

                val pollUrl = "$BASE_URL/jobs/$jobId"
                val pollRequest = Request.Builder()
                    .url(pollUrl)
                    .addHeader("Authorization", "Bearer $apiKey")
                    .get()
                    .build()

                try {
                    val pollResponse = httpClient.newCall(pollRequest).execute()
                    if (pollResponse.isSuccessful) {
                        val pollBody = pollResponse.body?.string() ?: ""
                        val pollJson = JSONObject(pollBody)
                        val status = pollJson.optString("status", "").lowercase()

                        if (status == "succeeded" || status == "completed") {
                            videoUrl = pollJson.optJSONObject("output")?.optString("video_url")
                                ?: pollJson.optString("video_url")
                            isFinished = true
                        } else if (status == "failed" || status == "error") {
                            val reason = pollJson.optString("error", "Échec de génération chez LTX")
                            emit(GenerationProgress.Error("Erreur LTX Video: $reason"))
                            return@flow
                        }
                    }
                } catch (e: Exception) {
                    // Transient network jitter, continue polling
                }
            }

            if (videoUrl.isNullOrBlank()) {
                emit(GenerationProgress.Error("Délai d'attente dépassé pour la vidéo LTX (#$jobId)."))
                return@flow
            }
        } else {
            emit(GenerationProgress.Error("Réponse inattendue de l'API LTX: $postResponseBody"))
            return@flow
        }

        // Download final video artifact locally for instant, offline playback
        emit(GenerationProgress.Status("Téléchargement du fichier MP4 généré par LTX...", 92))
        val localVideoPath = downloadRemoteVideo(videoUrl!!)
        if (localVideoPath == null) {
            emit(GenerationProgress.Error("Impossible de télécharger le fichier vidéo depuis $videoUrl"))
            return@flow
        }

        emit(GenerationProgress.Status("Vidéo LTX prête !", 100))

        val entity = CreationEntity(
            type = "VIDEO",
            title = generateTitle(prompt),
            prompt = prompt,
            negativePrompt = negativePrompt,
            modelEngine = "LTX Video ($model)",
            stylePreset = "Cinématique LTX",
            aspectRatio = if (resolution.contains("1080x1920") || resolution.contains("720x1280")) "9:16" else if (resolution.contains("1024x1024")) "1:1" else "16:9",
            durationSeconds = duration,
            mediaUrl = localVideoPath,
            thumbnailUrl = imageUriString,
            seed = seed,
            cfgScale = 3.5f,
            steps = 30,
            motionScore = 8,
            cameraMotion = cameraMotion,
            createdAt = System.currentTimeMillis(),
            tags = "LTXVideo,console.ltx.io,ImageToVideo,60FPS,$model",
            sourceImageUrl = imageUriString,
            inputMode = "IMAGE_TO_VIDEO",
            imageStrength = 0.85f,
            fps = fps,
            sampler = "Lightricks Flow Matching"
        )
        emit(GenerationProgress.Success(entity))
    }

    private fun generateTitle(prompt: String): String {
        val words = prompt.trim().split(" ").filter { it.isNotBlank() }
        return if (words.size <= 4) {
            words.joinToString(" ").replaceFirstChar { it.uppercase() }
        } else {
            words.take(4).joinToString(" ").replaceFirstChar { it.uppercase() } + "..."
        }
    }

    private fun downloadRemoteVideo(url: String): String? {
        return try {
            val request = Request.Builder().url(url).get().build()
            val response = httpClient.newCall(request).execute()
            if (!response.isSuccessful) return null

            val dir = File(context.filesDir, "creations")
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, "ltx_${System.currentTimeMillis()}.mp4")

            response.body?.byteStream()?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            file.toURI().toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
