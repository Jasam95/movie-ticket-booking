package com.example.movie.ticket.booking.service.impl;

import com.example.movie.ticket.booking.entity.Booking;
import com.example.movie.ticket.booking.entity.Movie;
import com.example.movie.ticket.booking.entity.Show;
import com.example.movie.ticket.booking.entity.User;
import com.example.movie.ticket.booking.repository.BookingRepository;
import com.example.movie.ticket.booking.repository.MovieRepository;
import com.example.movie.ticket.booking.repository.UserRepository;
import com.example.movie.ticket.booking.service.BookingService;
import com.example.movie.ticket.booking.service.ShowService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingServiceImplementation implements BookingService {

    private  BookingRepository bookingRepository;

    private ShowService showService;

    private MovieRepository movieRepository;

    private UserRepository userRepository;

    @Override
    public Map<String, Object> getSeatSelectionData(Long id) {

        Show show = showService.getShowById(id);

        if (show == null || show.getScreen() == null) {
            throw new IllegalArgumentException("Invalid show or screen ID: " + id);
        }

        int totalSeats = show.getScreen().getTotalSeats();
        int seatsPerRow = 10;
        int rows = (int) Math.ceil((double) totalSeats / seatsPerRow);

        // Pre-generate seat labels like A1, A2...
        List<String> seatLabels = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            char rowChar = (char) ('A' + i);
            for (int j = 1; j <= seatsPerRow; j++) {
                int seatNumber = i * seatsPerRow + j;
                if (seatNumber <= totalSeats) {
                    seatLabels.add(rowChar + String.valueOf(j));
                }
            }
        }

        List<String> bookedSeats = getBookedSeatsForShow(id);

        Map<String, Object> data = new HashMap<>();
        data.put("show", show);
        data.put("rows", rows);
        data.put("totalSeats", totalSeats);
        data.put("seatLabels", seatLabels);
        data.put("bookedSeats", bookedSeats);
        return data;
    }

    @Override
    public String validateSeatSelection(Long id, List<String> selectedSeats) {
        if (selectedSeats == null || selectedSeats.isEmpty()) {
            return "⚠ Please select at least one seat.";
        }

        if (areSeatsAlreadyBooked(id, selectedSeats)) {
            return "❌ One or more selected seats are already booked. Please choose different seats.";
        }

        return null;
    }


    @Override
    public Booking createPendingBooking(Long id, List<String> selectedSeats, UserDetails userDetails) {
        Show show = showService.getShowById(id);
        if (show == null) throw new IllegalArgumentException("Invalid show ID");

        String userEmail = userDetails.getUsername();
        User user = userRepository.findByEmail(userEmail);
        if (user == null) throw new RuntimeException("User not found");

        BigDecimal amount = BigDecimal.valueOf(show.getTicketPrice() * selectedSeats.size());

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setUsername(user.getFullName());
        booking.setShow(show);
        booking.setSeatCodes(String.join(",", selectedSeats));
        booking.setMovieTitle(show.getMovie().getTitle());
        booking.setTheaterName(show.getScreen().getTheater().getName());
        booking.setShowTime(show.getShowTime());
        booking.setAmount(amount);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(Booking.Status.PENDING);

        // bookingRepository.save(booking); // Uncomment when ready

        return booking;
    }

    @Override
    public void confirmBooking(Long showId, String selectedSeats, BigDecimal amount, UserDetails userDetails) {
        Show show = showService.getShowById(showId);
        String userEmail = userDetails.getUsername();
        User user = userRepository.findByEmail(userEmail);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setUsername(user.getFullName());
        booking.setShow(show);
        booking.setSeatCodes(selectedSeats);
        booking.setMovieTitle(show.getMovie().getTitle());
        booking.setTheaterName(show.getScreen().getTheater().getName());
        booking.setShowTime(show.getShowTime());
        booking.setAmount(amount);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(Booking.Status.CONFIRMED);

        try {
            bookingRepository.save(booking);
        } catch (DataIntegrityViolationException e) {
            throw e;
        }

    }

    @Override
    public Map<String, Object> getBookingsForUser(UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email);
        if (user == null) throw new RuntimeException("User not found");

        List<Booking> bookings;
        String message;

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("ROLE_ADMIN"));

        if (isAdmin) {
            bookings = bookingRepository.findAll();
            message = "🎟 All Movie Bookings";
        } else {
            bookings = bookingRepository.findByUserOrderByBookingTimeDesc(user);
            message = "Your Bookings";
        }
        Map<String, Object> data = new HashMap<>();
        data.put("bookings", bookings);
        data.put("message", message);
        return data;

    }


    @Override
    public List<String> getBookedSeatsForShow(Long showId) {
        return bookingRepository.findSeatCodesByShowId(showId)
                .stream()
                .flatMap(seats -> Arrays.stream(seats.split(",")))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public boolean areSeatsAlreadyBooked(Long showId, List<String> selectedSeats) {
        List<String> bookedSeats = getBookedSeatsForShow(showId);
        for (String seat : selectedSeats) {
            if (bookedSeats.contains(seat)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void save(Booking booking) {
        bookingRepository.save(booking);
    }


}
