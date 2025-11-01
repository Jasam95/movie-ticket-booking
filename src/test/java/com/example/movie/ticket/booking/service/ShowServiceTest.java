package com.example.movie.ticket.booking.service;


import com.example.movie.ticket.booking.MovieTicketBookingApplication;
import com.example.movie.ticket.booking.entity.Movie;
import com.example.movie.ticket.booking.entity.Screen;
import com.example.movie.ticket.booking.entity.Show;
import com.example.movie.ticket.booking.repository.MovieRepository;
import com.example.movie.ticket.booking.repository.ScreenRepository;
import com.example.movie.ticket.booking.repository.ShowRepository;
import com.example.movie.ticket.booking.service.impl.ShowServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MovieTicketBookingApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")

class ShowServiceTest {

    @InjectMocks
    private ShowServiceImplementation showService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ScreenRepository screenRepository;

    @Mock
    private ShowRepository showRepository;

    private Movie movie;
    private Screen screen;
    private Show show;

    @BeforeEach
    void setup() {
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Dune");

        screen = new Screen();
        screen.setId(2L);
        screen.setName("S1");
        screen.setTotalSeats(60);

        show = new Show();
        show.setId(5L);
        show.setMovie(movie);
        show.setScreen(screen);
        show.setShowTime(LocalDateTime.now().plusDays(1));
        show.setTicketPrice(200.0);
    }

    @Test
    void saveShow_shouldValidateAndSave() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(screenRepository.findById(2L)).thenReturn(Optional.of(screen));
        when(showRepository.save(any(Show.class))).thenAnswer(inv -> {
            Show s = inv.getArgument(0);
            s.setId(123L);
            return s;
        });

        show.getMovie().setId(1L);
        show.getScreen().setId(2L);

        Show saved = showService.saveShow(show);
        assertNotNull(saved.getId());
        assertEquals(123L, saved.getId());
    }

    @Test
    void getShowsByMovieId_callsRepo() {
        when(showRepository.findByMovieId(1L)).thenReturn(List.of(show));
        var list = showService.getShowsByMovieId(1L);
        assertEquals(1, list.size());
    }
}
