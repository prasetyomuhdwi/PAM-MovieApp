package net.prasetyomuhdwi.movieapps;

public class MoviesData {
    private final String title;
    private final String overview;
    private final String releaseDate;
    private final String poster_path;
    private final String backdrop_path;
    private final Double rating;
    private final String[] genres;

    public MoviesData(String title, String overview, String releaseDate, String poster_path, String backdrop_path, Double rating, String[] genres) {
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.rating = rating;
        this.genres = genres;
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

    public String[] getGenres() {
        return genres;
    }
}
