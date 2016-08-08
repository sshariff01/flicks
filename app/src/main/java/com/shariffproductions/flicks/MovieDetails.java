package com.shariffproductions.flicks;

public class MovieDetails {
    public String title;
    public String overview;
    public String posterImageUrl;

    public MovieDetails(String title, String overview, String posterImageFilePath) {
        this.title = title;
        this.overview = overview;
        this.posterImageUrl = assembleImageUrl("/w342", posterImageFilePath);
    }

    private String assembleImageUrl(String imageSize, String filePath) {
        return "http://image.tmdb.org/t/p" + imageSize + filePath;
    }
}
