package com.example.movie.ticket.booking.controller;

import com.example.movie.ticket.booking.MovieTicketBookingApplication;
import com.example.movie.ticket.booking.entity.*;
import com.example.movie.ticket.booking.repository.BookingRepository;
import com.example.movie.ticket.booking.repository.UserRepository;
import com.example.movie.ticket.booking.service.BookingService;
import com.example.movie.ticket.booking.service.ShowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = MovieTicketBookingApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
class BookingControllerTest {

    @Mock private ShowService showService;
    @Mock private BookingService bookingService;
    @Mock private BookingRepository bookingRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private BookingController bookingController;

    private MockMvc mockMvc;
    private Show show;
    private Booking booking;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");

        HandlerMethodArgumentResolver nullAuthResolver = new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.hasParameterAnnotation(AuthenticationPrincipal.class)
                        || parameter.getParameterType().equals(UserDetails.class);
            }

            @Override
            public Object resolveArgument(MethodParameter parameter,
                                          ModelAndViewContainer mavContainer,
                                          NativeWebRequest webRequest,
                                          WebDataBinderFactory binderFactory) {
                return null;
            }
        };

        mockMvc = MockMvcBuilders
                .standaloneSetup(bookingController)
                .setViewResolvers(viewResolver)
                .setCustomArgumentResolvers(nullAuthResolver)
                .build();

        // Mock Data
        Movie movie = new Movie(1L, "Inception", "Sci-Fi", 148, "poster.jpg", 9.0);
        Theater theater = new Theater(1L, "PVR", "Chennai", List.of());
        Screen screen = new Screen(1L, "Screen 1", 120, theater);
        show = new Show(1L, movie, screen, LocalDateTime.now().plusHours(2), 250.0);

        booking = new Booking();
        booking.setId(1L);
        booking.setShow(show);
        booking.setSeatCodes("A1,A2");
        booking.setAmount(BigDecimal.valueOf(500));
    }

    @Test
    void listBookings_shouldReturnBookingsView() throws Exception {
        when(bookingService.getBookingsForUser(any())).thenReturn(
                Map.of("bookings", List.of(booking), "message", "Your Bookings")
        );

        mockMvc.perform(get("/bookings"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookings"))
                .andExpect(model().attributeExists("bookings"))
                .andExpect(model().attributeExists("message"));

        verify(bookingService).getBookingsForUser(any());
    }

    @Test
    void finalizeBooking_shouldRedirectToSuccess_onValidBooking() throws Exception {
        doNothing().when(bookingService)
                .confirmBooking(anyLong(), anyString(), any(BigDecimal.class), isNull());

        mockMvc.perform(post("/bookings/confirm")
                        .param("showId", "1")
                        .param("selectedSeats", "A1,A2")
                        .param("amount", "500"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bookings/success"));

        verify(bookingService)
                .confirmBooking(eq(1L), eq("A1,A2"), eq(BigDecimal.valueOf(500)), isNull());
    }

    @Test
    void finalizeBooking_shouldReturnPaymentConfirmation_onSeatConflict() throws Exception {
        when(showService.getShowById(1L)).thenReturn(show);
        doThrow(new DataIntegrityViolationException("Seat already booked"))
                .when(bookingService)
                .confirmBooking(anyLong(), anyString(), any(BigDecimal.class), isNull());

        mockMvc.perform(post("/bookings/confirm")
                        .param("showId", "1")
                        .param("selectedSeats", "A1,A2")
                        .param("amount", "500"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment-confirmation"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attributeExists("show"))
                .andExpect(model().attributeExists("selectedSeats"))
                .andExpect(model().attributeExists("totalAmount"));

        verify(showService).getShowById(1L);
    }

    @Test
    void bookingSuccess_shouldReturnBookingsSuccessView() throws Exception {
        mockMvc.perform(get("/bookings/success"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookings-success"));
    }
}
