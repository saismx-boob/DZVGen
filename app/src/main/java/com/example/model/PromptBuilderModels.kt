package com.example.model

enum class TagCategory(val label: String, val iconName: String) {
    ALL("Tous", "Layers"),
    STYLE("Styles", "Palette"),
    LIGHTING("Éclairage", "WbSunny"),
    QUALITY("Qualité & Rendu", "HighQuality"),
    CAMERA("Caméra & Angles", "CameraAlt"),
    MOTION("Mouvement (Vidéo)", "Videocam")
}

data class PromptTag(
    val id: String,
    val labelFr: String,
    val token: String,
    val category: TagCategory
)

object PromptTagCatalog {
    val tags: List<PromptTag> = listOf(
        // Style
        PromptTag("cinematic", "Cinématique", "cinematic film still", TagCategory.STYLE),
        PromptTag("photoreal", "Photo Réaliste", "photorealistic, 8k raw photo", TagCategory.STYLE),
        PromptTag("abstract", "Abstrait", "abstract expressionism, surreal geometries", TagCategory.STYLE),
        PromptTag("cyberpunk", "Cyberpunk", "cyberpunk neon aesthetic", TagCategory.STYLE),
        PromptTag("dark_fantasy", "Dark Fantasy", "dark fantasy concept art", TagCategory.STYLE),
        PromptTag("anime", "Anime Japonais", "Makoto Shinkai anime aesthetic", TagCategory.STYLE),
        PromptTag("steampunk", "Steampunk", "intricate steampunk brass gears", TagCategory.STYLE),
        PromptTag("minimalist", "Minimaliste", "minimalist clean composition", TagCategory.STYLE),
        PromptTag("vintage", "Vintage 35mm", "vintage 1970s kodachrome film", TagCategory.STYLE),
        PromptTag("synthwave", "Synthwave 80s", "retro synthwave vaporwave glow", TagCategory.STYLE),

        // Lighting
        PromptTag("volumetric", "Lumière Volumétrique", "volumetric god rays streaming", TagCategory.LIGHTING),
        PromptTag("golden_hour", "Heure Dorée", "warm golden hour sunlight", TagCategory.LIGHTING),
        PromptTag("neon_glow", "Néons Éclatants", "vibrant neon ambient glow", TagCategory.LIGHTING),
        PromptTag("bioluminescent", "Bioluminescent", "ethereal bioluminescent lighting", TagCategory.LIGHTING),
        PromptTag("moody", "Ombres Dramatiques", "moody chiaroscuro dramatic shadows", TagCategory.LIGHTING),
        PromptTag("studio_light", "Éclairage Studio", "professional studio softbox lighting", TagCategory.LIGHTING),
        PromptTag("ray_tracing", "Ray Tracing", "ray traced realistic reflections", TagCategory.LIGHTING),

        // Quality
        PromptTag("high_resolution", "Haute Résolution 8K", "8k resolution, ultra-detailed", TagCategory.QUALITY),
        PromptTag("unreal_engine", "Unreal Engine 5", "Unreal Engine 5 lumen render", TagCategory.QUALITY),
        PromptTag("masterpiece", "Chef-d'œuvre", "masterpiece, trending on Artstation", TagCategory.QUALITY),
        PromptTag("sharp_focus", "Focus Net", "sharp micro focus, fine textures", TagCategory.QUALITY),
        PromptTag("octane_render", "Rendu Octane", "hyperrealistic octane 3D render", TagCategory.QUALITY),
        PromptTag("award_winning", "Primé", "award-winning photography", TagCategory.QUALITY),

        // Camera
        PromptTag("wide_angle", "Grand Angle", "wide angle panoramic shot", TagCategory.CAMERA),
        PromptTag("macro", "Macro Gros Plan", "extreme macro close-up shot", TagCategory.CAMERA),
        PromptTag("drone_aerial", "Vue Aérienne Drone", "cinematic aerial drone photography", TagCategory.CAMERA),
        PromptTag("low_angle", "Contre-plongée", "dramatic low angle perspective", TagCategory.CAMERA),
        PromptTag("bokeh", "Flou d'Arrière-Plan", "shallow depth of field, creamy bokeh", TagCategory.CAMERA),
        PromptTag("lens_flare", "Reflet Anamorphique", "anamorphic cinematic lens flare", TagCategory.CAMERA),

        // Motion (for Video: LTX / Runway)
        PromptTag("smooth_60fps", "Fluide 60 FPS", "smooth 60fps high framerate", TagCategory.MOTION),
        PromptTag("slow_motion", "Ralenti (Slow-Mo)", "ultra-smooth slow motion", TagCategory.MOTION),
        PromptTag("dynamic_pan", "Travelling Dynamique", "dynamic continuous camera pan", TagCategory.MOTION),
        PromptTag("spatiotemporal", "Suivi Spatio-Temporel", "spatiotemporal physical coherence", TagCategory.MOTION),
        PromptTag("fluid_motion", "Mouvement Organique", "fluid natural organic motion", TagCategory.MOTION)
    )

    fun isTagInPrompt(prompt: String, tag: PromptTag): Boolean {
        return prompt.contains(tag.token, ignoreCase = true) || prompt.contains(tag.labelFr, ignoreCase = true)
    }

    fun toggleTagInPrompt(currentPrompt: String, tag: PromptTag): String {
        val trimmed = currentPrompt.trim()
        if (isTagInPrompt(trimmed, tag)) {
            // Remove token
            var cleaned = trimmed
                .replace(", ${tag.token}", "")
                .replace("${tag.token}, ", "")
                .replace(tag.token, "")
                .trim()
            if (cleaned.endsWith(",")) cleaned = cleaned.dropLast(1).trim()
            if (cleaned.startsWith(",")) cleaned = cleaned.drop(1).trim()
            return cleaned
        } else {
            // Append token
            return if (trimmed.isEmpty()) {
                tag.token
            } else if (trimmed.endsWith(",")) {
                "$trimmed ${tag.token}"
            } else {
                "$trimmed, ${tag.token}"
            }
        }
    }
}
