package com.ombd.OMDBDemo.service;

import com.ombd.OMDBDemo.dao.MovieRepository;
import com.ombd.OMDBDemo.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {

    MovieRepository movieRepository;


    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }


    @Override
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    @Override
    public Movie findByTitle(String title) {
        return movieRepository.findByTitle(title);
    }

    @Override
    public Movie findById(String id) {
        Optional<Movie> result = movieRepository.findById(id);
        return result.get();
    }

    @Override
    public void save(Movie theMovie) {

        movieRepository.save(theMovie);

    }

    @Override
    public void delete(Movie theMovie) {
        movieRepository.delete(theMovie);
    }

}