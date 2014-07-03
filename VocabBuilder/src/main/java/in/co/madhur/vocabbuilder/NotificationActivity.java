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

import static in.co.madhur.vocabbuilder.Consts.VALUE_NOT_SET;

/**
 * Created by madhur on 22-Jun-14.
 */
public class NotificationActivity extends Activity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        // Cancel the notification that initiated this activity.
        // This is required when using the action buttons in expanded notifications.
        // While the default action automatically closes the notification, the
        // actions initiated by buttons do not.
        int notificationId = intent.getIntExtra(Consts.INTENT_EXTRA_NOTIFICATION_ID, VALUE_NOT_SET);
        if (notificationId != VALUE_NOT_SET)
        {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationId);
        }

        int targetRating=intent.getIntExtra(Consts.TARGET_RATING,  VALUE_NOT_SET);
        int wordId=intent.getIntExtra(Consts.TARGET_WORD, VALUE_NOT_SET);
        if(targetRating== VALUE_NOT_SET || wordId== VALUE_NOT_SET)
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
