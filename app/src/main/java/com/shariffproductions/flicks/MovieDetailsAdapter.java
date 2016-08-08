package com.shariffproductions.flicks;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetailsAdapter extends ArrayAdapter<MovieDetails> {
    private static class MovieDetailsHolder {
        TextView title;
        TextView overview;
        ImageView posterImage;
        ImageView backdropImage;
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
            movieDetailsHolder.backdropImage = (ImageView) convertView.findViewById(R.id.backdrop_image);
            convertView.setTag(movieDetailsHolder);
        } else {
            movieDetailsHolder = (MovieDetailsHolder) convertView.getTag();
        }

        movieDetailsHolder.title.setText(movieDetails.title);
        movieDetailsHolder.overview.setText(movieDetails.overview);
        if (isOrientationPortrait()) {
            Picasso.with(getContext()).load(movieDetails.posterImageUrl).into(movieDetailsHolder.posterImage);
        } else {
            Picasso.with(getContext()).load(movieDetails.backdropImageUrl).into(movieDetailsHolder.backdropImage);
        }
        prepareYoutubeTrailer(movieDetails, movieDetailsHolder);

        return convertView;
    }

    private boolean isOrientationPortrait() {
        return this.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    private void prepareYoutubeTrailer(final MovieDetails movieDetails, MovieDetailsHolder movieDetailsHolder) {
        if (isOrientationPortrait()) {
            movieDetailsHolder.posterImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchMovieTrailerActivity(movieDetails);
                }
            });
        } else {
            movieDetailsHolder.backdropImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchMovieTrailerActivity(movieDetails);
                }
            });
        }
    }

    private void launchMovieTrailerActivity(MovieDetails movieDetails) {
        Intent intent = new Intent(getContext(), MovieTrailerActivity.class);
        intent.putExtra("movieId", movieDetails.id);
        getContext().startActivity(intent);
    }
}
