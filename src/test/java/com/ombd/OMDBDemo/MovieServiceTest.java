package com.ombd.OMDBDemo;


import com.ombd.OMDBDemo.dao.MovieRepository;
import com.ombd.OMDBDemo.entity.Movie;
import com.ombd.OMDBDemo.service.MovieServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @InjectMocks
    MovieServiceImpl movieService;

    @Mock
    MovieRepository movieRepository;

    @Mock
    RestTemplate restTemplate;

    @Spy
    Movie movieSpy;

    @Test
    public void findAll() {
        List<Movie> collection = Arrays.asList(new Movie(), new Movie(), new Movie());

        when(movieRepository.findAll()).thenReturn(collection);

        assertEquals(3, movieService.findAll().size());
        verify(movieRepository, atLeastOnce()).findAll();
    }

    @Test
    public void findFavorites() {
        List<Movie> favorites = Arrays.asList(new Movie(), new Movie(), new Movie(), new Movie());

        when(movieRepository.findByIsFavorite(true)).thenReturn(favorites);

        assertEquals(4, movieService.findFavorites().size());
        verify(movieRepository, atMostOnce()).findByIsFavorite(true);
    }

    @Test
    public void findWatchList() {
        List<Movie> watchList = Arrays.asList(new Movie(), new Movie(), new Movie(), new Movie(), new Movie());

        when(movieRepository.findByIsWatchList(true)).thenReturn(watchList);

        assertEquals(5, movieService.findWatchList().size());
        verify(movieRepository, atLeastOnce()).findByIsWatchList(true);
    }


    @Test
    public void findByTitle_InDatabase() {
        String title = "Titanic";
        movieSpy.setTitle(title);

        when(movieRepository.findByTitle(anyString())).thenReturn(movieSpy);

        assertEquals(title, movieService.findByTitle(anyString()).getTitle());
        verify(movieRepository, atLeastOnce()).findByTitle(anyString());
    }

    @Test
    public void findByTitle_NotInDatabase() {
        when(movieRepository.findByTitle(anyString())).thenReturn(null);

        assertNull(movieService.findByTitle(anyString()));
        verify(movieRepository, atMostOnce()).findByTitle(anyString());
    }

    @Test
    public void findByImdbId_InDatabase() {
        String title = "Shrek";
        movieSpy.setTitle(title);

        when(movieRepository.findByImdbId(anyString())).thenReturn(movieSpy);

        assertEquals(title, movieService.findByImdbId(anyString()).getTitle());
    }

    @Test
    public void findByImdbId_NotInDatabase() {
        when(movieRepository.findByImdbId(anyString())).thenReturn(null);

        assertNull(movieService.findByImdbId(anyString()));
    }

    @Test
    public void search_TitleInDatabase() {
        String title = "Hot Rod";
        String searchType = "title";
        movieSpy.setTitle(title);

        when(movieRepository.findByTitle(anyString())).thenReturn(movieSpy);

        assertEquals(title, movieService.search(anyString(), searchType).getTitle());
    }

    @Test
    public void search_TitleNotInDatabase_AndNotFound() {
        String searchType = "title";
        ReflectionTestUtils.setField(movieService, "url", "http://www.omdbapi.com");

        when(movieRepository.findByTitle(anyString())).thenReturn(null);
        when(restTemplate.getForEntity(anyString(), eq(Movie.class))).thenReturn(new ResponseEntity<>(new Movie(), HttpStatus.OK));

        assertNull(movieService.search(anyString(), searchType));
    }


    @Test
    public void search_ImdbIdInDatabase() {
        String title = "Hot Rod";
        String searchType = "ImdbId";
        movieSpy.setTitle(title);

        when(movieRepository.findByImdbId(anyString())).thenReturn(movieSpy);

        assertEquals(title, movieService.search(anyString(), searchType).getTitle());
    }


    @Test
    public void searchNewImdbId_MovieFound() {
        String title = "Hot Rod";
        movieSpy.setTitle(title);
        movieSpy.setPoster("poster");

        ReflectionTestUtils.setField(movieService, "url", "http://www.omdbapi.com");

        when(restTemplate.getForEntity(anyString(), eq(Movie.class))).thenReturn(new ResponseEntity<>(movieSpy, HttpStatus.OK));
        assertEquals(title, movieService.searchNewImdbId(title).getTitle());
    }

    @Test
    public void searchNewImdbId_MovieFound_NoPosterPresent() {
        String title = "Hot Rod";
        movieSpy.setTitle(title);
        movieSpy.setPoster("N/A");

        ReflectionTestUtils.setField(movieService, "url", "http://www.omdbapi.com");

        when(restTemplate.getForEntity(anyString(), eq(Movie.class))).thenReturn(new ResponseEntity<>(movieSpy, HttpStatus.OK));
        assertNull(movieService.searchNewImdbId(title));
    }

    @Test
    public void searchNewImdbId_MovieNotFound() {
        ReflectionTestUtils.setField(movieService, "url", "http://www.omdbapi.com");

        when(restTemplate.getForEntity(anyString(), eq(Movie.class))).thenReturn(new ResponseEntity<>(movieSpy, HttpStatus.OK));
        assertNull(movieService.searchNewImdbId("title"));
    }


    @Test
    public void searchNewMovieTitle_MovieFound() {
        String title = "Hot Rod";
        movieSpy.setTitle(title);
        movieSpy.setPoster("poster");
        ReflectionTestUtils.setField(movieService, "url", "http://www.omdbapi.com");


        when(restTemplate.getForEntity(anyString(), eq(Movie.class))).thenReturn(new ResponseEntity<>(movieSpy, HttpStatus.OK));

        assertEquals(title, movieService.searchNewMovieTitle(title).getTitle());
    }

    @Test
    public void searchNewMovieTitle_MovieFound_NoPosterPresent() {
        String title = "Hot Rod";
        movieSpy.setTitle(title);
        movieSpy.setPoster("N/A");
        ReflectionTestUtils.setField(movieService, "url", "http://www.omdbapi.com");

        when(restTemplate.getForEntity(anyString(), eq(Movie.class))).thenReturn(new ResponseEntity<>(movieSpy, HttpStatus.OK));
        assertNull(movieService.searchNewMovieTitle(title));
    }


    @Test
    public void save_ToFavorites_Success() {
        assertTrue(movieService.save(movieSpy, "favorites"));
        verify(movieSpy, atLeastOnce()).setFavorite(true);
    }

    @Test
    public void save_ToFavorites_Fail() {
        movieSpy.setTitle("test");
        movieSpy.setFavorite(true);

        when(movieRepository.findByTitle(movieSpy.getTitle())).thenReturn(movieSpy);

        assertFalse(movieService.save(movieSpy, "favorites"));
        verify(movieRepository, never()).save(movieSpy);
    }

    @Test
    public void save_ToWatchList_Success() {
        assertTrue(movieService.save(movieSpy, "watch-list"));
        verify(movieSpy, atLeastOnce()).setWatchList(true);
        verify(movieRepository, atLeastOnce()).save(movieSpy);
    }

    @Test
    public void save_ToWatchList_Fail() {
        movieSpy.setTitle("test");
        movieSpy.setWatchList(true);

        when(movieRepository.findByTitle(movieSpy.getTitle())).thenReturn(movieSpy);

        assertFalse(movieService.save(movieSpy, "watch-list"));
        verify(movieRepository, never()).save(movieSpy);
    }

    @Test
    public void save_ToCollection_Success() {
        assertTrue(movieService.save(movieSpy, "collection"));
        verify(movieRepository, atLeastOnce()).save(movieSpy);
    }

    @Test
    public void save_ToCollection_Fail() {
        movieSpy.setTitle("test");

        when(movieRepository.findByTitle(movieSpy.getTitle())).thenReturn(movieSpy);

        verify(movieRepository, never()).save(movieSpy);
        assertFalse(movieService.save(movieSpy, "collection"));
    }

    @Test
    public void remove_FromFavorites() {
        movieSpy.setFavorite(true);

        when(movieRepository.findById(anyString())).thenReturn(Optional.of(movieSpy));

        movieService.remove(anyString(), "favorites");

        verify(movieRepository, atLeastOnce()).save(movieSpy);
        verify(movieSpy, atLeastOnce()).setFavorite(false);
        assertFalse(movieSpy.isFavorite());


    }


    @Test
    public void remove_fromWatchList() {
        movieSpy.setWatchList(true);

        when(movieRepository.findById(anyString())).thenReturn(Optional.of(movieSpy));

        movieService.remove(anyString(), "watch-list");

        verify(movieRepository, atLeastOnce()).save(movieSpy);
        verify(movieSpy, atLeastOnce()).setWatchList(false);
        assertFalse(movieSpy.isWatchList());
    }

    @Test
    public void remove_fromCollection() {
        when(movieRepository.findById(anyString())).thenReturn(Optional.of(movieSpy));

        movieService.remove(anyString(), "collection");

        verify(movieRepository, atLeastOnce()).delete(movieSpy);
    }


}
