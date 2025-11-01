package com.example.movie.ticket.booking.service;

import com.example.movie.ticket.booking.MovieTicketBookingApplication;
import com.example.movie.ticket.booking.entity.Theater;
import com.example.movie.ticket.booking.repository.TheaterRepository;
import com.example.movie.ticket.booking.service.impl.TheaterServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
@ExtendWith(MockitoExtension.class)

class TheaterServiceTest {

    @InjectMocks
    private TheaterServiceImplementation theaterService;

    @Mock
    private TheaterRepository theaterRepository;

    private Theater theater;

    @BeforeEach
    void setup() {
        theater = new Theater();
        theater.setId(1L);
        theater.setName("Mayajaal");
        theater.setLocation("Chennai");
    }

    @Test
    void getAllTheaters_returnsList() {
        when(theaterRepository.findAll()).thenReturn(List.of(theater));
        var list = theaterService.getAllTheaters();
        assertEquals(1, list.size());
    }

    @Test
    void findById_whenNotFound_throws() {
        when(theaterRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> theaterService.findById(99L));
    }

    @Test
    void save_shouldPersist() {
        when(theaterRepository.save(theater)).thenReturn(theater);
        var saved = theaterService.save(theater);
        assertEquals("Mayajaal", saved.getName());
    }
}
