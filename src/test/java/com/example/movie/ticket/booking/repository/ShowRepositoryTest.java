package com.example.movie.ticket.booking.repository;

import com.example.movie.ticket.booking.MovieTicketBookingApplication;
import com.example.movie.ticket.booking.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = MovieTicketBookingApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
class ShowRepositoryTest {

    @Autowired private ShowRepository showRepository;
    @Autowired private MovieRepository movieRepository;
    @Autowired private ScreenRepository screenRepository;
    @Autowired private TheaterRepository theaterRepository;

    @Test
    void saveAndFindByMovieId_shouldWork() {
        Theater theater = theaterRepository.save(new Theater(null, "PVR", "Mumbai", List.of()));
        Screen screen = new Screen(null, "Screen 1", 100, theater);
        screenRepository.save(screen);

        Movie movie = movieRepository.save(new Movie(null, "Avatar", "Sci-Fi", 180, "poster.jpg", 9.0));

        Show show = new Show();
        show.setMovie(movie);
        show.setScreen(screen);
        show.setShowTime(LocalDateTime.now().plusDays(1));
        show.setTicketPrice(300.0);
        showRepository.save(show);

        List<Show> shows = showRepository.findByMovieId(movie.getId());
        assertThat(shows).hasSize(1);
        assertThat(shows.get(0).getScreen().getName()).isEqualTo("Screen 1");
    }
}
