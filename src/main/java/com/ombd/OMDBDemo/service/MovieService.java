package com.ombd.OMDBDemo.service;

import com.ombd.OMDBDemo.entity.Movie;

import java.util.List;

public interface MovieService {

    public List<Movie> findAll();

    public Movie findByTitle(String title);

    public Movie findById(String id);

    public void save(Movie theMovie);

    public void delete(Movie theMovie);


}
