# Firebase Analytics Integration

This document describes how Firebase Analytics has been integrated into the VocabBuilder app and how to use it.

## What's Already Set Up

✅ **Dependencies**: Firebase BOM and Analytics dependencies are already added to `build.gradle`
✅ **Google Services Plugin**: Added to top-level `build.gradle`
✅ **google-services.json**: Already placed in the app directory
✅ **Application Class**: Updated to initialize Firebase and Analytics
✅ **AnalyticsHelper**: Utility class created for easy event tracking

## How to Use

### 1. Basic Event Tracking

```java
// Track a simple event
AnalyticsHelper.trackEvent("button_clicked");

// Track an event with parameters
Bundle bundle = new Bundle();
bundle.putString("screen_name", "MainActivity");
bundle.putString("action", "add_word");
AnalyticsHelper.trackEvent("user_action", bundle);
```

### 2. Pre-built Tracking Methods

The `AnalyticsHelper` class provides several pre-built methods for common events:

```java
// Track when a word is added
AnalyticsHelper.trackWordAdded("example", "a thing characteristic of its kind");

// Track when a word is reviewed
AnalyticsHelper.trackWordReviewed("example", true); // true = correct

// Track app opened
AnalyticsHelper.trackAppOpened();

// Track search performed
AnalyticsHelper.trackSearch("vocabulary");

// Track settings changed
AnalyticsHelper.trackSettingChanged("notification_enabled", "true");

// Track notification interactions
AnalyticsHelper.trackNotificationInteraction("word_reminder", "dismissed");
```

### 3. Adding Analytics to New Features

When adding new features, consider tracking these events:

- **User Actions**: Button clicks, form submissions, selections
- **Content Interactions**: Word additions, deletions, modifications
- **Navigation**: Screen views, tab switches, drawer navigation
- **Performance**: App startup time, feature usage
- **Errors**: App crashes, failed operations

### 4. Example Implementation

```java
// In your Activity or Fragment
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    // Track screen view
    Bundle bundle = new Bundle();
    bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "WordDetailActivity");
    bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "WordDetailActivity");
    AnalyticsHelper.trackEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
}

// Track user actions
private void onWordStarred(String word) {
    // Your existing logic
    toggleWordStarred(word);
    
    // Track the action
    Bundle bundle = new Bundle();
    bundle.putString("word", word);
    bundle.putString("action", "starred");
    AnalyticsHelper.trackEvent("word_action", bundle);
}
```

## Firebase Console

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project
3. Navigate to Analytics > Dashboard
4. View real-time data and reports

## Important Notes

- **Privacy**: Only track non-personally identifiable information
- **Performance**: Analytics events are sent asynchronously and won't impact app performance
- **Testing**: Use Firebase Console's DebugView to test events during development
- **Compliance**: Ensure compliance with privacy regulations (GDPR, CCPA, etc.)

## Debugging

To debug analytics events:

1. Enable debug logging in Firebase Console
2. Check logcat for "AnalyticsHelper" tags
3. Use Firebase Console's DebugView for real-time event monitoring

## Next Steps

Consider implementing these additional analytics features:

- **User Properties**: Track user preferences, subscription status, etc.
- **Custom Dimensions**: Create custom metrics for your specific use cases
- **Conversion Tracking**: Track user journey from app install to word mastery
- **A/B Testing**: Use Firebase Remote Config for feature experiments
