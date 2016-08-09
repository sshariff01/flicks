package com.shariffproductions.flicks;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MovieTrailerActivity extends YouTubeBaseActivity {
    YouTubePlayer mYouTubePlayer;
    int movieId;
    String movieTrailerUrlKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);
        movieId = getIntent().getIntExtra("movieId", 550);
        playYoutubeTrailer();
    }

    private void playYoutubeTrailer() {
        if (movieTrailerUrlIsDefined()) {
            playVideo(movieTrailerUrlKey);
            return;
        }

        HttpClient httpClient = HttpClient.getClient();
        httpClient.getYoutubeTrailer(movieId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                movieTrailerUrlKey = parseYoutubeTrailerUrl(response);
                if (movieTrailerUrlIsDefined()) {
                    playVideo(movieTrailerUrlKey);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(MovieTrailerActivity.this, "Failed to fetch YouTube trailer", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean movieTrailerUrlIsDefined() {
        return movieTrailerUrlKey != null && !movieTrailerUrlKey.isEmpty();
    }

    private String parseYoutubeTrailerUrl(JSONObject response) {
        try {
            JSONArray results = (JSONArray) response.get("results");
            JSONObject movie = results.getJSONObject(0);
            return movie.getString("key");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void playVideo(final String videoKey) {
        final YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.player);

        youTubePlayerView.initialize(
                "AIzaSyC1z_P1WHqdsizA6DEaA8qGmHa7kyCR6yk",
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        final YouTubePlayer youTubePlayer, boolean b) {
                        mYouTubePlayer = youTubePlayer;
                        mYouTubePlayer.loadVideo(videoKey);
                        mYouTubePlayer.setFullscreen(true);
                        mYouTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                            @Override
                            public void onFullscreen(boolean isFullscreen) {
                                if (!isFullscreen) {
                                    youTubePlayer.release();
                                    destroyActivity();
                                }
                            }
                        });
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                }
        );
    }

    private void destroyActivity() {
        finish();
    }
}
