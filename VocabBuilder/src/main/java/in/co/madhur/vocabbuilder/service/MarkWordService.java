/*
 * Author: Madhur Ahuja
 * Copyright (c) 2014.
 */

package in.co.madhur.vocabbuilder.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import in.co.madhur.vocabbuilder.App;
import in.co.madhur.vocabbuilder.Consts;
import in.co.madhur.vocabbuilder.db.VocabDB;

/**
 * Created by madhur on 22-Jun-14.
 */
public class MarkWordService extends IntentService
{
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MarkWordService(String name)
    {
        super(name);
    }

    public MarkWordService()
    {
        super("MarkWordService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Log.d(App.TAG, "onHandleIntent");
        int targetRating=intent.getIntExtra(Consts.TARGET_RATING, -1);
        int wordId=intent.getIntExtra(Consts.TARGET_WORD, -1);
        if(targetRating==-1 || wordId==-1)
        {
            // Finish service.
            Log.e(App.TAG, "Word ID or rating not found in MarkWordService");
            return;
        }

        int retVal = -1;
        try
        {
            Log.d(App.TAG, "Updating " + String.valueOf(wordId) + " " + String.valueOf(targetRating));

            retVal= VocabDB.getInstance(this).UpdateRating(wordId, targetRating);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return;
    }
}
