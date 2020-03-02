package com.ombd.OMDBDemo.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

public class Movie {

    @Id
    private String id;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Rated")
    private String rated;

    @JsonProperty("Year")
    private String year;

    @JsonProperty("Released")
    private String released;

    @JsonProperty("Runtime")
    private String runtime;

    @JsonProperty("Genre")
    private String genre;

    @JsonProperty("Plot")
    private String plot;

    @JsonProperty("Poster")
    private String poster;

    @JsonProperty("imdbID")
    private String imdbId;

    private boolean isFavorite;

    private boolean isWatchList;

    public Movie() {
    }

    public Movie(String title, String rated, String year, String released, String runtime, String genre, String plot, String poster, String imdbId) {
        this.title = title;
        this.rated = rated;
        this.year = year;
        this.released = released;
        this.runtime = runtime;
        this.genre = genre;
        this.plot = plot;
        this.poster = poster;
        this.imdbId = imdbId;
        this.isFavorite = false;
        this.isWatchList = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isWatchList() {
        return isWatchList;
    }

    public void setWatchList(boolean watchList) {
        isWatchList = watchList;
    }

    public String getImdbLink() {
        return "https://www.imdb.com/title/" + imdbId;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", rated='" + rated + '\'' +
                ", year='" + year + '\'' +
                ", released='" + released + '\'' +
                ", runtime='" + runtime + '\'' +
                ", genre='" + genre + '\'' +
                ", plot='" + plot + '\'' +
                ", poster='" + poster + '\'' +
                ", imdbId='" + imdbId + '\'' +
                '}';
    }
}
