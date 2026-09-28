package com.example.model

enum class MediaType {
    IMAGE,
    VIDEO
}

enum class AiEngine(
    val id: String,
    val displayName: String,
    val provider: String,
    val description: String,
    val mediaType: MediaType,
    val defaultModel: String,
    val isFree: Boolean = false,
    val versionTag: String = ""
) {
    // --- LTX VIDEO RECENT VERSIONS (LIGHTRICKS) ---
    LTX_VIDEO_2_5(
        id = "ltx_video_2_5",
        displayName = "LTX Video 2.5 Turbo",
        provider = "Lightricks Open Source",
        description = "Dernière version majeure LTX 2.5 : vitesse de rendu record, résolution augmentée, physique fluide 60 FPS (100% Gratuit)",
        mediaType = MediaType.VIDEO,
        defaultModel = "ltx-video-2.5-turbo",
        isFree = true,
        versionTag = "v2.5"
    ),
    LTX_VIDEO_2_3(
        id = "ltx_video_2_3",
        displayName = "LTX Video 2.3 HD",
        provider = "Lightricks Open Source",
        description = "Version cinématique LTX 2.3 : fidélité temporelle renforcée et motion tracking dynamique (100% Gratuit)",
        mediaType = MediaType.VIDEO,
        defaultModel = "ltx-video-2.3-hd",
        isFree = true,
        versionTag = "v2.3"
    ),
    LTX_VIDEO_2_0(
        id = "ltx_video_2_0",
        displayName = "LTX Video 2.0 Pro",
        provider = "Lightricks Open Source",
        description = "Version stable LTX 2.0 : contrôle précis des trajectoires caméra et cohérence spatiale (100% Gratuit)",
        mediaType = MediaType.VIDEO,
        defaultModel = "ltx-video-2.0",
        isFree = true,
        versionTag = "v2.0"
    ),
    LTX_VIDEO(
        id = "ltx_video",
        displayName = "LTX Video 0.9.5",
        provider = "Lightricks Open Source",
        description = "Version historique open-source en temps réel (100% Gratuit)",
        mediaType = MediaType.VIDEO,
        defaultModel = "ltx-video-0.9.5",
        isFree = true,
        versionTag = "v0.9"
    ),

    // --- OTHER FREE OPEN SOURCE MODELS ---
    FLUX_SCHNELL(
        id = "flux_schnell",
        displayName = "FLUX.1 Schnell",
        provider = "Black Forest Labs",
        description = "Architecture Diffusion Transformer open-source ultra-rapide de pointe (100% Gratuit)",
        mediaType = MediaType.IMAGE,
        defaultModel = "flux-1-schnell",
        isFree = true,
        versionTag = "v1.0"
    ),
    SD_TURBO(
        id = "sd_turbo",
        displayName = "SDXL Turbo Instant",
        provider = "Stability Open Weights",
        description = "Rendu quasi-instantané en 1 à 4 étapes de diffusion (100% Gratuit)",
        mediaType = MediaType.IMAGE,
        defaultModel = "sdxl-turbo",
        isFree = true,
        versionTag = "v1.0"
    ),
    COGVIDEOX(
        id = "cogvideox",
        displayName = "CogVideoX Open Video",
        provider = "THUDM Open Source",
        description = "Modèle vidéo open-source avec compréhension physique et spatiale (100% Gratuit)",
        mediaType = MediaType.VIDEO,
        defaultModel = "cogvideox-5b",
        isFree = true,
        versionTag = "v1.5"
    ),

    // --- CLOUD COMMERCIAL MODELS ---
    STABLE_DIFFUSION_XL(
        id = "sd_xl",
        displayName = "Stable Diffusion XL",
        provider = "Stability AI",
        description = "Rendu d'images haute fidélité 1024x1024 avec textures riches",
        mediaType = MediaType.IMAGE,
        defaultModel = "stable-diffusion-xl-1024-v1-0",
        isFree = false
    ),
    STABLE_DIFFUSION_3(
        id = "sd_3",
        displayName = "Stable Diffusion 3.5",
        provider = "Stability AI",
        description = "Génération ultra-réaliste avec compréhension typographique avancée",
        mediaType = MediaType.IMAGE,
        defaultModel = "sd3.5-large",
        isFree = false
    ),
    RUNWAY_GEN3(
        id = "runway_gen3",
        displayName = "Runway Gen-3 Alpha",
        provider = "Runway ML",
        description = "Génération vidéo cinématographique avec physique et mouvement réalistes",
        mediaType = MediaType.VIDEO,
        defaultModel = "gen3a_turbo",
        isFree = false
    ),
    RUNWAY_GEN2(
        id = "runway_gen2",
        displayName = "Runway Gen-2",
        provider = "Runway ML",
        description = "Création vidéo dynamique text-to-video et effets de caméra",
        mediaType = MediaType.VIDEO,
        defaultModel = "gen2",
        isFree = false
    );

    val isLtx: Boolean
        get() = this == LTX_VIDEO_2_5 || this == LTX_VIDEO_2_3 || this == LTX_VIDEO_2_0 || this == LTX_VIDEO
}

