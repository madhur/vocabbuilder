package in.co.madhur.vocabbuilder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import in.co.madhur.vocabbuilder.service.Alarms;

/**
 * Created by madhur on 21-Jun-14.
 */
public class BootReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Alarms alarms=new Alarms(context);

        if(alarms.ShouldSchedule())
            alarms.Schedule();
    }
}
