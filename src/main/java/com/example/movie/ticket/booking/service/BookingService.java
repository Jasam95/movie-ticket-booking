package com.example.movie.ticket.booking.service;

import com.example.movie.ticket.booking.entity.Booking;
import com.example.movie.ticket.booking.entity.Movie;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BookingService {

    List<String> getBookedSeatsForShow(Long showId);


    void save(Booking booking);

    boolean areSeatsAlreadyBooked(Long id, List<String> selectedSeats);


    Map<String, Object> getSeatSelectionData(Long id);

    String validateSeatSelection(Long id, List<String> selectedSeats);

    Booking createPendingBooking(Long id, List<String> selectedSeats, UserDetails userDetails);

    void confirmBooking(Long showId, String selectedSeats, BigDecimal amount, UserDetails userDetails);

    Map<String,Object> getBookingsForUser(UserDetails userDetails);
}