data class StylePreset(
    val id: String,
    val label: String,
    val promptSuffix: String,
    val negativeSuffix: String = "blurry, low quality, distorted, extra limbs, ugly, artifact",
    val iconName: String
)

object StylePresets {
    val list = listOf(
        StylePreset(
            id = "cinematic",
            label = "Cinématique",
            promptSuffix = ", 35mm film photograph, cinematic lighting, shallow depth of field, anamorphic lens flare, masterwork, 8k",
            iconName = "Movie"
        ),
        StylePreset(
            id = "photoreal",
            label = "Photo Réaliste",
            promptSuffix = ", hyperrealistic raw photography, Hasselblad camera, 85mm f/1.4, natural daylight, pores and fine textures, ultra-detailed",
            iconName = "CameraAlt"
        ),
        StylePreset(
            id = "cyberpunk",
            label = "Cyberpunk Néon",
            promptSuffix = ", cyberpunk aesthetic, neon glow, reflective wet asphalt, chromatic aberration, holographic HUD, synthwave violet cyan palette",
            iconName = "Bolt"
        ),
        StylePreset(
            id = "anime",
            label = "Anime Japonais",
            promptSuffix = ", modern anime key visual by Makoto Shinkai and Ufotable, expressive vibrant lighting, crisp line art, beautiful clouds, 4k",
            iconName = "Brush"
        ),
        StylePreset(
            id = "render3d",
            label = "Rendu 3D Octane",
            promptSuffix = ", 3D digital art, Octane render, raytracing, soft subsurface scattering, volumetric dust, vivid colors, Unreal Engine 5 showcase",
            iconName = "Category"
        ),
        StylePreset(
            id = "fantasy",
            label = "Dark Fantasy",
            promptSuffix = ", dark fantasy concept art, Elden Ring style, ethereal mist, ancient runes, dramatic chiaroscuro lighting, intricate matte painting",
            iconName = "AutoAwesome"
        ),
        StylePreset(
            id = "minimalist",
            label = "Minimaliste Pop",
            promptSuffix = ", minimalist modern graphic design, vector flat geometric shapes, bold clean colors, elegant negative space, Swiss style",
            iconName = "Layers"
        )
    )
}

enum class AspectRatioChoice(val label: String, val ratioWidth: Float, val ratioHeight: Float, val code: String) {
    SQUARE("1:1", 1f, 1f, "1:1"),
    LANDSCAPE("16:9", 16f, 9f, "16:9"),
    PORTRAIT("9:16", 9f, 16f, "9:16"),
    STANDARD_LANDSCAPE("4:3", 4f, 3f, "4:3"),
    STANDARD_PORTRAIT("3:4", 3f, 4f, "3:4")
}

data class CameraMotionOption(
    val id: String,
    val name: String,
    val description: String
)

object CameraMotions {
    val list = listOf(
        CameraMotionOption("zoom_in", "Zoom Avant", "Zoom fluide cinématique progressif vers le sujet"),
        CameraMotionOption("zoom_out", "Zoom Arrière", "Révélation progressive de l'environnement vaste"),
        CameraMotionOption("pan_right", "Travelling Droit", "Balayage horizontal régulier de gauche à droite"),
        CameraMotionOption("tilt_up", "Contre-plongée (Tilt)", "Mouvement vertical ascendant vers le ciel"),
        CameraMotionOption("orbit", "Orbite Circulaire", "Rotation dynamique à 360° autour du point focal"),
        CameraMotionOption("static_motion", "Mouvement Organique", "Caméra portée douce avec respiration naturelle")
    )
}
