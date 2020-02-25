package com.ombd.OMDBDemo.service;

import com.ombd.OMDBDemo.dao.MovieRepository;
import com.ombd.OMDBDemo.entity.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;


@Service
public class MovieServiceImpl implements MovieService {

    private final Logger LOGGER = LoggerFactory.getLogger(MovieServiceImpl.class);

    @Value("${OMDB.key}")
    private String key;

    @Value("${OMDB.url}")
    private String url;

    private RestTemplate restTemplate;

    private MovieRepository movieRepository;


    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, RestTemplate restTemplate){
        this.restTemplate = restTemplate;
        this.movieRepository = movieRepository;
    }


    @Override
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    @Override
    public List<Movie> findFavorites() {
        return movieRepository.findByIsFavorite(true);
    }

    @Override
    public List<Movie> findWatchList() {
        return movieRepository.findByIsWatchList(true);
    }

    @Override
    public Movie findByTitle(String title) {
        return movieRepository.findByTitle(title);
    }

    @Override
    public Movie findByImdbId(String imdbId) {
        return movieRepository.findByImdbId(imdbId);
    }

    @Override
    public Movie search(String searchString, String searchType) {
        Movie theMovie;

        if(searchType.equals("title")) {
            if ((theMovie = findByTitle(searchString)) == null) { // Use movie object in database if present
                theMovie = searchNewMovieTitle(searchString);     // Get Movie from OMDB api otherwise
            }
        }
        else{
            if((theMovie =  findByImdbId(searchString)) == null) { // Use movie object in database if present
                theMovie = searchNewImdbId(searchString);        // Get Movie from OMDB api otherwise
            }
        }
        return theMovie;
    }

    @Override
    public Movie searchNewImdbId(String imdbId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                                                            .queryParam("i", imdbId)
                                                            .queryParam("apikey", key);

        ResponseEntity<Movie> response = restTemplate.getForEntity(builder.toUriString(), Movie.class);

        Movie theMovie = response.getBody();

        if(theMovie.getTitle() == null || theMovie.getPoster().equals("N/A")){
            return null;
        }
        return theMovie;
    }

    @Override
    public Movie searchNewMovieTitle(String movieTitle) {
        try {
            movieTitle = URLEncoder
                    .encode(movieTitle, StandardCharsets.UTF_8.toString()) // Encode to support spaces in title
                    .replace("%3A", ":")
                    .replace("%27", "'");
            LOGGER.info(movieTitle);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                                        .queryParam("t", movieTitle)
                                        .queryParam("apikey", key);

        LOGGER.info(builder.toUriString());

        ResponseEntity<Movie> response = restTemplate.getForEntity(builder.toUriString(), Movie.class);

        Movie theMovie = response.getBody();

        if(theMovie.getTitle() == null || theMovie.getPoster().equals("N/A")){
            return null;
        }
        return theMovie;

    }

    @Override
    public boolean save(Movie theMovie, String flag) {
        Movie tempMovie = findByTitle(theMovie.getTitle()); // Check if movie object is in database
        theMovie = tempMovie != null ? tempMovie : theMovie; // if movie in database, use db object. Use given movie object otherwise
        if(flag.equals("favorites") && !theMovie.isFavorite()){
            theMovie.setFavorite(true);
        }
        else if(flag.equals("watch-list") && !theMovie.isWatchList()){
            theMovie.setWatchList(true);
        }
        else if(findByTitle(theMovie.getTitle()) != null){
            return false;
        }
        movieRepository.save(theMovie);
        return true;
    }

    @Override
    public void remove(String id, String flag) {
        Optional<Movie> result = movieRepository.findById(id);
        Movie tempMovie = result.get();
        if(flag.equals("collection")){
            movieRepository.delete(tempMovie);
        }else{
            if(flag.equals("favorites")){
                tempMovie.setFavorite(false);
            }
            else if(flag.equals("watch-list")) {
                tempMovie.setWatchList(false);
            }
            movieRepository.save(tempMovie);
        }
    }

}