package in.co.madhur.vocabbuilder.utils;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

import in.co.madhur.vocabbuilder.App;

/**
 * Utility class for Firebase Analytics tracking
 */
public class AnalyticsHelper {
    
    private static final String TAG = "AnalyticsHelper";
    
    /**
     * Track a custom event
     */
    public static void trackEvent(String eventName, Bundle parameters) {
        try {
            if (App.mFirebaseAnalytics != null) {
                App.mFirebaseAnalytics.logEvent(eventName, parameters);
                Log.d(TAG, "Tracked event: " + eventName);
            } else {
                Log.w(TAG, "Firebase Analytics not initialized");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error tracking event: " + eventName, e);
        }
    }
    
    /**
     * Track a custom event without parameters
     */
    public static void trackEvent(String eventName) {
        trackEvent(eventName, null);
    }
    
    /**
     * Track word added event
     */
    public static void trackWordAdded(String word, String definition) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, word);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, word);
        bundle.putString("definition", definition);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "vocabulary_word");
        
        trackEvent("word_added", bundle);
    }
    
    /**
     * Track word reviewed event
     */
    public static void trackWordReviewed(String word, boolean wasCorrect) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, word);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, word);
        bundle.putString("result", wasCorrect ? "correct" : "incorrect");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "vocabulary_review");
        
        trackEvent("word_reviewed", bundle);
    }
    
    /**
     * Track app opened event
     */
    public static void trackAppOpened() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "app_session");
        
        trackEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);
    }
    
    /**
     * Track search performed event
     */
    public static void trackSearch(String searchTerm) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, searchTerm);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "vocabulary_search");
        
        trackEvent(FirebaseAnalytics.Event.SEARCH, bundle);
    }
    
    /**
     * Track settings changed event
     */
    public static void trackSettingChanged(String settingName, String newValue) {
        Bundle bundle = new Bundle();
        bundle.putString("setting_name", settingName);
        bundle.putString("setting_value", newValue);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "app_setting");
        
        trackEvent("setting_changed", bundle);
    }
    
    /**
     * Track notification interaction
     */
    public static void trackNotificationInteraction(String notificationType, String action) {
        Bundle bundle = new Bundle();
        bundle.putString("notification_type", notificationType);
        bundle.putString("action", action);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "notification");
        
        trackEvent("notification_interaction", bundle);
    }
}
