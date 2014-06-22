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

import java.util.ArrayList;

import in.co.madhur.vocabbuilder.Consts;
import in.co.madhur.vocabbuilder.MainActivity;
import in.co.madhur.vocabbuilder.R;

/**
 * Created by madhur on 21-Jun-14.
 */
public class Notifications
{

    private Context context;
    private final int PI_REQUEST_CODE=0;

    public Notifications(Context context)
    {

        this.context = context;
    }

    public NotificationCompat.Builder GetNotificationBuilder(String title, String contentText)
    {
        NotificationCompat.Builder noti = new NotificationCompat.Builder(context);
        noti.setContentTitle(title);
        noti.setAutoCancel(true);
        noti.setTicker(contentText);
        noti.setContentText(contentText);
        noti.setSmallIcon(R.drawable.ic_notification);
        noti.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
        noti.setContentIntent(GetNotificationIntent());

        return noti;
    }


    private PendingIntent GetNotificationIntent()
    {
        Intent launchIntent = new Intent();
        launchIntent.setClass(context, MainActivity.class);
        launchIntent.setAction(Consts.ACTION_SHOW_RECENT);
        // The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//// Adds the back stack for the Intent (but not the Intent itself)
//        stackBuilder.addParentStack(MainActivity.class);
//// Adds the Intent that starts the Activity to the top of the stack
//        stackBuilder.addNextIntent(launchIntent);

        PendingIntent resultPendingIntent=PendingIntent.getActivity(context, PI_REQUEST_CODE, launchIntent , PendingIntent.FLAG_UPDATE_CURRENT);

//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(
//                        PI_REQUEST_CODE,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );

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

    public void FireNotification(int id, NotificationCompat.Builder builder)
    {
        FireNotification(id, builder, true, true, true);

    }

    public void Cancel(int id)
    {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(id);
    }

    private boolean isNotificationVisible()
    {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent test = PendingIntent.getActivity(context,PI_REQUEST_CODE, notificationIntent, PendingIntent.FLAG_NO_CREATE);
        return test != null;
    }


}
