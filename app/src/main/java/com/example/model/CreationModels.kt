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

    val supportsImageInput: Boolean
        get() = true // All models in VisionAI Studio support image guidance (Image-to-Image / Image-to-Video)

    val profile: ModelGenerationProfile
        get() = ModelOptionsRegistry.getProfile(this)
}

enum class GenerationInputMode(
    val label: String,
    val shortLabel: String,
    val isImageInput: Boolean,
    val description: String
) {
    TEXT_TO_IMAGE(
        label = "Texte vers Image (T2I)",
        shortLabel = "Texte ➔ Image",
        isImageInput = false,
        description = "Génération d'images haute fidélité à partir d'un prompt textuel"
    ),
    IMAGE_TO_IMAGE(
        label = "Image vers Image (I2I)",
        shortLabel = "Image ➔ Image",
        isImageInput = true,
        description = "Variation, restylage et transformation guidée à partir d'une photo source"
    ),
    TEXT_TO_VIDEO(
        label = "Texte vers Vidéo (T2V)",
        shortLabel = "Texte ➔ Vidéo",
        isImageInput = false,
        description = "Synthèse complète d'une séquence vidéo animée depuis un texte descriptif"
    ),
    IMAGE_TO_VIDEO(
        label = "Image vers Vidéo (I2V)",
        shortLabel = "Image ➔ Vidéo",
        isImageInput = true,
        description = "Animation cinématique fluide et mise en mouvement d'une image fixe"
    );

    val isVideo: Boolean
        get() = this == TEXT_TO_VIDEO || this == IMAGE_TO_VIDEO
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

data class ModelGenerationProfile(
    val engine: AiEngine,
    val architectureName: String,
    val summary: String,
    val minSteps: Int,
    val maxSteps: Int,
    val defaultSteps: Int,
    val minCfg: Float,
    val maxCfg: Float,
    val defaultCfg: Float,
    val supportedSamplers: List<String>,
    val defaultSampler: String,
    val supportedAspectRatios: List<AspectRatioChoice>,
    val supportedModes: List<GenerationInputMode>,
    val supportedDurations: List<Int>, // for video (seconds)
    val defaultDuration: Int,
    val supportedFps: List<Int>, // e.g. 60, 48, 30, 24
    val defaultFps: Int,
    val supportsNegativePrompt: Boolean,
    val supportsCameraMotion: Boolean,
    val supportsMotionScore: Boolean,
    val motionScoreLabel: String = "Dynamisme du Mouvement",
    val featureHighlights: List<String>,
    val promptRecommendation: String
)

object ModelOptionsRegistry {
    private val profiles = mapOf(
        AiEngine.LTX_VIDEO_2_5 to ModelGenerationProfile(
            engine = AiEngine.LTX_VIDEO_2_5,
            architectureName = "DiT Spatio-Temporel 60 FPS (Lightricks)",
            summary = "Génération vidéo en temps réel 60 FPS avec physique cinématique fluide et contrôle de caméra.",
            minSteps = 15,
            maxSteps = 45,
            defaultSteps = 25,
            minCfg = 2.0f,
            maxCfg = 7.0f,
            defaultCfg = 3.5f,
            supportedSamplers = listOf("Lightricks DiT Solver", "Euler Flow", "DPM-Solver++", "UniPC Video"),
            defaultSampler = "Lightricks DiT Solver",
            supportedAspectRatios = listOf(AspectRatioChoice.LANDSCAPE, AspectRatioChoice.PORTRAIT, AspectRatioChoice.SQUARE),
            supportedModes = listOf(GenerationInputMode.TEXT_TO_VIDEO, GenerationInputMode.IMAGE_TO_VIDEO),
            supportedDurations = listOf(5, 10),
            defaultDuration = 5,
            supportedFps = listOf(60, 30, 24),
            defaultFps = 60,
            supportsNegativePrompt = true,
            supportsCameraMotion = true,
            supportsMotionScore = true,
            motionScoreLabel = "Dynamique Spatio-Temporelle",
            featureHighlights = listOf("60 FPS Natif", "Physique Fluide", "DiT Spatio-Temporel", "100% Gratuit"),
            promptRecommendation = "Décrivez précisément l'action, l'éclairage volumétrique et la dynamique du sujet."
        ),
        AiEngine.LTX_VIDEO_2_3 to ModelGenerationProfile(
            engine = AiEngine.LTX_VIDEO_2_3,
            architectureName = "DiT HD Cinématique (Lightricks)",
            summary = "Fidélité visuelle accrue et cohérence temporelle optimale pour plans cinématographiques.",
            minSteps = 20,
            maxSteps = 50,
            defaultSteps = 30,
            minCfg = 2.5f,
            maxCfg = 8.0f,
            defaultCfg = 4.0f,
            supportedSamplers = listOf("Lightricks DiT Solver", "Euler SDE", "DPM-Solver++"),
            defaultSampler = "Lightricks DiT Solver",
            supportedAspectRatios = listOf(AspectRatioChoice.LANDSCAPE, AspectRatioChoice.PORTRAIT, AspectRatioChoice.SQUARE),
            supportedModes = listOf(GenerationInputMode.TEXT_TO_VIDEO, GenerationInputMode.IMAGE_TO_VIDEO),
            supportedDurations = listOf(5, 10),
            defaultDuration = 5,
            supportedFps = listOf(48, 24),
            defaultFps = 48,
            supportsNegativePrompt = true,
            supportsCameraMotion = true,
            supportsMotionScore = true,
            motionScoreLabel = "Intensité Temporelle",
            featureHighlights = listOf("Cinématique HD", "Motion Tracking", "Stabilité Temporelle"),
            promptRecommendation = "Idéal pour travellings lents, portraits animés et paysages naturels vivants."
        ),
        AiEngine.LTX_VIDEO_2_0 to ModelGenerationProfile(
            engine = AiEngine.LTX_VIDEO_2_0,
            architectureName = "DiT Stable v2.0 (Lightricks)",
            summary = "Version de référence pour contrôle direct de trajectoires de caméra et cohérence spatiale.",
            minSteps = 18,
            maxSteps = 40,
            defaultSteps = 28,
            minCfg = 2.5f,
            maxCfg = 7.5f,
            defaultCfg = 3.8f,
            supportedSamplers = listOf("Lightricks DiT Solver", "Euler SDE"),
            defaultSampler = "Lightricks DiT Solver",
            supportedAspectRatios = listOf(AspectRatioChoice.LANDSCAPE, AspectRatioChoice.PORTRAIT, AspectRatioChoice.SQUARE),
            supportedModes = listOf(GenerationInputMode.TEXT_TO_VIDEO, GenerationInputMode.IMAGE_TO_VIDEO),
            supportedDurations = listOf(5),
            defaultDuration = 5,
            supportedFps = listOf(30, 24),
            defaultFps = 30,
            supportsNegativePrompt = true,
            supportsCameraMotion = true,
            supportsMotionScore = true,
            motionScoreLabel = "Intensité du Mouvement",
            featureHighlights = listOf("DiT v2.0", "Contrôle Caméra", "Faible Latence"),
            promptRecommendation = "Spécifiez les mouvements de caméra voulus et les détails principaux de la scène."
        ),
        AiEngine.LTX_VIDEO to ModelGenerationProfile(
            engine = AiEngine.LTX_VIDEO,
            architectureName = "DiT Realtime Core v0.9",
            summary = "Modèle DiT open-source historique avec exécution en temps réel.",
            minSteps = 15,
            maxSteps = 35,
            defaultSteps = 20,
            minCfg = 2.0f,
            maxCfg = 6.0f,
            defaultCfg = 3.0f,
            supportedSamplers = listOf("DiT Euler", "DDIM"),
            defaultSampler = "DiT Euler",
            supportedAspectRatios = listOf(AspectRatioChoice.LANDSCAPE, AspectRatioChoice.SQUARE),
            supportedModes = listOf(GenerationInputMode.TEXT_TO_VIDEO, GenerationInputMode.IMAGE_TO_VIDEO),
            supportedDurations = listOf(5),
            defaultDuration = 5,
            supportedFps = listOf(24),
            defaultFps = 24,
            supportsNegativePrompt = true,
            supportsCameraMotion = true,
            supportsMotionScore = true,
            motionScoreLabel = "Mouvement",
            featureHighlights = listOf("Vitesse Record", "Open-Source"),
            promptRecommendation = "Mouvements francs et sujets bien cadrés."
        ),
        AiEngine.COGVIDEOX to ModelGenerationProfile(
            engine = AiEngine.COGVIDEOX,
            architectureName = "3D VAE Spatial-Temporal (THUDM)",
            summary = "Compression VAE 3D avancée avec modélisation continue de l'espace et du temps.",
            minSteps = 25,
            maxSteps = 50,
            defaultSteps = 35,
            minCfg = 3.0f,
            maxCfg = 9.0f,
            defaultCfg = 5.0f,
            supportedSamplers = listOf("DPM-Solver++ 3D", "DDIM Temporal"),
            defaultSampler = "DPM-Solver++ 3D",
            supportedAspectRatios = listOf(AspectRatioChoice.LANDSCAPE, AspectRatioChoice.PORTRAIT, AspectRatioChoice.STANDARD_LANDSCAPE),
            supportedModes = listOf(GenerationInputMode.TEXT_TO_VIDEO, GenerationInputMode.IMAGE_TO_VIDEO),
            supportedDurations = listOf(5, 8),
            defaultDuration = 5,
            supportedFps = listOf(24),
            defaultFps = 24,
            supportsNegativePrompt = true,
            supportsCameraMotion = true,
            supportsMotionScore = true,
            motionScoreLabel = "Cohérence 3D",
            featureHighlights = listOf("VAE 3D", "Cohérence Tridimensionnelle", "Open-Weights"),
            promptRecommendation = "Privilégiez les ambiances volumétriques, changements de perspective et scènes denses."
        ),
        AiEngine.RUNWAY_GEN3 to ModelGenerationProfile(
            engine = AiEngine.RUNWAY_GEN3,
            architectureName = "Gen-3 Alpha Temporal Studio (Runway)",
            summary = "Génération vidéo de calibre cinématographique avec physique réaliste et mouvements complexes.",
            minSteps = 25,
            maxSteps = 50,
            defaultSteps = 35,
            minCfg = 3.0f,
            maxCfg = 10.0f,
            defaultCfg = 6.0f,
            supportedSamplers = listOf("Runway Alpha Engine", "Director Flow"),
            defaultSampler = "Runway Alpha Engine",
            supportedAspectRatios = listOf(AspectRatioChoice.LANDSCAPE, AspectRatioChoice.PORTRAIT),
            supportedModes = listOf(GenerationInputMode.TEXT_TO_VIDEO, GenerationInputMode.IMAGE_TO_VIDEO),
            supportedDurations = listOf(5, 10),
            defaultDuration = 5,
            supportedFps = listOf(30, 24),
            defaultFps = 30,
            supportsNegativePrompt = true,
            supportsCameraMotion = true,
            supportsMotionScore = true,
            motionScoreLabel = "Motion Brush Runway",
            featureHighlights = listOf("Rendu Hollywood", "Physique Avancée", "Director Camera"),
            promptRecommendation = "Utilisez un vocabulaire cinématographique : éclairage volumétrique, grain film 35mm, travel shot."
        ),
        AiEngine.RUNWAY_GEN2 to ModelGenerationProfile(
            engine = AiEngine.RUNWAY_GEN2,
            architectureName = "Gen-2 Dynamic Motion Engine",
            summary = "Rendu vidéo dynamique text-to-video et effets de caméra directifs.",
            minSteps = 20,
            maxSteps = 45,
            defaultSteps = 30,
            minCfg = 3.0f,
            maxCfg = 9.0f,
            defaultCfg = 5.0f,
            supportedSamplers = listOf("Gen-2 Core", "DDIM"),
            defaultSampler = "Gen-2 Core",
            supportedAspectRatios = listOf(AspectRatioChoice.LANDSCAPE, AspectRatioChoice.PORTRAIT),
            supportedModes = listOf(GenerationInputMode.TEXT_TO_VIDEO, GenerationInputMode.IMAGE_TO_VIDEO),
            supportedDurations = listOf(4, 8),
            defaultDuration = 4,
            supportedFps = listOf(24),
            defaultFps = 24,
            supportsNegativePrompt = true,
            supportsCameraMotion = true,
            supportsMotionScore = true,
            motionScoreLabel = "Motion Intensity",
            featureHighlights = listOf("Director Mode", "Dynamic Camera"),
            promptRecommendation = "Des prompts stylisés et contrastés."
        ),
        AiEngine.FLUX_SCHNELL to ModelGenerationProfile(
            engine = AiEngine.FLUX_SCHNELL,
            architectureName = "Rectified Flow Transformer 12B (Black Forest Labs)",
            summary = "Modèle Flow Matching de 12 milliards de paramètres distillé pour une qualité maximale en seulement 4 étapes.",
            minSteps = 1,
            maxSteps = 8,
            defaultSteps = 4,
            minCfg = 1.0f,
            maxCfg = 4.0f,
            defaultCfg = 2.0f,
            supportedSamplers = listOf("FlowMatchEuler", "Heun Flow", "UniPC Distilled"),
            defaultSampler = "FlowMatchEuler",
            supportedAspectRatios = listOf(
                AspectRatioChoice.SQUARE,
                AspectRatioChoice.LANDSCAPE,
                AspectRatioChoice.PORTRAIT,
                AspectRatioChoice.STANDARD_LANDSCAPE,
                AspectRatioChoice.STANDARD_PORTRAIT
            ),
            supportedModes = listOf(GenerationInputMode.TEXT_TO_IMAGE, GenerationInputMode.IMAGE_TO_IMAGE),
            supportedDurations = emptyList(),
            defaultDuration = 0,
            supportedFps = emptyList(),
            defaultFps = 0,
            supportsNegativePrompt = false,
            supportsCameraMotion = false,
            supportsMotionScore = false,
            featureHighlights = listOf("4 Étapes Ultra-Rapide", "Architecture Flow Matching", "Encodeur T5-XXL", "100% Gratuit"),
            promptRecommendation = "Rédigez des descriptions complètes en langage naturel; le modèle comprend précisément la syntaxe."
        ),
        AiEngine.SD_TURBO to ModelGenerationProfile(
            engine = AiEngine.SD_TURBO,
            architectureName = "Adversarial Diffusion Distillation (ADD 1-Step)",
            summary = "Génération d'image ultra-rapide en 1 à 4 étapes sans latence perceptible.",
            minSteps = 1,
            maxSteps = 4,
            defaultSteps = 2,
            minCfg = 1.0f,
            maxCfg = 2.5f,
            defaultCfg = 1.5f,
            supportedSamplers = listOf("Euler a ADD", "DPM++ SDE Karras"),
            defaultSampler = "Euler a ADD",
            supportedAspectRatios = listOf(
                AspectRatioChoice.SQUARE,
                AspectRatioChoice.LANDSCAPE,
                AspectRatioChoice.PORTRAIT,
                AspectRatioChoice.STANDARD_LANDSCAPE
            ),
            supportedModes = listOf(GenerationInputMode.TEXT_TO_IMAGE, GenerationInputMode.IMAGE_TO_IMAGE),
            supportedDurations = emptyList(),
            defaultDuration = 0,
            supportedFps = emptyList(),
            defaultFps = 0,
            supportsNegativePrompt = true,
            supportsCameraMotion = false,
            supportsMotionScore = false,
            featureHighlights = listOf("Instantané 1-4 Steps", "Zéro Latence", "Temps Réel"),
            promptRecommendation = "Prompts concis et directs pour une composition instantanée."
        ),
        AiEngine.STABLE_DIFFUSION_XL to ModelGenerationProfile(
            engine = AiEngine.STABLE_DIFFUSION_XL,
            architectureName = "Dual-Text Encoder Latent Diffusion (SDXL 1.0)",
            summary = "Génération d'images haute fidélité 1024x1024 avec textures riches et support de prompts négatifs complet.",
            minSteps = 20,
            maxSteps = 60,
            defaultSteps = 30,
            minCfg = 3.5f,
            maxCfg = 15.0f,
            defaultCfg = 7.5f,
            supportedSamplers = listOf("Euler a", "DPM++ 2M Karras", "DDIM", "DPM++ SDE Karras", "UniPC"),
            defaultSampler = "DPM++ 2M Karras",
            supportedAspectRatios = listOf(
                AspectRatioChoice.SQUARE,
                AspectRatioChoice.LANDSCAPE,
                AspectRatioChoice.PORTRAIT,
                AspectRatioChoice.STANDARD_LANDSCAPE,
                AspectRatioChoice.STANDARD_PORTRAIT
            ),
            supportedModes = listOf(GenerationInputMode.TEXT_TO_IMAGE, GenerationInputMode.IMAGE_TO_IMAGE),
            supportedDurations = emptyList(),
            defaultDuration = 0,
            supportedFps = emptyList(),
            defaultFps = 0,
            supportsNegativePrompt = true,
            supportsCameraMotion = false,
            supportsMotionScore = false,
            featureHighlights = listOf("Résolution Native 1024", "Dual CLIP Encoders", "Rendu Artistique"),
            promptRecommendation = "Combinez le sujet principal, la technique artistique et les modificateurs de lumière."
        ),
        AiEngine.STABLE_DIFFUSION_3 to ModelGenerationProfile(
            engine = AiEngine.STABLE_DIFFUSION_3,
            architectureName = "Multimodal Diffusion Transformer (SD 3.5 Large)",
            summary = "Architecture MMDiT avec encodeurs triples (CLIP L + CLIP G + T5) pour typographie et composition anatomique précises.",
            minSteps = 24,
            maxSteps = 50,
            defaultSteps = 35,
            minCfg = 3.0f,
            maxCfg = 10.0f,
            defaultCfg = 5.0f,
            supportedSamplers = listOf("FlowMatchEuler", "DPM++ 2M", "Euler", "Heun"),
            defaultSampler = "FlowMatchEuler",
            supportedAspectRatios = listOf(
                AspectRatioChoice.SQUARE,
                AspectRatioChoice.LANDSCAPE,
                AspectRatioChoice.PORTRAIT,
                AspectRatioChoice.STANDARD_LANDSCAPE,
                AspectRatioChoice.STANDARD_PORTRAIT
            ),
            supportedModes = listOf(GenerationInputMode.TEXT_TO_IMAGE, GenerationInputMode.IMAGE_TO_IMAGE),
            supportedDurations = emptyList(),
            defaultDuration = 0,
            supportedFps = emptyList(),
            defaultFps = 0,
            supportsNegativePrompt = true,
            supportsCameraMotion = false,
            supportsMotionScore = false,
            featureHighlights = listOf("Architecture MMDiT", "Typographie dans l'Image", "Triple Encodeur CLIP+T5"),
            promptRecommendation = "Vous pouvez inclure du texte exact entre guillemets et décrire des scènes complexes multi-sujets."
        )
    )

    fun getProfile(engine: AiEngine): ModelGenerationProfile {
        return profiles[engine] ?: profiles[AiEngine.FLUX_SCHNELL]!!
    }
}
