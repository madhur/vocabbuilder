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

import static in.co.madhur.vocabbuilder.Consts.*;

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

        int targetRating=intent.getIntExtra(TARGET_RATING, VALUE_NOT_SET);
        int wordId=intent.getIntExtra(TARGET_WORD, VALUE_NOT_SET);
        if(targetRating==VALUE_NOT_SET || wordId==VALUE_NOT_SET)
        {
            // Finish service.
            Log.e(App.TAG, "Word ID or rating not found in MarkWordService");
            return;
        }

        int retVal = VALUE_NOT_SET;
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
