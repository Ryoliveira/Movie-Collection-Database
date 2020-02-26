package com.ombd.OMDBDemo;


import com.ombd.OMDBDemo.dao.MovieRepository;
import com.ombd.OMDBDemo.entity.Movie;
import com.ombd.OMDBDemo.service.MovieServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @InjectMocks
    MovieServiceImpl movieService;

    @Mock
    MovieRepository movieRepository;

    @Mock
    RestTemplate restTemplate;

    @Test
    public void findAll(){
        List<Movie> movies = Arrays.asList(new Movie(), new Movie(), new Movie());

        Mockito.when(movieRepository.findAll()).thenReturn(movies);

        assertEquals(3, movieService.findAll().size());
        Mockito.verify(movieRepository, Mockito.atLeastOnce()).findAll();
    }

    @Test
    public void findFavorites(){
        List<Movie> movies = Arrays.asList(new Movie(), new Movie(), new Movie(), new Movie());

        movies.get(3).setFavorite(true);

        List<Movie> favorites = movies.stream().filter(Movie::isFavorite).collect(Collectors.toList());

        Mockito.when(movieRepository.findByIsFavorite(true)).thenReturn(favorites);

        assertEquals(1, movieService.findFavorites().size());
        Mockito.verify(movieRepository, Mockito.atLeastOnce()).findByIsFavorite(true);
    }

    @Test
    public void findWatchList(){
        List<Movie> movies = Arrays.asList(new Movie(), new Movie(), new Movie(), new Movie(), new Movie());
        movies.get(1).setWatchList(true);
        movies.get(4).setWatchList(true);

        List<Movie> watchList = movies.stream().filter(Movie::isWatchList).collect(Collectors.toList());

        Mockito.when(movieRepository.findByIsWatchList(true)).thenReturn(watchList);

        assertEquals(2, movieService.findWatchList().size());
        Mockito.verify(movieRepository, Mockito.atLeastOnce()).findByIsWatchList(true);

    }



    @Test
    public void findByTitle_InDatabase(){

        Movie testMovie = new Movie();

        testMovie.setTitle("Titanic");

        Mockito.when(movieRepository.findByTitle("Titanic")).thenReturn(testMovie);

        assertEquals("Titanic", movieService.findByTitle("Titanic").getTitle());
        Mockito.verify(movieRepository, Mockito.atLeastOnce()).findByTitle("Titanic");

    }

    @Test
    public void findByTitle_NotInDatabase(){
        Mockito.when(movieRepository.findByTitle("Titanic")).thenReturn(null);

        assertNull(movieService.findByTitle("Titanic"));
    }

    @Test
    public void findByImdbId_InDatabase(){
        Movie testMovie = new Movie();

        testMovie.setTitle("Shrek");

        Mockito.when(movieRepository.findByImdbId("tt0126029")).thenReturn(testMovie);

        assertEquals("Shrek", movieService.findByImdbId("tt0126029").getTitle());

    }

    @Test
    public void findByImdbId_NotInDatabase(){
        Mockito.when(movieRepository.findByImdbId("tt0126029")).thenReturn(null);

        assertNull(movieService.findByImdbId("tt0126029"));
    }

    @Test
    public void search_TitleInDatabase(){
        Movie testMovie = new Movie();
        testMovie.setTitle("Hot Rod");

        Mockito.when(movieRepository.findByTitle("Hot Rod")).thenReturn(testMovie);

        assertEquals("Hot Rod", movieService.search("Hot Rod", "title").getTitle());
    }

    @Test
    public void search_TitleNotInDatabase_AndNotFound(){

        //Todo fix this search_TitleNotInDatabase_AndNotFound

        Mockito.when(movieRepository.findByTitle("Hot Rod")).thenReturn(null);

        ReflectionTestUtils.setField(movieService, "url", "http://www.omdbapi.com");

        Mockito.when(restTemplate.getForEntity(anyString(), Mockito.eq(Movie.class))).thenReturn(new ResponseEntity<>(new Movie(), HttpStatus.OK));

        assertNull(movieService.search("Hot Rod", "title"));
    }



    @Test
    public void search_ImdbIdInDatabase(){
        Movie testMovie = new Movie();
        testMovie.setTitle("Hot Rod");

        Mockito.when(movieRepository.findByImdbId("tt0787475")).thenReturn(testMovie);

        assertEquals("Hot Rod", movieService.search("tt0787475", "ImdbId").getTitle());

    }


    @Test
    public void searchNewImdbId_MovieFound(){
        Movie testMovie = new Movie();
        testMovie.setTitle("Hot Rod");
        testMovie.setPoster("poster");


        ReflectionTestUtils.setField(movieService, "url", "http://www.omdbapi.com");


        Mockito.when(restTemplate.getForEntity(anyString(), Mockito.eq(Movie.class))).thenReturn(new ResponseEntity<>(testMovie, HttpStatus.OK));

        assertEquals("Hot Rod", movieService.searchNewImdbId("tt0787475").getTitle());
    }


    @Test
    public void searchNewMovieTitle_MovieFound(){
        Movie testMovie = new Movie();
        testMovie.setTitle("Hot Rod");
        testMovie.setPoster("poster");

        ReflectionTestUtils.setField(movieService, "url", "http://www.omdbapi.com");


        Mockito.when(restTemplate.getForEntity(anyString(), Mockito.eq(Movie.class))).thenReturn(new ResponseEntity<>(testMovie, HttpStatus.OK));

        assertEquals("Hot Rod", movieService.searchNewMovieTitle("Hot Rod").getTitle());

    }


    @Test
    public void save_toFavorites_success(){
        Movie testMovie = Mockito.mock(Movie.class);

        assertTrue(movieService.save(testMovie, "favorites"));
        Mockito.verify(testMovie, Mockito.atLeastOnce()).setFavorite(true);
    }

    @Test
    public void save_toFavorites_fail(){
        Movie testMovie = new Movie();
        testMovie.setTitle("test");
        testMovie.setFavorite(true);

        Mockito.when(movieRepository.findByTitle(testMovie.getTitle())).thenReturn(testMovie);

        assertFalse(movieService.save(testMovie, "favorites"));
        Mockito.verify(movieRepository, Mockito.never()).save(testMovie);
    }

    @Test
    public void save_toWatchList_success(){
        Movie testMovie = Mockito.mock(Movie.class);

        assertTrue(movieService.save(testMovie, "watch-list"));

        Mockito.verify(testMovie, Mockito.atLeastOnce()).setWatchList(true);
        Mockito.verify(movieRepository, Mockito.atLeastOnce()).save(testMovie);
    }

    @Test
    public void save_toWatchList_fail(){
        Movie testMovie = new Movie();
        testMovie.setTitle("test");
        testMovie.setWatchList(true);

        Mockito.when(movieRepository.findByTitle(testMovie.getTitle())).thenReturn(testMovie);

        assertFalse(movieService.save(testMovie, "watch-list"));
        Mockito.verify(movieRepository, Mockito.never()).save(testMovie);
    }

    @Test
    public void save_toCollection_success(){
        Movie testMovie = Mockito.mock(Movie.class);


        assertTrue(movieService.save(testMovie, "collection"));
        Mockito.verify(movieRepository, Mockito.atLeastOnce()).save(testMovie);
    }

    @Test
    public void save_toCollection_fail(){
        Movie testMovie = Mockito.mock(Movie.class);
        testMovie.setTitle("test");

        Mockito.when(movieRepository.findByTitle(testMovie.getTitle())).thenReturn(testMovie);

        Mockito.verify(movieRepository, Mockito.never()).save(testMovie);
        assertFalse(movieService.save(testMovie, "collection"));
    }

    @Test
    public void remove_fromFavorites(){
        Movie testMovie = Mockito.mock(Movie.class);
        testMovie.setFavorite(true);

        Mockito.when(movieRepository.findById(anyString())).thenReturn(Optional.of(testMovie));

        movieService.remove(anyString(), "favorites");

        Mockito.verify(movieRepository, Mockito.atLeastOnce()).save(testMovie);
        Mockito.verify(testMovie, Mockito.atLeastOnce()).setFavorite(false);
        assertFalse(testMovie.isFavorite());



    }


    @Test
    public void remove_fromWatchList(){
        Movie testMovie = Mockito.mock(Movie.class);
        testMovie.setWatchList(true);

        Mockito.when(movieRepository.findById(anyString())).thenReturn(Optional.of(testMovie));

        movieService.remove(anyString(), "watch-list");

        Mockito.verify(movieRepository, Mockito.atLeastOnce()).save(testMovie);
        Mockito.verify(testMovie, Mockito.atLeastOnce()).setWatchList(false);
        assertFalse(testMovie.isWatchList());
    }

    @Test
    public void remove_fromCollection(){
        Movie testMovie = Mockito.mock(Movie.class);

        Mockito.when(movieRepository.findById(anyString())).thenReturn(Optional.of(testMovie));

        movieService.remove(anyString(), "collection");

        Mockito.verify(movieRepository, Mockito.atLeastOnce()).delete(testMovie);
    }




}
