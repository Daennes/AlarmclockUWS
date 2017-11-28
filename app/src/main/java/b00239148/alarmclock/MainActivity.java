package b00239148.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;


import com.spotify.sdk.android.player.Spotify;

//Test Commit by Dennis

public class MainActivity extends AppCompatActivity {

    // alarm manager
    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    TextView update_text;
    Context context;
    PendingIntent pendingIntent;

    //MySpotify spotify = new MySpotify();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context = this;

        //spotify.spotifyConnect(this);

        // initialize alarm manager
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // initialize timepicker
        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);

        // initialize text update box
        update_text = (TextView) findViewById(R.id.update_text);

        // create instance of calender
        final Calendar calendar = Calendar.getInstance();

        // create an intent to the alarm reciever class
        final Intent my_intent = new Intent(this.context, Alarm_Receiver.class);

        // initialize start buttons
        Button alarm_on = (Button) findViewById(R.id.alarm_on);
        // create an onclick listener to start alarm

        //create Button to Quiz //for manually enter the quiz section
        Button toQuiz = (Button) findViewById(R.id.toQuizID);



        alarm_on.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                // setting calendar instance with the hour and minute thats been picked on timepicker
                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());

                //get string values of hour and minute
                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();

                // convert int values to strings
                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                if (hour > 12) {
                    hour_string = String.valueOf(hour - 12);
                }

                if (minute < 10) {
                    //10:8 --> 10:08
                    minute_string = "0" + String.valueOf(minute);
                }


                //methods that changes the update text
                set_alarm_text("Alarm set to: " + hour_string + ":" + minute_string);

                //put in extra string into my_intent
                //tell the clock that you pressed the "alarm on" button
                my_intent.putExtra("extra", "alarm on");

                //create a pending intent that delays the intent
                //until the specified calender time
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
                        my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                //set the alarm manager
                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        pendingIntent);

            }
        });




        // initialize stop button
        Button alarm_off = (Button) findViewById(R.id.alarm_off);
        // create an onclick lister to stop alarm or undo alarm set


        alarm_off.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //methods that changes the update text
                set_alarm_text("Alarm off!");


                //cancel alarm
                //check if pending alarm is already initialized
                if(pendingIntent != null)
                    alarm_manager.cancel(pendingIntent);

                //put extra string into my_intent
                //tell clock that "alarm off" has been pressed
                my_intent.putExtra("extra", "alarm off");

                //stop the alarm ringtone
                sendBroadcast(my_intent);


            }
        });

        //for manually enter the quiz section
        toQuiz.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToQuiz(v);
            }
        });


    }

    //for manually enter the quiz section
    public void changeToQuiz(View view){
        Intent intent = new Intent(this, QuizActivity.class);

        //Test data exchange between Activities
        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);
        //intent.putExtra("spotifyObject", spotify);
        intent.putExtra("hour", String.valueOf(alarm_timepicker.getHour()));
        intent.putExtra("min", String.valueOf(alarm_timepicker.getMinute()));
        //-------------------------------------


        startActivity(intent);
    }

    private void set_alarm_text(String output) {
        update_text.setText(output);
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
}
