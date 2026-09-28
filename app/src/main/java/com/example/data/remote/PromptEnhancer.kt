package com.example.data.remote

object PromptEnhancer {

    private val cinematicAdditions = listOf(
        "volumetric god rays streaming through atmosphere",
        "shot on ARRI Alexa Mini, 35mm anamorphic lens, f/1.8",
        "photorealistic subsurface scattering and intricate micro-details",
        "masterpiece composition, rule of thirds, dramatic chiaroscuro lighting",
        "award-winning cinematography, ultra-crisp textures, 8k resolution"
    )

    private val artisticKeywords = listOf(
        "vivid color grading",
        "intricate ambient occlusion",
        "hyper-detailed reflections",
        "hyperrealistic lighting",
        "Unreal Engine 5 lumen render"
    )

    fun enhance(rawPrompt: String, stylePreset: String, isVideo: Boolean): String {
        val trimmed = rawPrompt.trim()
        if (trimmed.isEmpty()) return rawPrompt

        val randomLighting = cinematicAdditions.shuffled().take(2).joinToString(", ")
        val randomDetail = artisticKeywords.shuffled().take(2).joinToString(", ")

        val motionText = if (isVideo) {
            ", continuous fluid dynamic motion, 60fps cinematic fluidity, smooth spatiotemporal camera trajectory"
        } else {
            ""
        }

        return "$trimmed, $randomLighting, $randomDetail, cinematic masterpiece, trending on Artstation$motionText"
    }

    val samplePrompts = listOf(
        "Cité néo-Tokyo sous la pluie battante avec reflets holographiques et voitures volantes",
        "Guerrier cybernétique avec armure en titane doré dans un temple ancien envahi par la jungle",
        "Vue aérienne d'une île flottante magique avec cascades cristallines se déversant dans les nuages",
        "Portrait photoréaliste d'une astronaute découvrant des fleurs bioluminescentes sur Mars",
        "Dragon mécanique forgé en verre teinté et engrenages d'horlogerie crachant une flamme violette",
        "Forêt de cristal féerique illuminée par des aurores boréales et des lucioles géantes",
        "Voiture de sport rétro-futuriste roulant à toute vitesse sur une autoroute synthwave au coucher de soleil"
    )
}
