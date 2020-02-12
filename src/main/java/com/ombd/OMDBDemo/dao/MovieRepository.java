package com.ombd.OMDBDemo.dao;

import com.ombd.OMDBDemo.entity.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieRepository extends MongoRepository<Movie, String> {

    public Movie findByTitle(String title);

}
