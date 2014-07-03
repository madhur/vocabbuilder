package in.co.madhur.vocabbuilder.service;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;

import java.util.List;
import java.util.Random;

import in.co.madhur.vocabbuilder.App;
import in.co.madhur.vocabbuilder.AppPreferences;
import in.co.madhur.vocabbuilder.Consts;
import in.co.madhur.vocabbuilder.db.VocabDB;
import in.co.madhur.vocabbuilder.model.Word;

/**
 * Created by madhur on 21-Jun-14.
 */
public class RemindWordService extends WakefulIntentService
{
    private AppPreferences appPreferences;
    private int NOTIFICATION_ID=0;

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

        List<Word> words = null;

        try
        {
            words = vocabDB.GetFilteredWords(appPreferences.GetNotificationWordSetting());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

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

        if(appPreferences.IsWakelockEnabled())
        {
            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, App.TAG);
            wl.acquire(Consts.WAKE_LOCK_TIME);
        }


        try
        {
            vocabDB.PutRecentWord(selectedWord.getId());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

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


        NotificationCompat.Builder noti = notifications.GetNotificationBuilder(NOTIFICATION_ID, selectedWord.getId(), selectedWord.getName(), selectedWord.getMeaning(), selectedWord.getRating());

        noti = notifications.GetExpandedBuilder(noti, selectedWord.getMeaning(), selectedWord.getName());



        notifications.FireNotification(NOTIFICATION_ID, noti, appPreferences.getBoolMetadata(AppPreferences.Keys.ENABLE_VIBRATE), appPreferences.getBoolMetadata(AppPreferences.Keys.ENABLE_SOUND), appPreferences.getBoolMetadata(AppPreferences.Keys.ENABLE_LED));


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
