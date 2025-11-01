package com.example.movie.ticket.booking.repository;

import com.example.movie.ticket.booking.MovieTicketBookingApplication;
import com.example.movie.ticket.booking.entity.Screen;
import com.example.movie.ticket.booking.entity.Theater;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = MovieTicketBookingApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
class ScreenRepositoryTest {

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @Test
    void saveScreenWithTheater_shouldPersist() {
        Theater theater = new Theater();
        theater.setName("PVR");
        theater.setLocation("Bangalore");
        theaterRepository.save(theater);

        Screen screen = new Screen();
        screen.setName("Screen 1");
        screen.setTotalSeats(120);
        screen.setTheater(theater);
        screenRepository.save(screen);

        Screen found = screenRepository.findById(screen.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getTheater().getName()).isEqualTo("PVR");
    }
}
