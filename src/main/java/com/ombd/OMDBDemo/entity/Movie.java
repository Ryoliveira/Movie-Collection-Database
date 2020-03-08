package com.ombd.OMDBDemo.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;


@NoArgsConstructor
@Getter
@Setter
@ToString
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

    public String getImdbLink() {
        return "https://www.imdb.com/title/" + imdbId;
    }
}
