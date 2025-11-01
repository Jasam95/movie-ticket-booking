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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = MovieTicketBookingApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
class BookingRepositoryTest {

    @Autowired private BookingRepository bookingRepository;
    @Autowired private ShowRepository showRepository;
    @Autowired private ScreenRepository screenRepository;
    @Autowired private TheaterRepository theaterRepository;
    @Autowired private MovieRepository movieRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;

    @Test
    void saveBooking_shouldPersistCorrectly() {
        Role role = new Role(null, "ROLE_USER", new ArrayList<>());
        User user = new User();
        user.setFullName("Bob");
        user.setEmail("bob@example.com");
        user.setPassword("123");
        user.setRoles(List.of(role));
        userRepository.save(user);

        Theater theater = theaterRepository.save(new Theater(null, "PVR", "Chennai", List.of()));
        Screen screen = screenRepository.save(new Screen(null, "Screen 1", 80, theater));
        Movie movie = movieRepository.save(new Movie(null, "Avatar", "Sci-Fi", 148, "poster.jpg", 9.0));

        Show show = new Show();
        show.setMovie(movie);
        show.setScreen(screen);
        show.setShowTime(LocalDateTime.now().plusHours(2));
        show.setTicketPrice(250.0);
        showRepository.save(show);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setUsername(user.getFullName());
        booking.setShow(show);
        booking.setSeatCodes("A1,A2");
        booking.setMovieTitle(movie.getTitle());
        booking.setTheaterName(theater.getName());
        booking.setShowTime(show.getShowTime());
        booking.setAmount(BigDecimal.valueOf(500.0));
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(Booking.Status.CONFIRMED);
        bookingRepository.save(booking);

        List<String> seats = bookingRepository.findSeatCodesByShowId(show.getId());
        assertThat(seats).contains("A1,A2");
    }
}
