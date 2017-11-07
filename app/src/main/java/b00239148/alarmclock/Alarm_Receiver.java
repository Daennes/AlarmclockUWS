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

        //fetch extra strings from the intent
        String get_your_string = intent.getExtras().getString("extra");

        Log.e("what is the key? ", get_your_string);

        //create an intent to the ringtone service
        Intent service_intent = new Intent(context, RingtonePlayingService.class);

        //pass the extra string from the main activity to the ringtone playing service
        service_intent.putExtra("extra", get_your_string);

        //start ringtone service
        context.startService(service_intent);
    }
}
