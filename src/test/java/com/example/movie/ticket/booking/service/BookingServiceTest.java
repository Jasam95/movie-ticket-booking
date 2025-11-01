package com.example.movie.ticket.booking.service;


import com.example.movie.ticket.booking.MovieTicketBookingApplication;
import com.example.movie.ticket.booking.entity.*;
import com.example.movie.ticket.booking.repository.BookingRepository;
import com.example.movie.ticket.booking.repository.MovieRepository;
import com.example.movie.ticket.booking.repository.UserRepository;
import com.example.movie.ticket.booking.service.impl.BookingServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MovieTicketBookingApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")

class BookingServiceTest {

    @InjectMocks
    private BookingServiceImplementation bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ShowService showService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private UserRepository userRepository;

    private Show show;
    private Screen screen;
    private Movie movie;
    private User user;

    @BeforeEach
    void setup() {
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Matrix");

        screen = new Screen();
        screen.setId(11L);
        screen.setName("Main");
        screen.setTotalSeats(20);
        Theater th = new Theater();
        th.setId(2L);
        th.setName("Grand");
        screen.setTheater(th);

        show = new Show();
        show.setId(5L);
        show.setMovie(movie);
        show.setScreen(screen);
        show.setShowTime(LocalDateTime.now().plusDays(1));
        show.setTicketPrice(150.0);

        user = new User();
        user.setId(100L);
        user.setEmail("user1@123.com");
        user.setFullName("User1");

    }

    @Test
    void getSeatSelectionData_generatesSeatLabelsAndBookedSeats() {
        when(showService.getShowById(5L)).thenReturn(show);
        when(bookingRepository.findSeatCodesByShowId(5L)).thenReturn(List.of("A1,A2"));

        var data = bookingService.getSeatSelectionData(5L);
        assertEquals(show, data.get("show"));
        assertTrue(((List<String>)data.get("seatLabels")).size() > 0);
        assertTrue(((List<String>)data.get("bookedSeats")).contains("A1"));
    }

    @Test
    void validateSeatSelection_whenSelectedAlreadyBooked_returnsMessage() {
        when(bookingRepository.findSeatCodesByShowId(5L)).thenReturn(List.of("B1,B2"));
        String msg = bookingService.validateSeatSelection(5L, List.of("B1"));
        assertNotNull(msg);
        assertTrue(msg.contains("already booked"));
    }

    @Test
    void createPendingBooking_constructsBookingButNotSaved() {
        UserDetails ud = mock(UserDetails.class);
        when(ud.getUsername()).thenReturn("user1@123.com");
        when(showService.getShowById(5L)).thenReturn(show);
        when(userRepository.findByEmail("user1@123.com")).thenReturn(user);

        var booking = bookingService.createPendingBooking(5L, List.of("A1","A2"), ud);
        assertEquals(Booking.Status.PENDING, booking.getStatus());
        assertEquals("A1,A2", booking.getSeatCodes());
        assertEquals(BigDecimal.valueOf(150.0 * 2), booking.getAmount());
    }

    @Test
    void confirmBooking_savesBooking() {
        UserDetails ud = mock(UserDetails.class);
        when(ud.getUsername()).thenReturn("bob@example.com");
        when(showService.getShowById(5L)).thenReturn(show);
        when(userRepository.findByEmail("bob@example.com")).thenReturn(user);

        bookingService.confirmBooking(5L, "A3", BigDecimal.valueOf(150.0), ud);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void getBookedSeatsForShow_flattensSeatCodes() {
        when(bookingRepository.findSeatCodesByShowId(5L)).thenReturn(List.of("A1,A2", "B3"));
        var list = bookingService.getBookedSeatsForShow(5L);
        assertTrue(list.contains("A1"));
        assertTrue(list.contains("B3"));
    }
}
