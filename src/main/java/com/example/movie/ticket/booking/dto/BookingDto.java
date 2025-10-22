package com.example.movie.ticket.booking.dto;

import com.example.movie.ticket.booking.entity.Booking;
import com.example.movie.ticket.booking.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    private Long id;

    private String movieTitle;
    private String theaterName;
    private String seatCodes; // e.g. "A1,A2"
    private LocalDateTime showTime;

    private BigDecimal totalAmount;


    @Enumerated(EnumType.STRING)
    private Booking.Status status = Booking.Status.CONFIRMED;


    public enum Status { PENDING, CONFIRMED, CANCELLED }

    private Long userId;
}
