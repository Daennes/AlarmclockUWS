package b00239148.alarmclock;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;

public class QuizActivity extends AppCompatActivity  implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback  {

    private static final String CLIENT_ID = "1c15dd3e98014e828bae9ddca23a2ac5";
    private static final String REDIRECT_URI = "my-first-android-app://callback";

    private static final int REQUEST_CODE = 1337;
    private static int currentSongIndex = -1;
    private static List<PlaylistTrack> allTracks;

    private Player mPlayer;
    private SpotifyService spotify;

    SongQuiz Quiz;
    int rightAnswer = 0;
    int tries = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        //Test data exchange between Activities
        Intent test_intent = getIntent();
        TextView textview = (TextView) findViewById(R.id.testTimeViewID);
        textview.setText("Picked Time: " + test_intent.getStringExtra("hour") + ":" + test_intent.getStringExtra("min"));
        //-------------------------------------

        ((TextView) findViewById(R.id.textViewTries_id)).setText("Tries: " + tries);


        Button submitBtn = (Button) findViewById(R.id.submitBtn_id);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup_id);

                if(((RadioButton) radioGroup.getChildAt(rightAnswer)).isChecked()){
                    Toast.makeText(getApplicationContext(), "Hooray!!!", Toast.LENGTH_SHORT).show();

                    //Stop Music!

                    onBackPressed();
                }
                else if(radioGroup.getCheckedRadioButtonId() != -1){
                    Toast.makeText(getApplicationContext(), "Wrong answer", Toast.LENGTH_SHORT).show();
                    loadQuizData();
                    Log.d("TAG","wrong");
                }else{
                    Toast.makeText(getApplicationContext(), "Please choose an answer!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        try {
            loadQuizData();
        }
        catch (Exception e){
            Log.d("Error","Error Loading Quiz");
            Log.d("Error",e.getMessage());
        }



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("QuizActivity", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {

            default:
                break;
        }
    }

    public List<PlaylistTrack> getAllTracks() {
        return allTracks;
    }

    public Track getCurrentSong() {
        return allTracks.get(currentSongIndex).track;
    }

    public void getPlaylist(final String playlistName) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Pager<PlaylistSimple> myPlaylists = spotify.getMyPlaylists();
                    for (PlaylistSimple playlistSimpleItem : myPlaylists.items) {
                        if (playlistSimpleItem.name.equals(playlistName)) {
                            Pager<PlaylistTrack> playlistTracks = spotify.getPlaylistTracks(playlistSimpleItem.owner.id, playlistSimpleItem.id);
                            allTracks = playlistTracks.items;
                            break;
                        }
                    }
                    if (allTracks == null) {
                        throw new Exception("No playlist found with a name: " + playlistName);
                    }
                    if (allTracks.size() == 0) {
                        throw new Exception("Playlist with a name " + playlistName + " is empty.");
                    }
                    Collections.shuffle(allTracks);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            Log.e("Error", e.getMessage());
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("QuizActivity", "Playback error received: " + error.name());
        switch (error) {
            default:
                break;
        }
    }

    public void playNextSong() {

        if(currentSongIndex<allTracks.size()-1) {
            currentSongIndex = currentSongIndex+1;
            mPlayer.playUri(null, allTracks.get(currentSongIndex).track.uri, 0, 0);
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("QuizActivity", "User logged in");

        //todo provide playlist name from options
        getPlaylist("My Shazam Tracks");
        playNextSong();

    }

    @Override
    public void onLoggedOut() {
        Log.d("QuizActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d("QuizActivity", "Login failed");
    }


    @Override
    public void onTemporaryError() {
        Log.d("QuizActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("QuizActivity", "Received connection message: " + message);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                SpotifyApi api = new SpotifyApi();
                api.setAccessToken(response.getAccessToken());
                spotify = api.getService();
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(QuizActivity.this);
                        mPlayer.addNotificationCallback(QuizActivity.this);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("QuizActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    private void loadQuizData(){

        //Here some random function to decide which kind of quiz
        if(Math.floor((Math.random()*2)) == 0)
            Quiz = new TitleQuiz();
        else
            Quiz = new CoverQuiz();


        //TODO make it compatible for both sub classes
        //Object[] answers;
        String[] answers;

        /*if (Quiz instanceof TitleQuiz){
            answers = new String[((String[]) Quiz.getAnswers()).length];
            for (int i=0; i < answers.length; i++){
                answers[i] = ((String[]) Quiz.getAnswers())[i];
            }
        }
        else if(Quiz.getAnswers() instanceof Drawable[]){                       //Subclass of SongQuiz
            answers = new Drawable[((Drawable[]) Quiz.getAnswers()).length];
            for (int i=0; i < answers.length; i++){
                answers[i] = ((Drawable[]) Quiz.getAnswers())[i];
            }
        }
        else{
            answers = null;
        }*/

        //Set question
        ((TextView) findViewById(R.id.questionText)).setText(R.string.question_title);

        answers = new String[((ArrayList<String[]>) Quiz.getAnswers()).size()];

        if(answers != null) {
            for (int i = 0; i < answers.length; i++) {
                answers[i] = (((ArrayList<String[]>) Quiz.getAnswers()).get(i))[0];

                //Save right answer
                if ((((ArrayList<String[]>) Quiz.getAnswers()).get(i))[1] == "true") {
                    Log.d("TAG", "gefunden");
                    rightAnswer = i;
                }
            }
        }else
            answers = new String[0];






        //Get RadioButtons---------------------
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup_id);
        Log.d("TAG","gefunden: "+radioGroup.getChildCount() );

        int tempChildCount = radioGroup.getChildCount();
        for (int i=0; i<tempChildCount; i++)
        {
            radioGroup.removeViewAt(0);
        }


        try {
            for (int i=0; i < answers.length; i++) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(answers[i]);
                radioButton.setId((int) i+1);//set radiobutton id and store it somewhere
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                radioGroup.addView(radioButton, params);
            }
        }
        catch (Exception e){
            Log.d("Error", e.getMessage());
        }

        radioGroup.clearCheck();

        ArrayList<RadioButton> radioButtonsList = new ArrayList<RadioButton>();

        for (int i=0;i<radioGroup.getChildCount();i++) {
            View rb = radioGroup.getChildAt(i);
            if (rb instanceof RadioButton) {
                radioButtonsList.add((RadioButton)rb);
            }
        }
        Log.d("TAG","you have "+radioButtonsList.size()+" radio buttons");


        //((RadioButton) radioButtonsList.toArray()[0]).setText("test");
        //--------------------------------------
        ((TextView) findViewById(R.id.textViewTries_id)).setText("Tries: " + ++tries);

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


}
