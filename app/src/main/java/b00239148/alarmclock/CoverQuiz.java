package b00239148.alarmclock;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by dennisalt on 12/11/2017.
 */

public class CoverQuiz extends SongQuiz {

    CoverQuiz(MySpotify spotify){
        mySpotify = spotify;
        super.songTitle = "Which is the right song cover?";
    }

    @Override
    protected void readAnswers() {

    }

    @Override
    public ArrayList<Drawable> getAnswers() {

        List<PlaylistTrack> AllTracks = mySpotify.getAllTracks();
        ArrayList<Track> tempTracks = new ArrayList<Track>();
        InputStream is = null;
        ArrayList<Drawable> d = new ArrayList<Drawable>();



        for (int i = 0; i<AllTracks.size(); i++){
            if(AllTracks.get(i).track.name != mySpotify.getCurrentSong().name){
                tempTracks.add(AllTracks.get(i).track);
            }
        }

        Collections.shuffle(tempTracks);


        try {
            is = (InputStream) new URL(mySpotify.getCurrentSong().album.images.get(0).url).getContent();
            d.add(Drawable.createFromStream(is, "src name"));
            for (int i = 0; i<2; i++){
                is = (InputStream) new URL(tempTracks.get(i).album.images.get(0).url).getContent();
                d.add(Drawable.createFromStream(is, "src name"));
            }

            return d;
        }catch (Exception e){
            return null;
        }


    }


}
