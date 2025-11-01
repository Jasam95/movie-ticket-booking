package com.example.movie.ticket.booking.repository;


import com.example.movie.ticket.booking.MovieTicketBookingApplication;
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
class TheaterRepositoryTest {

    @Autowired
    private TheaterRepository theaterRepository;

    @Test
    void saveAndFindById_shouldPersistTheater() {
        Theater theater = new Theater();
        theater.setName("PVR");
        theater.setLocation("Chennai");
        theaterRepository.save(theater);

        Theater found = theaterRepository.findById(theater.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("PVR");
    }

    @Test
    void deleteTheater_shouldRemoveRecord() {
        Theater t = new Theater();
        t.setName("INOX");
        t.setLocation("Coimbatore");
        theaterRepository.save(t);

        theaterRepository.deleteById(t.getId());
        assertThat(theaterRepository.findById(t.getId())).isEmpty();
    }
}

