package com.example.movie.ticket.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class ShowDto {

    @NotNull(message = "Show time is required")
    @Future(message = "Show time must be in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime showTime;

    @NotNull(message = "Ticket price is required")
    @Positive(message = "Ticket price must be positive")
    private Double ticketPrice;
}
