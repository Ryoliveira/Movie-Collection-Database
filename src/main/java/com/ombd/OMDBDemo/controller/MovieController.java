package com.ombd.OMDBDemo.controller;

import com.ombd.OMDBDemo.entity.Movie;
import com.ombd.OMDBDemo.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/")
public class MovieController {

    private final Logger LOGGER = LoggerFactory.getLogger(MovieController.class);

    @Value("${OMDB.key}")
    private String key;

    private RestTemplate restTemplate;

    private MovieService movieService;

    @Autowired
    public MovieController(RestTemplate restTemplate, MovieService movieService){
        this.restTemplate = restTemplate;
        this.movieService = movieService;
    }


    @GetMapping("/")
    public String homePage(){
        return "redirect:/movies";
    }


    @GetMapping("/movies/search")
    public String getMovie(@RequestParam("title") String title, Model theModel){

        Movie theMovie = restTemplate.getForObject("http://www.omdbapi.com/?t=" + title + "&apikey=" + key, Movie.class);

        LOGGER.info(theMovie.toString());

        if(theMovie.getTitle() == null){
            return "redirect:/not-found";
        }

        theModel.addAttribute("movie", theMovie);

        return "display/display-movie";
    }


    @GetMapping("/movies")
    public String displayFavoritePage(Model theModel){

        List<Movie> movies = movieService.findAll();
        theModel.addAttribute("movies", movies);

        return "display/collection-list";
    }

    @GetMapping("/display-saved")
    public String getMovieByTitle(@RequestParam("id") String id, Model theModel){
        Movie theMovie = movieService.findById(id);
        theModel.addAttribute("movie", theMovie);
        return "display/display-movie";

    }

    @PostMapping("/movies")
    public String addMovie(@ModelAttribute("movie") Movie theMovie, RedirectAttributes attributes){
        if(movieService.findByTitle(theMovie.getTitle()) == null) {
            movieService.save(theMovie);
        }else{
            attributes.addAttribute("failure", "Movie already in collection");
            attributes.addFlashAttribute("movie", theMovie);
            return "redirect:/already-saved";
        }
        return "redirect:/movies";
    }

    @GetMapping("/delete")
    public String removeMovie(@RequestParam("id") String id){
        Movie theMovie = movieService.findById(id);
        movieService.delete(theMovie);

        return "redirect:/movies";
    }

    @GetMapping("/already-saved")
    public String alreadyFavorite(@ModelAttribute Movie theMovie,
                                  @RequestParam("failure") String fail,
                                  Model theModel){
        theModel.addAttribute("failure", fail);
        theModel.addAttribute("movie", theMovie);

        return "display/display-movie";
    }

    @GetMapping("/not-found")
    public String movieNotFound(Model theModel){
        List<Movie> movies = movieService.findAll();
        theModel.addAttribute("notFound", "Movie not found");
        theModel.addAttribute("movies", movies);
        return "display/collection-list";
    }



}
