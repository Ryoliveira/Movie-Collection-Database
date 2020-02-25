package com.ombd.OMDBDemo.service;

import com.ombd.OMDBDemo.entity.Movie;

import java.util.List;

public interface MovieService {

    public List<Movie> findAll();

    public List<Movie> findFavorites();

    public List<Movie> findWatchList();

    public Movie findByTitle(String title);

    public Movie findByImdbId(String imdbId);

    public Movie search(String searchString, String searchType);

    public Movie searchNewImdbId(String imdbId);

    public Movie searchNewMovieTitle(String movieTitle);

    public boolean save(Movie theMovie, String flag);

    public void remove(String id, String flag);


}
