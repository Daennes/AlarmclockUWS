package b00239148.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by alantsang on 06/11/2017.
 */

public class Alarm_Receiver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("We are in the receiver.", "ok!");

        String get_your_string = intent.getExtras().getString("extra");

        Log.e("what is the key? ", get_your_string);

        context.sendBroadcast(new Intent("ALARM IS ON"));


    }
}
