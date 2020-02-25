package com.ombd.OMDBDemo.dao;

import com.ombd.OMDBDemo.entity.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MovieRepository extends MongoRepository<Movie, String> {

    public Movie findByTitle(String title);

    public Movie findByImdbId(String imdbId);

    public List<Movie> findByIsFavorite(boolean isFavorite);

    public List<Movie> findByIsWatchList(boolean isWatchList);

}
