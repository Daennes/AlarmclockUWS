package b00239148.alarmclock;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.PlaylistTrack;

/**
 * Created by dennisalt on 12/11/2017.
 */

public class CoverQuiz extends SongQuiz {


    private List<Drawable> answers;       //Could be titles or links to song covers??

    Drawable testDrwa;

    CoverQuiz(MySpotify spotify){
        mySpotify = spotify;
        super.songTitle = "Which is the right song cover?";
    }

    /*CoverQuiz(String title){
        readAnswers();
    }*/

    public void fillDummyAnsw()
    {

    }


    //TODO Implement methods cover

    @Override
    protected void readAnswers() {

    }

    @Override
    public ArrayList<Drawable> getAnswers() {

        List<PlaylistTrack> AllTracks = mySpotify.getAllTracks();
        InputStream is = null;
        ArrayList<Drawable> d = new ArrayList<Drawable>();
        Drawable tempDraw = null;
        String[] urls = new String[3];
        String tempUrl = "";

        try {
            urls[0] = mySpotify.getCurrentSong().album.images.get(0).url;
            is = (InputStream) new URL(mySpotify.getCurrentSong().album.images.get(0).url).getContent();
            d.add(Drawable.createFromStream(is, "src name"));
            for (int i=0; i<2;i++){

                tempUrl = AllTracks.get((int)Math.floor((Math.random()*AllTracks.size()))).track.album.images.get(0).url;

                for (int j=0; j<urls.length; j++){
                    if (tempUrl == urls[j]){             //check if the titke is already in the answers
                        tempUrl = AllTracks.get((int)Math.floor((Math.random()*AllTracks.size()))).track.album.images.get(0).url;
                        j=0;
                        continue;
                    }
                }

                urls[i+1] = tempUrl;
                is = (InputStream) new URL(tempUrl).getContent();
                d.add(Drawable.createFromStream(is, "src name"));


            }

            return d;
        }catch (Exception e){
            return null;
        }


    }

    public Drawable getCover(){
        List<PlaylistTrack> AllTracks = mySpotify.getAllTracks();

        try {
            InputStream is = (InputStream) new URL(mySpotify.getCurrentSong().album.images.get(0).url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        }catch (Exception e){
            return null;
        }



    }


    private class LoadImages extends AsyncTask<String, Void, Drawable> {

        private Exception exception;

        @Override
        protected Drawable doInBackground(String... urls) {
            try {
                //URL url = new URL(urls);
                InputStream is = (InputStream) new URL(urls[0]).getContent();
                Drawable d = Drawable.createFromStream(is, "src name");
                return d;
            } catch (Exception e) {
                this.exception = e;

                return null;
            } finally {

            }
        }

        protected void onPostExecute(Drawable draw) {
            // TODO: check this.exception
            // TODO: do something with the feed
            testDrwa = draw;
        }
    }
}
