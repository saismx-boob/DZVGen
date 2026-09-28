package com.example.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.local.CreationEntity
import java.io.File

object SocialShareManager {

    enum class SocialPlatform(
        val label: String,
        val packageName: String,
        val description: String
    ) {
        GENERIC("Partager...", "", "Menu de partage Android"),
        INSTAGRAM("Instagram", "com.instagram.android", "Stories & Feed"),
        TIKTOK("TikTok", "com.zhiliaoapp.musically", "Post direct ou son"),
        WHATSAPP("WhatsApp", "com.whatsapp", "Discussion & Statut"),
        X_TWITTER("X (Twitter)", "com.twitter.android", "Tweet avec hashtag"),
        TELEGRAM("Telegram", "org.telegram.messenger", "Canal ou message"),
        FACEBOOK("Facebook", "com.facebook.katana", "Fil d'actualité")
    }

    fun shareToSocial(
        context: Context,
        creation: CreationEntity,
        platform: SocialPlatform = SocialPlatform.GENERIC
    ) {
        val caption = buildSocialCaption(creation)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = if (creation.type == "VIDEO") "video/*" else "image/*"
            putExtra(Intent.EXTRA_TEXT, caption)
            putExtra(Intent.EXTRA_SUBJECT, "Création IA - ${creation.title}")

            // Attach media stream if local file exists
            getShareableUri(context, creation.mediaUrl)?.let { uri ->
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            if (platform.packageName.isNotEmpty()) {
                setPackage(platform.packageName)
            }
        }

        try {
            if (platform.packageName.isNotEmpty()) {
                val packageManager = context.packageManager
                val activities = packageManager.queryIntentActivities(intent, 0)
                if (activities.isNotEmpty()) {
                    context.startActivity(intent)
                    return
                }
            }
            // Fallback to chooser
            val chooser = Intent.createChooser(intent, "Partager via VisionAI Studio")
            context.startActivity(chooser)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Impossible d'ouvrir l'application ciblée (${platform.label}), ouverture du sélecteur universel.",
                Toast.LENGTH_SHORT
            ).show()
            try {
                val genericIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, caption)
                }
                context.startActivity(Intent.createChooser(genericIntent, "Partager ma création IA"))
            } catch (err: Exception) {
                // Ignore
            }
        }
    }

    fun copyPromptToClipboard(context: Context, prompt: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Prompt IA VisionAI", prompt)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Prompt copié dans le presse-papiers !", Toast.LENGTH_SHORT).show()
    }

    fun copyCaptionToClipboard(context: Context, creation: CreationEntity) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Légende Réseaux Sociaux", buildSocialCaption(creation))
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Légende et hashtags copiés !", Toast.LENGTH_SHORT).show()
    }

    private fun buildSocialCaption(creation: CreationEntity): String {
        val engineTag = if (creation.modelEngine.contains("Runway", ignoreCase = true)) {
            "#RunwayGen3 #RunwayML"
        } else {
            "#StableDiffusion #SDXL"
        }
        val mediaTag = if (creation.type == "VIDEO") "#AIVideo #CinematicVideo" else "#AIArt #AIImage"

        return """
✨ Rendu généré avec VisionAI Studio
🤖 Moteur : ${creation.modelEngine}
🎨 Style : ${creation.stylePreset.ifEmpty { "Créatif" }}
💡 Prompt : "${creation.prompt}"

$engineTag $mediaTag #ArtificialIntelligence #GenerativeAI #CreativeTech
        """.trimIndent()
    }

    private fun getShareableUri(context: Context, mediaUrl: String): Uri? {
        return try {
            if (mediaUrl.startsWith("file://")) {
                val file = File(Uri.parse(mediaUrl).path ?: "")
                if (file.exists()) {
                    FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                } else null
            } else if (mediaUrl.startsWith("android.resource://")) {
                Uri.parse(mediaUrl)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
