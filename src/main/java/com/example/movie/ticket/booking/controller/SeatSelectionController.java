package com.example.movie.ticket.booking.controller;

import com.example.movie.ticket.booking.entity.Booking;
import com.example.movie.ticket.booking.entity.Show;
import com.example.movie.ticket.booking.entity.User;
import com.example.movie.ticket.booking.repository.BookingRepository;
import com.example.movie.ticket.booking.repository.UserRepository;
import com.example.movie.ticket.booking.service.BookingService;
import com.example.movie.ticket.booking.service.MovieService;
import com.example.movie.ticket.booking.service.ShowService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor

public class SeatSelectionController {

    private final ShowService showService;

    private final BookingService bookingService;

    private BookingRepository bookingRepository;

    private UserRepository userRepository;

    @GetMapping("/shows/{id}/seats")
    public String showSeats(@PathVariable Long id, Model model) {

        Map<String, Object> seatData = bookingService.getSeatSelectionData(id);


        model.addAttribute("show", seatData.get("show"));
        model.addAttribute("rows", seatData.get("rows"));
        model.addAttribute("totalSeats", seatData.get("totalSeats"));
        model.addAttribute("seatLabels", seatData.get("seatLabels"));
        model.addAttribute("bookedSeats", seatData.get("bookedSeats"));

        return "seat-selection";
    }

    @PostMapping("/shows/{id}/seats/select")
    public String confirmBooking(@PathVariable Long id,
                                 @RequestParam(required = false) List<String> selectedSeats,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        String errorMessage = bookingService.validateSeatSelection(id, selectedSeats);
        if (errorMessage != null) {
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/shows/" + id + "/seats";
        }

        Booking booking = bookingService.createPendingBooking(id, selectedSeats, userDetails);

        model.addAttribute("booking", booking);
        model.addAttribute("selectedSeats", selectedSeats);
        model.addAttribute("totalAmount", booking.getAmount());

        return "payment-confirmation";
    }
}

