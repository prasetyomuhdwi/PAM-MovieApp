package net.prasetyomuhdwi.movieapps;

public class MoviesData {
    private String title;
    private String overview;
    private String releaseDate;
    private String poster_path;
    private String backdrop_path;
    private Double rating;

    public MoviesData(String title, String overview, String releaseDate, String poster_path, String backdrop_path, Double rating) {
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public Double getRating() {
        return rating;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }
}
