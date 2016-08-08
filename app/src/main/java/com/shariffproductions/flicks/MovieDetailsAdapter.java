package com.shariffproductions.flicks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieDetailsAdapter extends ArrayAdapter<MovieDetails> {
    private static final int PORTRAIT = 1;

    private static class MovieDetailsHolder {
        TextView title;
        TextView overview;
        ImageView posterImage;
        ImageButton backdropImage;
    }

    public MovieDetailsAdapter(Context context, ArrayList<MovieDetails> movieDetailsList) {
        super(context, R.layout.item_movie_details, movieDetailsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieDetails movieDetails = getItem(position);

        final MovieDetailsHolder movieDetailsHolder;
        if (convertView == null) {
            movieDetailsHolder = new MovieDetailsHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie_details, parent, false);
            movieDetailsHolder.title = (TextView) convertView.findViewById(R.id.movie_title);
            movieDetailsHolder.overview = (TextView) convertView.findViewById(R.id.movie_overview);
            movieDetailsHolder.posterImage = (ImageView) convertView.findViewById(R.id.poster_image);
            movieDetailsHolder.backdropImage = (ImageButton) convertView.findViewById(R.id.backdrop_image);
            convertView.setTag(movieDetailsHolder);
        } else {
            movieDetailsHolder = (MovieDetailsHolder) convertView.getTag();
        }

        movieDetailsHolder.title.setText(movieDetails.title);
        movieDetailsHolder.overview.setText(movieDetails.overview);
        if (this.getContext().getResources().getConfiguration().orientation == PORTRAIT) {
            Picasso.with(getContext()).load(movieDetails.posterImageUrl).into(movieDetailsHolder.posterImage);
        } else {
            Picasso.with(getContext()).load(movieDetails.backdropImageUrl).into(movieDetailsHolder.backdropImage);
            initializeYoutubePlayer(convertView, movieDetails, movieDetailsHolder);
        }

        return convertView;
    }

    private void initializeYoutubePlayer(View convertView, final MovieDetails movieDetails, MovieDetailsHolder movieDetailsHolder) {
        final YouTubePlayerView youTubePlayerView = (YouTubePlayerView) convertView.findViewById(R.id.player);
        movieDetailsHolder.backdropImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadYoutubeTrailer(youTubePlayerView, movieDetails);
            }
        });
    }

    private void loadYoutubeTrailer(final YouTubePlayerView youTubePlayerView, final MovieDetails movieDetails) {
        if (movieDetails.trailerUrl != null && !movieDetails.trailerUrl.isEmpty()) {
            playYoutubeTrailer(movieDetails, youTubePlayerView);
            return;
        }

        HttpClient httpClient = new HttpClient();
        httpClient.getYoutubeTrailer(movieDetails.id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                fetchYoutubeTrailer(response, movieDetails);
                if (movieDetails.trailerUrl != null && !movieDetails.trailerUrl.isEmpty()) {
                    playYoutubeTrailer(movieDetails, youTubePlayerView);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getContext(), "Failed to fetch YouTube trailer", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchYoutubeTrailer(JSONObject response, MovieDetails movieDetails) {
        try {
            JSONArray results = (JSONArray) response.get("results");
            JSONObject movie = results.getJSONObject(0);
            movieDetails.trailerUrl = movie.getString("key");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void playYoutubeTrailer(final MovieDetails movieDetails, YouTubePlayerView youTubePlayerView) {
        youTubePlayerView.initialize(
                "AIzaSyC1z_P1WHqdsizA6DEaA8qGmHa7kyCR6yk",
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        final YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.loadVideo(movieDetails.trailerUrl);
                        youTubePlayer.setFullscreen(true);
                        youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                            @Override
                            public void onFullscreen(boolean isFullscreen) {
                                if (!isFullscreen) {
                                    youTubePlayer.release();
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
}
