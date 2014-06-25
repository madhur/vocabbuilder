/*
 * Author: Madhur Ahuja
 * Copyright (c) 2014.
 */

package in.co.madhur.vocabbuilder;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import in.co.madhur.vocabbuilder.service.MarkWordService;

/**
 * Created by madhur on 22-Jun-14.
 */
public class NotificationActivity extends Activity
{
    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(App.TAG, "NotificationActivity: onResume");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(App.TAG, "NotificationActivity: onCreate");

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        // Cancel the notification that initiated this activity.
        // This is required when using the action buttons in expanded notifications.
        // While the default action automatically closes the notification, the
        // actions initiated by buttons do not.
        int notificationId = intent.getIntExtra(Consts.INTENT_EXTRA_NOTIFICATION_ID, -1);
        if (notificationId != -1)
        {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationId);
        }

        int targetRating=intent.getIntExtra(Consts.TARGET_RATING, -1);
        int wordId=intent.getIntExtra(Consts.TARGET_WORD, -1);
        if(targetRating==-1 || wordId==-1)
        {
            // Finish activity.
            finish();
            return;
        }

        Intent serviceIntent=new Intent();
        serviceIntent.setClass(this, MarkWordService.class);

        serviceIntent.putExtra(Consts.TARGET_RATING, targetRating);
        serviceIntent.putExtra(Consts.TARGET_WORD, wordId);

        this.startService(serviceIntent);

        finish();
    }



}
