package com.example.jarvis

import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import java.util.Locale

class AppLauncherEngine(private val context: Context) {
    private var tts: TextToSpeech? = null

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.UK
            }
        }
    }

    fun speakAndLaunch(appName: String) {
        val packageName = findPackageByName(appName)
        if (packageName != null) {
            speak("Right away, sir.")
            Thread.sleep(1000)
            launchApp(packageName)
        } else {
            speak("I am sorry, sir. Application not found.")
        }
    }

    private fun speak(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun findPackageByName(name: String): String? {
        val pm = context.packageManager
        val packages = pm.getInstalledPackages(0)
        val query = name.lowercase()

        for (pkg in packages) {
            val appLabel = pm.getApplicationLabel(pkg.applicationInfo).toString().lowercase()
            if (appLabel.contains(query)) {
                return pkg.packageName
            }
        }
        return null
    }

    private fun launchApp(packageName: String) {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    fun destroy() {
        tts?.stop()
        tts?.shutdown()
    }
}
