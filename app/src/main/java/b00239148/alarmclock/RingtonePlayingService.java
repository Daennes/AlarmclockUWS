package b00239148.alarmclock;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by alantsang on 06/11/2017.
 */

public class RingtonePlayingService extends Service {

    MediaPlayer media_song;
    int startId;
    boolean isRunning;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Recieved start id " + startId + ": " + intent);

        //fetch extra string values
        String state = intent.getExtras().getString("extra");

        Log.e("ringtone extra is ", state);

        //this converts extra strings from the intent to start IDs, values 0 or 1
        assert state != null;
        switch (state) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                Log.e("start ID is ", state);
                break;
            default:
                startId = 0;
                break;
        }



        // if else statements

        //if there is no music playing and the user pressed "alarm on"
        //music should start playing
        if (!this.isRunning && startId == 1) {
            Log.e("there is no music ", "and you want to start");

            //create an instance of media player
            media_song = MediaPlayer.create(this, R.raw.analog);
            //start ringtone
            media_song.start();

            this.isRunning = true;
            this.startId = 0;


        }

        //if there is music playing and user pressed "alarm off"
        //music should stop playing
        else if (this.isRunning && startId == 0) {
            Log.e("there is music, ", "and you want to end");

            //stop ringtone
            media_song.stop();
            media_song.reset();

            this.isRunning = false;
            this.startId = 0;

        }

        //these are if the user presses random buttons
        //just to bug proof app
        //if there is no music playing and user pressed "alarm off"
        //do nothing
        else if (!this.isRunning && startId == 0) {
            Log.e("there is no music, ", "and you want to end");

            this.isRunning = false;
            this.startId = 0;

        }

        //if there is music playing and the user pressed "alarm on"
        //do nothing
        else if (this.isRunning && startId == 1) {
            Log.e("there is music, ", "and you want to start");

            this.isRunning = true;
            this.startId = 1;

        }

        else {
            Log.e("else ", "end reach");

        }




        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        //tell the user we stopped
        Toast.makeText(this, "on Destroy called", Toast.LENGTH_SHORT).show();
    }

}
