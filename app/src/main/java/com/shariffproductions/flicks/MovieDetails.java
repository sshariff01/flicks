package com.shariffproductions.flicks;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieDetails implements Parcelable {
    public int id;
    public String title;
    public String overview;
    public String posterImageUrl;
    public String backdropImageUrl;
    public String trailerUrlKey;

    public MovieDetails(int id, String title, String overview, String posterImageFilePath, String backdropImageFilePath) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterImageUrl = assembleImageUrl("/w342", posterImageFilePath);
        this.backdropImageUrl = assembleImageUrl("/w1280", backdropImageFilePath);
    }

    protected MovieDetails(Parcel in) {
        id = in.readInt();
        title = in.readString();
        overview = in.readString();
        posterImageUrl = in.readString();
        backdropImageUrl = in.readString();
        trailerUrlKey = in.readString();
    }

    public static final Creator<MovieDetails> CREATOR = new Creator<MovieDetails>() {
        @Override
        public MovieDetails createFromParcel(Parcel in) {
            return new MovieDetails(in);
        }

        @Override
        public MovieDetails[] newArray(int size) {
            return new MovieDetails[size];
        }
    };

    private String assembleImageUrl(String imageSize, String filePath) {
        return "http://image.tmdb.org/t/p" + imageSize + filePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(posterImageUrl);
        parcel.writeString(backdropImageUrl);
        parcel.writeString(trailerUrlKey);
    }
}
