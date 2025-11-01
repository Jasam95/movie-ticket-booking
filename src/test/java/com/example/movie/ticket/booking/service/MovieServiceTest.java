package com.example.movie.ticket.booking.service;


import com.example.movie.ticket.booking.MovieTicketBookingApplication;
import com.example.movie.ticket.booking.dto.MovieDto;
import com.example.movie.ticket.booking.entity.Movie;
import com.example.movie.ticket.booking.repository.MovieRepository;
import com.example.movie.ticket.booking.service.impl.MovieServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MovieTicketBookingApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")

 class MovieServiceTest {

    @InjectMocks
    private MovieServiceImplementation movieService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ModelMapper modelMapper;

    private Movie movie;

    @BeforeEach
    void setUp() {
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");
        movie.setGenre("Sci-Fi");
        movie.setDuration(148);
        movie.setPosterUrl("url");
        movie.setRating(8.8);
    }

    @Test
    void getAllMovies_shouldReturnList() {
        when(movieRepository.findAll()).thenReturn(List.of(movie));
        var list = movieService.getAllMovies();
        assertEquals(1, list.size());
    }

    @Test
    void getMovieById_found() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        var m = movieService.getMovieById(1L);
        assertEquals("Inception", m.getTitle());
    }

    @Test
    void saveMovie_mapsAndSaves() throws Exception {
        MovieDto dto = new MovieDto();
        dto.setTitle("Inception");
        when(modelMapper.map(dto, Movie.class)).thenReturn(movie);
        when(movieRepository.save(movie)).thenReturn(movie);

        var saved = movieService.saveMovie(dto);
        assertNotNull(saved);
        verify(movieRepository).save(movie);
    }

    @Test
    void searchMovies_withEmptyQuery_returnsAll() {
        when(movieRepository.findAll()).thenReturn(List.of(movie));
        var res = movieService.searchMovies("");
        assertEquals(1, res.size());
    }
}

