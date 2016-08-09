package com.shariffproductions.flicks;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends Activity {
    private ArrayList<MovieDetails> movieDetailsList;
    private MovieDetailsAdapter movieDetailsAdapter;
    private SwipeRefreshLayout swipeContainer;
    private final static String savedInstanceKey_movieDetailsList = "movieDetailsList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchNowPlayingMovieDetails();
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright);


        movieDetailsList = initializeMovieDetailsList(savedInstanceState);
        movieDetailsAdapter = new MovieDetailsAdapter(this, movieDetailsList);
        movieDetailsAdapter.setNotifyOnChange(true);

        ListView listView = (ListView) findViewById(R.id.movie_listings);
        listView.setAdapter(movieDetailsAdapter);

        populateMovieListings();
    }

    private ArrayList<MovieDetails> initializeMovieDetailsList(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return new ArrayList<>();
        } else {
            return savedInstanceState.getParcelableArrayList(savedInstanceKey_movieDetailsList);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList(savedInstanceKey_movieDetailsList, movieDetailsList);
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
                                movie.getInt("id"),
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
                Toast.makeText(MainActivity.this, "Failed to load movie listing data", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void fetchNowPlayingMovieDetails() {
        HttpClient httpClient = new HttpClient();
        httpClient.getNowPlayingMovies(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                movieDetailsAdapter.clear();

                try {
                    JSONArray results = (JSONArray) response.get("results");
                    JSONObject movie;
                    MovieDetails movieDetails;
                    for (int i = 0; i < results.length(); i++) {
                        movie = results.getJSONObject(i);
                        movieDetails = new MovieDetails(
                                movie.getInt("id"),
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

                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(MainActivity.this, "Failed to reload movie listing data", Toast.LENGTH_LONG).show();
            }
        });
    }

}
