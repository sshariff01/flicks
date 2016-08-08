package com.shariffproductions.flicks;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends Activity {
    ArrayList<MovieDetails> movieDetailsList;
    MovieDetailsAdapter movieDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieDetailsList = new ArrayList<>();
        movieDetailsAdapter = new MovieDetailsAdapter(this, movieDetailsList);
        movieDetailsAdapter.setNotifyOnChange(true);

        ListView listView = (ListView) findViewById(R.id.movie_listings);
        listView.setAdapter(movieDetailsAdapter);

        populateMovieListings();
    }

    private void populateMovieListings() {
        HttpClient httpClient = new HttpClient();
        httpClient.getNowPlayingMovies(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONArray results = (JSONArray) response.get("results");
                    JSONObject movie;
                    MovieDetails movieDetails;
                    for (int i = 0; i < results.length(); i++) {
                        movie = results.getJSONObject(i);
                        movieDetails = new MovieDetails(
                                movie.getString("title"),
                                movie.getString("overview"),
                                movie.getString("poster_path"),
                                movie.getString("backdrop_path")
                        );
                        movieDetailsAdapter.add(movieDetails);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
