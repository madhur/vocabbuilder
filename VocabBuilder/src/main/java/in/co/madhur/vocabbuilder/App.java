package in.co.madhur.vocabbuilder;

import android.app.Application;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.FirebaseApp;

import in.co.madhur.vocabbuilder.db.Cache;

/**
 * Created by madhur on 19-Jun-14.
 */
public class App extends Application
{
    public static final String TAG="VocabBuilder";
    public static FirebaseAnalytics mFirebaseAnalytics;

    public static Cache Cache;

    @Override
    public void onCreate() {
        super.onCreate();
        
        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        
        // Initialize Firebase Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        
        Log.d(TAG, "Firebase Analytics initialized successfully");
    }
}
