package in.co.madhur.vocabbuilder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import in.co.madhur.vocabbuilder.App;
import in.co.madhur.vocabbuilder.Consts;
import in.co.madhur.vocabbuilder.service.RemindWordService;

/**
 * Created by madhur on 21-Jun-14.
 */
public class AlarmReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent serviceIntent = new Intent();
        serviceIntent.setClass(context, RemindWordService.class);
        context.startService(serviceIntent);
    }
}
