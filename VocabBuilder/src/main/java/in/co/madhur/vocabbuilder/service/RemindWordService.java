package in.co.madhur.vocabbuilder.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;

import java.util.List;
import java.util.Random;

import in.co.madhur.vocabbuilder.App;
import in.co.madhur.vocabbuilder.AppPreferences;
import in.co.madhur.vocabbuilder.db.VocabDB;
import in.co.madhur.vocabbuilder.model.Word;

/**
 * Created by madhur on 21-Jun-14.
 */
public class RemindWordService extends WakefulIntentService
{
    private AppPreferences appPreferences;

    public RemindWordService()
    {
        super("");
    }

    @Override
    protected void doWakefulWork(Intent intent)
    {
        Log.d(App.TAG, "Starting service");

         appPreferences = new AppPreferences(this);

        if (!CheckLastSync())
        {
            Log.d(App.TAG, "Successful notification within time interval. aborting");
            return;
        }



        VocabDB vocabDB = VocabDB.getInstance(this);

        List<Word> words = vocabDB.GetFilteredWords(appPreferences.GetNotificationWordSetting());

        if (words.size() == 0)
        {
            Log.d(App.TAG, "No suitable words");
            return;
        }

        appPreferences = new AppPreferences(this);

        Random rnd = new Random();
        int rndNumber = rnd.nextInt(words.size());

        Word selectedWord = words.get(rndNumber);


        SendNotification(selectedWord);

        appPreferences.SaveSuccessfulNotification();


    }

    private boolean CheckLastSync()
    {
        long lastSync = appPreferences.GetLastSuccessfulNotification();

        // First time sync, Just return true
        if (lastSync == 0)
            return true;

        // Else see if we are not doing too much sync within the sync interval
        int syncIntervalSeconds = appPreferences.GetNotificationSchedule();

        long syncIntervalMillisec = syncIntervalSeconds* 1000;
        if (System.currentTimeMillis() - lastSync > syncIntervalMillisec)
            return true;

        return false;
    }

    private void SendNotification(Word selectedWord)
    {

        Notifications notifications = new Notifications(this);

        NotificationCompat.Builder noti = notifications.GetNotificationBuilder(selectedWord.getName(), selectedWord.getMeaning());

        noti = notifications.GetExpandedBuilder(noti, selectedWord.getMeaning(), selectedWord.getName());

        notifications.FireNotification(0, noti, appPreferences.getBoolMetadata(AppPreferences.Keys.ENABLE_VIBRATE), appPreferences.getBoolMetadata(AppPreferences.Keys.ENABLE_SOUND), appPreferences.getBoolMetadata(AppPreferences.Keys.ENABLE_LED));


    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RemindWordService(String name)
    {
        super(name);
    }


}
