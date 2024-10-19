package com.cemerlang.dentalint.analytics

import android.content.Context
import android.os.Bundle
import com.cemerlang.dentalint.BuildConfig
import com.cemerlang.dentalint.data.PrefManager
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

object AnalyticsHelper {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var prefManager: PrefManager

    fun initialize(context: Context) {
        firebaseAnalytics = Firebase.analytics
        prefManager = PrefManager(context)
        Firebase.analytics.setAnalyticsCollectionEnabled(false)
        if (BuildConfig.DEBUG) {
            Firebase.analytics.setAnalyticsCollectionEnabled(true)
        }
    }

    fun logSignIn() {
        val params = Bundle().apply {
            putString("user", prefManager.getString(PrefManager.Key.EMAIL))
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, params)
    }

    fun logFeature(name: String) {
        val params = Bundle().apply {
            putString("user", prefManager.getString(PrefManager.Key.EMAIL))
        }
        firebaseAnalytics.logEvent(name, params)
    }
}