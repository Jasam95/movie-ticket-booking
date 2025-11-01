package com.example.movie.ticket.booking.api;

import com.example.movie.ticket.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Booking API", description = "Book movie tickets and view booked seats")
public class BookingApiController {

    private final BookingService bookingService;

    @Operation(summary = "Get all booked seats for a show")
    @GetMapping("/show/{showId}")
    public List<String> getBookedSeats(@PathVariable Long showId) {
        return bookingService.getBookedSeatsForShow(showId);
    }

    @Operation(summary = "Confirm a booking (no authentication required)")
    @PostMapping
    public String confirmBooking(
            @RequestParam Long showId,
            @RequestParam String selectedSeats,
            @RequestParam BigDecimal amount) {
        bookingService.confirmBooking(showId, selectedSeats, amount, null);
        return "Booking confirmed!";
    }
}
