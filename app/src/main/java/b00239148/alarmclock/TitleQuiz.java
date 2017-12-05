package b00239148.alarmclock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by dennisalt on 10/11/2017.
 */

public class TitleQuiz extends SongQuiz {
    private ArrayList<String> answers = new ArrayList<String>();       //Could be titles or links to song covers??

    TitleQuiz(){
        super.songTitle = "Which is the right song title?";
    }

    TitleQuiz(MySpotify spotify){
        mySpotify = spotify;
        readAnswers();
    }


    //TODO Implement methods
    @Override
    protected void readAnswers() {
        List<PlaylistTrack> AllTracks = mySpotify.getAllTracks();
        String[] tempString = new String[2];
        String tempNewTrack = "";
        ArrayList<String> tempTracks = new ArrayList<String>();


        for (int i = 0; i<AllTracks.size(); i++){
            if(AllTracks.get(i).track.name != mySpotify.getCurrentSong().name){
                tempTracks.add(AllTracks.get(i).track.name);
            }
        }

        Collections.shuffle(tempTracks);

        answers.add(mySpotify.getCurrentSong().name);

        for (int i = 0; i< 3; i++){
            answers.add(tempTracks.get(i));
        }




        /*answers.add(new String[] {mySpotify.getCurrentSong().name, "true"});
        for (int i=0; i<3;i++){
            tempNewTrack = AllTracks.get((int)Math.floor((Math.random()*AllTracks.size()))).track.name;

            for (int j=0; j<answers.size(); j++){
                if (tempNewTrack == answers.get(j)[0]){             //check if the titke is already in the answers
                    tempNewTrack = AllTracks.get((int)Math.floor((Math.random()*AllTracks.size()))).track.name;
                    j=0;
                    continue;
                }
            }
            answers.add(new String[] {tempNewTrack, "false"});
        }*/
    }

    @Override
    public ArrayList<String> getAnswers() {
        return answers;
    }
}
