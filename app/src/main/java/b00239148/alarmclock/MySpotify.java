package b00239148.alarmclock;

import android.content.Intent;
import android.util.Log;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by John on 2017-11-21.
 */

public class MySpotify implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback, Serializable {

    private static final String CLIENT_ID = "dc6a1b53b1cd459e818d062995fb2427";
    private static final String REDIRECT_URI = "my-first-android-app://callback";

    private static final int REQUEST_CODE = 1337;
    private static int currentSongIndex = -1;
    private static List<PlaylistTrack> allTracks;

    private Player mPlayer;
    private SpotifyService spotify;

    private boolean isAnyPlaylistActive = false;


    void spotifyConnect(MainActivity quizActivity) {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(quizActivity, REQUEST_CODE, request);
    }

    public List<PlaylistTrack> getAllTracks() {
        return allTracks;
    }


    public Track getCurrentSong() {
        return allTracks.get(currentSongIndex).track;
    }


    public void setOrChangePlaylist(final String playlistName) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Pager<PlaylistSimple> myPlaylists = spotify.getMyPlaylists();
                    for (PlaylistSimple playlistSimpleItem : myPlaylists.items) {
                        if (playlistSimpleItem.name.equals(playlistName)) {
                            Pager<PlaylistTrack> playlistTracks = spotify.getPlaylistTracks(playlistSimpleItem.owner.id, playlistSimpleItem.id);
                            allTracks = playlistTracks.items;
                            isAnyPlaylistActive = true;
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
                    isAnyPlaylistActive = false;
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

    public boolean playNextSong() {

        if (isAnyPlaylistActive && currentSongIndex < allTracks.size() - 1) {
            currentSongIndex = currentSongIndex + 1;
            mPlayer.playUri(null, allTracks.get(currentSongIndex).track.uri, 0, 0);
            return true;
        }

        return false;
    }

    public void stopMusic() {
        mPlayer.pause(new Player.OperationCallback() {
            @Override
            public void onSuccess() {
                Log.d("QuizActivity", "Music stopped successfully");
            }

            @Override
            public void onError(Error error) {

            }
        });
    }

    @Override
    public void onLoggedIn() {
        Log.d("QuizActivity", "User logged in");

        //todo provide playlist name from options
//        setOrChangePlaylist("My Shazam Tracks");
//        playNextSong();

    }

    void spotifyLogin(int requestCode, int resultCode, Intent intent, final MainActivity quizActivity) {
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(quizActivity, response.getAccessToken(), CLIENT_ID);
                SpotifyApi api = new SpotifyApi();
                api.setAccessToken(response.getAccessToken());
                spotify = api.getService();
                com.spotify.sdk.android.player.Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(MySpotify.this);
                        mPlayer.addNotificationCallback(MySpotify.this);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("QuizActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("QuizActivity", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {

            default:
                break;
        }
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
    public void onPlaybackError(Error error) {
        Log.d("QuizActivity", "Playback error received: " + error.name());
        switch (error) {
            default:
                break;
        }
    }
}
