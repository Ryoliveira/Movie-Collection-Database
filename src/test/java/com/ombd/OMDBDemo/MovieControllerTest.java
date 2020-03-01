package com.ombd.OMDBDemo;


import com.ombd.OMDBDemo.controller.MovieController;
import com.ombd.OMDBDemo.entity.Movie;
import com.ombd.OMDBDemo.service.MovieService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(MockitoJUnitRunner.class)
public class MovieControllerTest {


    @Mock
    private MovieService movieService;

    @Mock
    private Movie mockMovie;

    @Mock
    private List<Movie> mockList;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        final MovieController movieController = new MovieController(movieService);

        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();

        when(movieService.findAll()).thenReturn(mockList);
        when(movieService.findFavorites()).thenReturn(mockList);
        when(movieService.findWatchList()).thenReturn(mockList);

    }


    @Test
    public void homePageRedirect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/movies"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void searchMovie_Found() throws Exception {
        Mockito.when(movieService.search(anyString(), anyString())).thenReturn(mockMovie);

        mockMvc.perform(MockMvcRequestBuilders.get("/movies/search")
                .param("searchString", anyString())
                .param("searchType", anyString()))
                .andExpect(status().isOk())
                .andExpect(view().name("display/display-movie"))
                .andExpect(model().attribute("movie", mockMovie))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    @Test
    public void searchMovie_NotFound() throws Exception {
        Mockito.when(movieService.search(anyString(), anyString())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/movies/search")
                .param("searchString", anyString())
                .param("searchType", anyString()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/movies"))
                .andExpect(flash().attribute("notFound", ":  not found"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void displayMoviesPage_All() throws Exception {
        String listView = "all";
        mockMvc.perform(MockMvcRequestBuilders.get("/movies")
                .param("listView", listView))
                .andExpect(status().isOk())
                .andExpect(view().name("display/collection-list"))
                .andExpect(model().attribute("collection", mockList))
                .andExpect(model().attribute("favorites", mockList))
                .andExpect(model().attribute("watchList", mockList))
                .andExpect(model().attribute("listView", listView))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        verify(movieService, atLeastOnce()).findAll();
        verify(movieService, atLeastOnce()).findFavorites();
        verify(movieService, atLeastOnce()).findWatchList();
    }

    @Test
    public void displayMoviesPage_Collection() throws Exception {
        String listView = "collection";
        mockMvc.perform(MockMvcRequestBuilders.get("/movies")
                .param("listView", listView))
                .andExpect(status().isOk())
                .andExpect(view().name("display/collection-list"))
                .andExpect(model().attribute("collection", mockList))
                .andExpect(model().attribute("listView", listView))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        verify(movieService, never()).findFavorites();
        verify(movieService, never()).findWatchList();

    }

    @Test
    public void displayMoviesPage_Favorties() throws Exception {
        String listView = "favorites";
        mockMvc.perform(MockMvcRequestBuilders.get("/movies")
                .param("listView", listView))
                .andExpect(status().isOk())
                .andExpect(view().name("display/collection-list"))
                .andExpect(model().attribute("favorites", mockList))
                .andExpect(model().attribute("listView", listView))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        verify(movieService, never()).findAll();
        verify(movieService, never()).findWatchList();
    }

    @Test
    public void displayMoviesPage_WatchList() throws Exception {
        String listView = "watchList";
        mockMvc.perform(MockMvcRequestBuilders.get("/movies")
                .param("listView", listView))
                .andExpect(status().isOk())
                .andExpect(view().name("display/collection-list"))
                .andExpect(model().attribute("watchList", mockList))
                .andExpect(model().attribute("listView", listView))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        verify(movieService, never()).findAll();
        verify(movieService, never()).findFavorites();
    }

    @Test
    public void addMovie_Success() throws Exception {
        String flag = "collection";
        when(movieService.save(eq(mockMovie), anyString())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/movies")
                .flashAttr("movie", mockMovie)
                .param("flag", flag))
                .andExpect(view().name("redirect:/movies"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        verify(movieService, atLeastOnce()).save(mockMovie, flag);
    }

    @Test
    public void addMovie_Fail() throws Exception {
        Movie movieSpy = spy(Movie.class);
        String flag = "collection";
        String title = "Lord of the Rings";
        movieSpy.setTitle(title);
        when(movieService.save(eq(movieSpy), anyString())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/movies")
                .flashAttr("movie", movieSpy)
                .param("flag", flag))
                .andExpect(view().name("display/display-movie"))
                .andExpect(model().attribute("failure", title + " already in " + flag))
                .andExpect(model().attribute("movie", movieSpy))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        verify(movieService, atLeastOnce()).save(movieSpy, flag);
    }

    @Test
    public void removeMovie() throws Exception {
        String listView = "collection";
        mockMvc.perform(MockMvcRequestBuilders.get("/delete")
                .param("id", anyString())
                .param("flag", anyString())
                .param("listView", listView))
                .andExpect(view().name("redirect:/movies"))
                .andExpect(model().attribute("listView", listView))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        verify(movieService, atLeastOnce()).remove(anyString(), anyString());
    }


}
