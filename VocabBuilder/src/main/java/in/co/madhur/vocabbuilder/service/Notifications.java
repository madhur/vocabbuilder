package in.co.madhur.vocabbuilder.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import in.co.madhur.vocabbuilder.App;
import in.co.madhur.vocabbuilder.Consts;
import in.co.madhur.vocabbuilder.MainActivity;
import in.co.madhur.vocabbuilder.NotificationActivity;
import in.co.madhur.vocabbuilder.R;
import in.co.madhur.vocabbuilder.ui.WordActivity;

import static in.co.madhur.vocabbuilder.Consts.STAR.FULL_STAR;
import static in.co.madhur.vocabbuilder.Consts.STAR.HALF_STAR;
import static in.co.madhur.vocabbuilder.Consts.STAR.NO_STAR;

/**
 * Created by madhur on 21-Jun-14.
 */
public class Notifications
{

    private Context context;
    private  int PI_REQUEST_CODE = 0;

    public Notifications(Context context)
    {

        this.context = context;
    }

    public NotificationCompat.Builder GetNotificationBuilder(int id, int wordId, String title, String contentText, int rating)
    {
        NotificationCompat.Builder noti = new NotificationCompat.Builder(context);
        noti.setContentTitle(title);
        noti.setAutoCancel(true);
        noti.setTicker(contentText);
        noti.setContentText(contentText);
        noti.setSmallIcon(R.drawable.ic_notification);
        noti.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
        noti.setContentIntent(GetNotificationIntent(wordId));

        if(rating== NO_STAR.ordinal())
        {
            noti.addAction(R.drawable.half_star, context.getString(R.string.set), BuildNotificationActionIntent(id, wordId, HALF_STAR.ordinal()));
            noti.addAction(R.drawable.full_star, context.getString(R.string.set), BuildNotificationActionIntent(id, wordId, FULL_STAR.ordinal()));

        }
        else if(rating==HALF_STAR.ordinal())
        {
            noti.addAction(R.drawable.no_star, context.getString(R.string.set), BuildNotificationActionIntent(id, wordId, NO_STAR.ordinal()));
            noti.addAction(R.drawable.full_star,context.getString(R.string.set), BuildNotificationActionIntent(id, wordId, FULL_STAR.ordinal()));
        }
        else if(rating==FULL_STAR.ordinal())
        {
            noti.addAction(R.drawable.no_star,context.getString(R.string.set), BuildNotificationActionIntent(id, wordId, NO_STAR.ordinal()));
            noti.addAction(R.drawable.half_star, context.getString(R.string.set), BuildNotificationActionIntent(id, wordId, HALF_STAR.ordinal()));
        }

        noti.addAction(R.drawable.ic_action_remove, context.getString(R.string.dismiss), BuildNotificationActionIntent(id,wordId, Consts.VALUE_NOT_SET) );


        return noti;
    }

    public PendingIntent BuildNotificationActionIntent(int notificationId, int wordId, int targetRating)
    {
        // Build the file action intent (e.g. VIEW or SEND) that we eventually want to start.


        // Build the intent to start the NotificationActivity.
        final Intent notificationIntent = new Intent(context, NotificationActivity.class);
        // This flag must be set on activities started from a notification.
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Pass the file action and notification id to the NotificationActivity.
        notificationIntent.setAction(Consts.ACTION_MARK_WORD);
        notificationIntent.putExtra(Consts.INTENT_EXTRA_NOTIFICATION_ID, notificationId);
        notificationIntent.putExtra(Consts.TARGET_RATING, targetRating);
        notificationIntent.putExtra(Consts.TARGET_WORD, wordId);

        // Return a pending intent to pass to the notification manager.

        return PendingIntent.getActivity(context, PI_REQUEST_CODE++, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private PendingIntent GetNotificationIntent(int wordId)
    {
        Intent launchIntent = new Intent();
        launchIntent.setClass(context, WordActivity.class);
        launchIntent.setAction(Consts.ACTION_VIEW_WORD);

        Bundle data=new Bundle();
        data.putInt("id",wordId);
        launchIntent.putExtras(data);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack
        stackBuilder.addParentStack(WordActivity.class);
// Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(launchIntent);

        //PendingIntent resultPendingIntent = PendingIntent.getActivity(context, PI_REQUEST_CODE, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent resultPendingIntent =stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        return resultPendingIntent;


    }

    public NotificationCompat.Builder GetExpandedBuilder(NotificationCompat.Builder builder, String bigText, String summaryText)
    {

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        // Sets a title for the Inbox style big view
        bigTextStyle.setBigContentTitle(summaryText);
        bigTextStyle.setSummaryText(context.getString(R.string.app_name));

        bigTextStyle.bigText(bigText);


        // Moves the big view style object into the notification object.
        builder.setStyle(bigTextStyle);

        return builder;
    }


    public void FireNotification(int id, NotificationCompat.Builder builder, boolean vibrate, boolean sound, boolean led)
    {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.build();

        if (vibrate)
            notification.defaults |= Notification.DEFAULT_VIBRATE;
        if (sound)
        {
            notification.defaults |= Notification.DEFAULT_SOUND;
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        }
        if (led)
            notification.defaults |= Notification.DEFAULT_LIGHTS;

        mNotificationManager.notify(id, builder.build());

    }

//    public void FireNotification(int id, NotificationCompat.Builder builder)
//    {
//        FireNotification(id, builder, true, true, true);
//
//    }
//
//    public void Cancel(int id)
//    {
//        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.cancel(id);
//    }
//
//    private boolean isNotificationVisible()
//    {
//        Intent notificationIntent = new Intent(context, MainActivity.class);
//        PendingIntent test = PendingIntent.getActivity(context, PI_REQUEST_CODE, notificationIntent, PendingIntent.FLAG_NO_CREATE);
//        return test != null;
//    }


}
