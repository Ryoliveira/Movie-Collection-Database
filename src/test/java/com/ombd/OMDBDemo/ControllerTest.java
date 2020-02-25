package com.ombd.OMDBDemo;


import com.ombd.OMDBDemo.controller.MovieController;
import com.ombd.OMDBDemo.entity.Movie;
import com.ombd.OMDBDemo.service.MovieService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class ControllerTest {

    @Mock
    MovieService movieServiceMock;

    @InjectMocks
    MovieController controllerMock;

    @Mock
    Movie movieMock;

    @Mock
    Model modelMock;


    @Autowired
    private WebApplicationContext context;


    @Test
    void addMovieFail(){

        String flag = "collection";
        Mockito.when(movieMock.getTitle()).thenReturn("Titanic");

        Mockito.when(movieServiceMock.save(movieMock, flag)).thenReturn(false);
        assertEquals(controllerMock.addMovie(movieMock, flag, modelMock), "display/display-movie");

    }

    @Test
    void addMovieSuccess(){

        String flag = "collection";
        Mockito.when(movieMock.getTitle()).thenReturn("Titanic");

        Mockito.when(movieServiceMock.save(movieMock, flag)).thenReturn(true);

        assertEquals("redirect:/movies", controllerMock.addMovie(movieMock, flag, modelMock));
    }



}
