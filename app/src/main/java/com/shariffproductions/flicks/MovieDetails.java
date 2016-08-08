package com.shariffproductions.flicks;

public class MovieDetails {
    public String title;
    public String overview;
    public String posterImageUrl;
    public String backdropImageUrl;

    public MovieDetails(String title, String overview, String posterImageFilePath, String backdropImageFilePath) {
        this.title = title;
        this.overview = overview;
        this.posterImageUrl = assembleImageUrl("/w342", posterImageFilePath);
        this.backdropImageUrl = assembleImageUrl("/w1280", backdropImageFilePath);
    }

    private String assembleImageUrl(String imageSize, String filePath) {
        return "http://image.tmdb.org/t/p" + imageSize + filePath;
    }
}
