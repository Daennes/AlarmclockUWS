package b00239148.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.spotify.sdk.android.player.Spotify;

import java.util.Calendar;

//Test Commit by Dennis

public class MainActivity extends AppCompatActivity {


    public static MySpotify getSpotify() {
        return spotify;
    }

    private static MySpotify spotify = new MySpotify();

    // alarm manager
    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    TextView update_text;
    Context context;
    PendingIntent pendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context = this;

        spotify.spotifyConnect(this);

        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm_timepicker = findViewById(R.id.timePicker);
        update_text = findViewById(R.id.update_text);
        final Calendar calendar = Calendar.getInstance();
        final Intent my_intent = new Intent(this.context, Alarm_Receiver.class);
        Button alarm_on = findViewById(R.id.alarm_on);

        registerReceiver(broadcastReceiver, new IntentFilter("ALARM IS ON"));

        alarm_on.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());

                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();

                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                if (hour > 12) {
                    hour_string = String.valueOf(hour - 12);
                }

                if (minute < 10) {
                    //10:8 --> 10:08
                    minute_string = "0" + String.valueOf(minute);
                }

                set_alarm_text("Alarm set to: " + hour_string + ":" + minute_string);

                my_intent.putExtra("extra", "alarm on");

                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
                        my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        pendingIntent);

            }
        });

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            changeToQuiz();
        }
    };

    public void changeToQuiz(){

        String playlistName = ((EditText)findViewById(R.id.playlistText_id)).getText().toString();
        Intent intent = new Intent(this, QuizActivity.class);

        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);
        intent.putExtra("hour", String.valueOf(alarm_timepicker.getHour()));
        intent.putExtra("min", String.valueOf(alarm_timepicker.getMinute()));
        intent.putExtra("playlistName", playlistName);

        startActivity(intent);
    }

    private void set_alarm_text(String output) {
        update_text.setText(output);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        spotify.spotifyLogin(requestCode, resultCode, intent, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(spotify);
        super.onDestroy();
    }
}
