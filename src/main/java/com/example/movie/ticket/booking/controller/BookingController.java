package com.example.movie.ticket.booking.controller;

import com.example.movie.ticket.booking.dto.UserDto;
import com.example.movie.ticket.booking.entity.Booking;
import com.example.movie.ticket.booking.entity.Show;
import com.example.movie.ticket.booking.entity.User;
import com.example.movie.ticket.booking.repository.BookingRepository;
import com.example.movie.ticket.booking.repository.UserRepository;
import com.example.movie.ticket.booking.service.BookingService;
import com.example.movie.ticket.booking.service.MovieService;
import com.example.movie.ticket.booking.service.ShowService;
import com.example.movie.ticket.booking.service.UserLogInService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class BookingController {

//    private UserLogInService userLogInService;

    private ShowService showService;

    private BookingService bookingService;

    private BookingRepository bookingRepository;

    private UserRepository userRepository;


    @GetMapping("/bookings")
    public String listBookings(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        Map<String, Object> data = bookingService.getBookingsForUser(userDetails);
        model.addAttribute("bookings", data.get("bookings"));
        model.addAttribute("message", data.get("message"));

        return "bookings";
    }


    @PostMapping("/bookings/confirm")
    public String finalizeBooking(
            @RequestParam Long showId,
            @RequestParam String selectedSeats,
            @RequestParam BigDecimal amount,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        try {
            bookingService.confirmBooking(showId, selectedSeats, amount, userDetails);
            return "redirect:/bookings/success";
        } catch (DataIntegrityViolationException e) {
            Show show = showService.getShowById(showId);
            model.addAttribute("errorMessage", "❌ Some selected seats have already been booked. Please choose different seats.");
            model.addAttribute("show", show);
            model.addAttribute("selectedSeats", selectedSeats);
            model.addAttribute("totalAmount", amount);
            return "payment-confirmation";
        }
    }

    @GetMapping("/bookings/success")
    public String bookingSuccess(Model model) {
        return "bookings-success";
    }
}

