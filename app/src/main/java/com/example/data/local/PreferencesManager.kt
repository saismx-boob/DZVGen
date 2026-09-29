package com.example.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("vision_ai_settings", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LTX_API_KEY = "ltx_api_key"
        private const val KEY_STABILITY_API_KEY = "stability_api_key"
        private const val KEY_RUNWAY_API_KEY = "runway_api_key"
        private const val KEY_DEFAULT_ENGINE = "default_engine"
        private const val KEY_DEFAULT_ASPECT = "default_aspect"
        private const val KEY_AUTO_SAVE = "auto_save"
        private const val KEY_ENHANCE_PROMPT_AUTO = "enhance_prompt_auto"
    }

    var ltxApiKey: String
        get() = prefs.getString(KEY_LTX_API_KEY, "") ?: ""
        set(value) = prefs.edit().putString(KEY_LTX_API_KEY, value).apply()

    var stabilityApiKey: String
        get() = prefs.getString(KEY_STABILITY_API_KEY, "") ?: ""
        set(value) = prefs.edit().putString(KEY_STABILITY_API_KEY, value).apply()

    var runwayApiKey: String
        get() = prefs.getString(KEY_RUNWAY_API_KEY, "") ?: ""
        set(value) = prefs.edit().putString(KEY_RUNWAY_API_KEY, value).apply()

    var defaultEngineId: String
        get() = prefs.getString(KEY_DEFAULT_ENGINE, "sd_xl") ?: "sd_xl"
        set(value) = prefs.edit().putString(KEY_DEFAULT_ENGINE, value).apply()

    var defaultAspect: String
        get() = prefs.getString(KEY_DEFAULT_ASPECT, "1:1") ?: "1:1"
        set(value) = prefs.edit().putString(KEY_DEFAULT_ASPECT, value).apply()

    var autoSave: Boolean
        get() = prefs.getBoolean(KEY_AUTO_SAVE, true)
        set(value) = prefs.edit().putBoolean(KEY_AUTO_SAVE, value).apply()

    var autoEnhancePrompt: Boolean
        get() = prefs.getBoolean(KEY_ENHANCE_PROMPT_AUTO, false)
        set(value) = prefs.edit().putBoolean(KEY_ENHANCE_PROMPT_AUTO, value).apply()
}
