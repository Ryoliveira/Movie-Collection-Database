package com.ombd.OMDBDemo;

import com.ombd.OMDBDemo.controller.MovieController;
import com.ombd.OMDBDemo.dao.MovieRepository;
import com.ombd.OMDBDemo.entity.Movie;
import com.ombd.OMDBDemo.service.MovieService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class OmdbDemoApplicationTests {

	@Mock
	MovieService movieServiceMock;

	@Mock
	Movie mockMovie;

	@Mock
	MovieRepository movieRepositoryMock;

	@Mock
	MovieController movieControllerMock;


	@Test
	void contextLoads() {
	}

	@Test
	void mockTest(){

		List<Movie> movies = Arrays.asList(new Movie(), new Movie(), new Movie());

		Mockito.when(movieServiceMock.findAll()).thenReturn(movies);

		assertEquals(3, movieServiceMock.findAll().size());

	}

	@Test
	void movieMockTest(){
		Mockito.when(mockMovie.getTitle()).thenReturn("Titanic");

		assertEquals("Titanic", mockMovie.getTitle());


	}

	@Test
	void dbTest(){

		List<Movie> movies = Arrays.asList(new Movie(), new Movie());

		Mockito.when(movieRepositoryMock.findAll()).thenReturn(movies);

		assertEquals(2, movieRepositoryMock.findAll().size());

	}



}

