package com.ombd.OMDBDemo.controller;

import com.ombd.OMDBDemo.entity.Movie;
import com.ombd.OMDBDemo.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class MovieController {

    private final Logger LOGGER = LoggerFactory.getLogger(MovieController.class);

    private MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }


    @GetMapping("/")
    public String homePage() {
        return "redirect:/movies";
    }


    @GetMapping("/movies/search")
    public String searchMovie(@RequestParam("searchString") String searchString,
                              @RequestParam("searchType") String searchType,
                              Model theModel,
                              RedirectAttributes redirectAttr) {

        Movie theMovie;
        if ((theMovie = movieService.search(searchString, searchType)) == null) {
            String msg = new StringBuilder(searchType).append(": ").append(searchString).append(" not found").toString();
            redirectAttr.addFlashAttribute("notFound", msg);
            return "redirect:/movies";
        }
        theModel.addAttribute("movie", theMovie);
        return "display/display-movie";
    }

    @GetMapping("/movies")
    public String displayMoviesPage(@RequestParam(value = "listView", defaultValue = "all") String listView,
                                    Model theModel) {
        if (listView.equals("collection") || listView.equals("all")) {
            theModel.addAttribute("collection", movieService.findAll());
        }
        if (listView.equals("favorites") || listView.equals("all")) {
            theModel.addAttribute("favorites", movieService.findFavorites());
        }
        if (listView.equals("watchList") || listView.equals("all")) {
            theModel.addAttribute("watchList", movieService.findWatchList());
        }
        theModel.addAttribute("listView", listView);
        return "display/collection-list";
    }

    @PostMapping("/movies")
    public String addMovie(@ModelAttribute("movie") Movie theMovie,
                           @RequestParam("listName") String listName,
                           Model theModel) {
        LOGGER.info(listName);
        if (!movieService.save(theMovie, listName)) {
            String msg = new StringBuilder(theMovie.getTitle()).append(" already in ").append(listName).toString();
            theModel.addAttribute("failure", msg);
            theModel.addAttribute("movie", theMovie);
            return "display/display-movie";
        }
        return "redirect:/movies";
    }


    @GetMapping("/delete")
    public String removeMovie(@RequestParam("id") String id, String listName,
                              @RequestParam("listView") String listView,
                              RedirectAttributes redirectAttr) {
        movieService.remove(id, listName);
        redirectAttr.addAttribute("listView", listView);
        return "redirect:/movies";
    }


}
