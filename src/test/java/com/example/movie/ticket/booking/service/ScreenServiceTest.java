package com.example.movie.ticket.booking.service;


import com.example.movie.ticket.booking.MovieTicketBookingApplication;
import com.example.movie.ticket.booking.entity.Screen;
import com.example.movie.ticket.booking.entity.Theater;
import com.example.movie.ticket.booking.repository.ScreenRepository;
import com.example.movie.ticket.booking.repository.TheaterRepository;
import com.example.movie.ticket.booking.service.impl.ScreenServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

class ScreenServiceTest {

    @InjectMocks
    private ScreenServiceImplementation screenService;

    @Mock
    private ScreenRepository screenRepository;

    @Mock
    private TheaterRepository theaterRepository;

    private Theater theater;
    private Screen screen;

    @BeforeEach
    void setup() {
        theater = new Theater();
        theater.setId(1L);
        theater.setName("PVR");
        theater.setLocation("City");

        screen = new Screen();
        screen.setId(1L);
        screen.setName("Screen 1");
        screen.setTotalSeats(50);
    }

    @Test
    void save_shouldSetTheaterAndSave() {
        when(theaterRepository.findById(1L)).thenReturn(Optional.of(theater));
        when(screenRepository.save(any(Screen.class))).thenAnswer(inv -> {
            Screen s = inv.getArgument(0);
            s.setId(11L);
            return s;
        });

        Screen out = screenService.save(1L, screen);
        assertEquals(11L, out.getId());
        assertEquals(theater, out.getTheater());
    }

    @Test
    void getAllScreens_callsRepo() {
        when(screenRepository.findAll()).thenReturn(List.of(screen));
        var list = screenService.getAllScreens();
        assertEquals(1, list.size());
    }

    @Test
    void getScreenById_notFound_throws() {
        when(screenRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> screenService.getScreenById(99L));
    }
}

