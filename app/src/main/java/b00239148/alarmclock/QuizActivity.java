package b00239148.alarmclock;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class QuizActivity extends AppCompatActivity {

    MySpotify spotify = MainActivity.getSpotify();

    SongQuiz Quiz;
    Context cntx = this;
    private int rightAnswer = 0;
    private int tries = 0;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //Test data exchange between Activities
        Intent test_intent = getIntent();

        //-------------------------------------

        ((TextView) findViewById(R.id.textViewTries_id)).setText("Tries: " + tries);


        spotify.setOrChangePlaylist(test_intent.getStringExtra("playlistName"));

        Button submitBtn = (Button) findViewById(R.id.submitBtn_id);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup_id);

                ((TextView) findViewById(R.id.textViewTries_id)).setText("Tries: " + ++tries);

                if(((RadioButton) radioGroup.getChildAt(rightAnswer)).isChecked()){
                    Toast.makeText(getApplicationContext(), "Hooray!!!", Toast.LENGTH_SHORT).show();

                    //Stop Music!
                    spotify.stopMusic();
                    toWeather();
                }
                else if(radioGroup.getCheckedRadioButtonId() != -1){
                    Toast.makeText(getApplicationContext(), "Wrong answer", Toast.LENGTH_SHORT).show();
                    if(!spotify.playNextSong()) {
                        Toast.makeText(getApplicationContext(), "PlaylistEnded!!!", Toast.LENGTH_SHORT).show();
                        spotify.stopMusic();
                        toWeather();
                    }
                    loadQuizData();
                    Log.d("TAG","wrong");
                }else{
                    Toast.makeText(getApplicationContext(), "Please choose an answer!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        try {
            if(!spotify.playNextSong()) {
                startActivity(new Intent(this,MainActivity.class));
                Toast.makeText(getApplicationContext(), "Playlist not found or empty!!!", Toast.LENGTH_SHORT).show();
            } else {
                loadQuizData();
            }
        }
        catch (Exception e){
            Log.d("Error","Error Loading Quiz");
            Log.d("Error",e.getMessage());
        }


    }

    private void toWeather(){
        Intent intent = new Intent(this, WeatherActivity.class);

        startActivity(intent);
    }

    private void loadQuizData(){

        //Here some random function to decide which kind of quiz
        if(Math.floor((Math.random()*2)) == 0)
            loadTitleQuiz();
        else
            loadCoverQuiz();

    }

    //Not used anymore----------------------
    public void changeToSettings(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    //--------------------------------------

    private void findbyIDs(){
        //But all findsbyIds here:
    }

    private void loadCoverQuiz(){
        CoverQuiz cQuiz = new CoverQuiz(spotify);

        ((TextView) findViewById(R.id.questionText)).setText(R.string.question_Cover);
        new LoadImages().execute(cQuiz);


    }


    private void loadTitleQuiz(){

        Quiz = new TitleQuiz(spotify);


        ((TextView) findViewById(R.id.questionText)).setText(R.string.question_title);

        ArrayList<String> tempAnswers = Quiz.getAnswers();
        Collections.shuffle(tempAnswers);

        for (int i= 0; i<tempAnswers.size(); i++){
            if (tempAnswers.get(i) == spotify.getCurrentSong().name){
                rightAnswer = i;
            }
        }

        //Get RadioButtons---------------------
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup_id);
        Log.d("TAG","gefunden: "+radioGroup.getChildCount() );

        int tempChildCount = radioGroup.getChildCount();
        for (int i=0; i<tempChildCount; i++)
        {
            radioGroup.removeViewAt(0);
        }


        try {
            for (int i=0; i < tempAnswers.size(); i++) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(tempAnswers.get(i));
                radioButton.setBackgroundResource(R.drawable.rbbackground);
                radioButton.setId((int) i+1);//set radiobutton id and store it somewhere
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 50, 0, 15);
                radioButton.setPadding(15,30,15,30);
                //radioButton.setHeight(200);
                radioButton.setTextSize(20);
                radioButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                radioButton.setWidth(1000);
                radioButton.setButtonDrawable(android.R.color.transparent);
                radioGroup.addView(radioButton, params);
            }
        }
        catch (Exception e){
            Log.d("Error", e.getMessage());
        }

        radioGroup.clearCheck();



    }

    private class LoadImages extends AsyncTask<CoverQuiz, Void, ArrayList<Drawable>> {

        Drawable draw;

        private Exception exception;

        @Override
        protected ArrayList<Drawable> doInBackground(CoverQuiz... coverQuizs) {
            return coverQuizs[0].getAnswers();
        }

        protected void onPostExecute(ArrayList<Drawable> draw) {
            // TODO: check this.exception
            // TODO: do something with the feed
            //testDrwa = draw;
            //((RadioButton) findViewById(R.id.radioButton_id)).setCompoundDrawablesWithIntrinsicBounds(draw, null,null, null);

            //Get RadioButtons---------------------
            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup_id);
            Log.d("TAG","gefunden: "+radioGroup.getChildCount() );

            int tempChildCount = radioGroup.getChildCount();
            for (int i=0; i<tempChildCount; i++)
            {
                radioGroup.removeViewAt(0);
            }


            Drawable rightAnswers = draw.get(0);
            Collections.shuffle(draw);

            for (int i=0; i<draw.size(); i++){
                if(draw.get(i) == rightAnswers)
                    rightAnswer = i;
            }


            try {
                for (int i=0; i < draw.size(); i++) {
                    RadioButton radioButton = new RadioButton(cntx);
                    radioButton.setBackgroundResource(R.drawable.rbbackground);
                    radioButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null,draw.get(i),null,null);
                    radioButton.setId((int) i+1);//set radiobutton id and store it somewhere
                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins((radioGroup.getWidth()-draw.get(i).getMinimumWidth()-20)/2, 10, (radioGroup.getWidth()-draw.get(i).getMinimumWidth()-20)/2, 10);
                    //params.setMargins((radioGroup.getWidth()-draw.get(i).getMinimumWidth()-20)/2, 10, (radioGroup.getWidth()-draw.get(i).getMinimumWidth()-20)/2, 10);


                    radioButton.setWidth(draw.get(i).getMinimumWidth()+20);
                    radioButton.setHeight(draw.get(i).getMinimumHeight() + 20);
                    radioButton.setPadding(10,10,10,10);
                    radioButton.setButtonDrawable(android.R.color.transparent);
                    radioGroup.addView(radioButton, params);
                    Log.d("RadiogroupWidth", String.valueOf(radioGroup.getWeightSum()));
                }
            }
            catch (Exception e){
                Log.d("Error", e.getMessage());
            }

            radioGroup.clearCheck();

        }


    }

}
