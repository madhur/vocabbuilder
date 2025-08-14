package in.co.madhur.vocabbuilder.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import in.co.madhur.vocabbuilder.App;
import in.co.madhur.vocabbuilder.AppPreferences;
import in.co.madhur.vocabbuilder.Consts;

/**
 * Created by madhur on 21-Jun-14.
 */
public class Alarms
{

    int REQUEST_CODE_ALARM=1;
    private Context context;
    private AppPreferences appPreferences;

    //lowest recur in seconds
    int LOWEST_RECUR_INTERVAL=300;
//    int LOWEST_RECUR_INTERVAL=60;

//    public Alarms(Context context, AppPreferences appPreferences)
//    {
//
//        this.context=context;
//        this.appPreferences=appPreferences;
//    }

    public Alarms(Context context)
    {

        this.context=context;
        this.appPreferences=new AppPreferences(context);
    }

    public void Schedule()
    {

        AlarmManager alarmManager=GetAlarmManager(context);


        long recurInterval=LOWEST_RECUR_INTERVAL*1000;

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, recurInterval , recurInterval, GetPendingIntentAlarm(context) );
    }

    public boolean DoesAlarmExist()
    {
        PendingIntent existingIntent=PendingIntent.getBroadcast(context, REQUEST_CODE_ALARM, GetIntent(), PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE);

        if(existingIntent!=null)
        {
            Log.d(App.TAG, "Alarm exists");
            return true;
        }

        Log.d(App.TAG, "Alarm doesn't exist , scheduling");
        return false;
    }

    public void cancel()
    {
        GetAlarmManager(context).cancel(GetPendingIntentAlarm(context));
    }


    private static AlarmManager GetAlarmManager(Context context)
    {
        return (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

    }

    private PendingIntent GetPendingIntentAlarm(Context context)
    {
        return PendingIntent.getBroadcast(context, REQUEST_CODE_ALARM, GetIntent(), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

//    public PendingIntent GetPendingIntentWidget(Context context)
//    {
//        return PendingIntent.getBroadcast(context, REQUEST_CODE_WIDGET, GetIntent(), PendingIntent.FLAG_ONE_SHOT);
//    }

    private Intent GetIntent()
    {
        Intent updateIntent=new Intent();
        updateIntent.setAction(Consts.ACTION_REMIND_WORD);
        return updateIntent;
    }

//    public  void StartUpdate(UPDATESOURCE source)
//    {
//        Intent updateIntent=new Intent();
//        updateIntent.putExtra(Consts.UPDATE_SOURCE, source.key);
//        updateIntent.setClass(context, UpdateFeedCountService.class);
//        context.startService(updateIntent);
//    }

    public boolean ShouldSchedule()
    {
        return appPreferences.IsNotificationEnabled();
    }


}
