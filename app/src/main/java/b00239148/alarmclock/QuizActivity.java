package b00239148.alarmclock;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //create Button to Quiz
        Button toQuiz = (Button) findViewById(R.id.toSettingsID);

        toQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToSettings(v);
            }
        });

        //Test data exchange between Activities
        Intent test_intent = getIntent();
        TextView textview = (TextView) findViewById(R.id.testTimeViewID);
        textview.setText("Picked Time: " + test_intent.getStringExtra("hour") + ":" + test_intent.getStringExtra("min"));
        //-------------------------------------


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    public void changeToSettings(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
