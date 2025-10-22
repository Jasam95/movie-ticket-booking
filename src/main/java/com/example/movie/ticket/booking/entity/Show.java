package com.example.movie.ticket.booking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "shows")
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Movie is required")
    @ManyToOne
    @JoinColumn(name = "movie_id",nullable = false)
    private Movie movie;

    @NotNull(message = "Screen is required")
    @ManyToOne
    @JoinColumn(name = "screen_id",nullable = false)
    private Screen screen;

    @NotNull(message = "Show time is required")
    @Future(message = "Show time must be in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime showTime;

    @NotNull(message = "Ticket price is required")
    @Positive(message = "Ticket price must be positive")
    private Double ticketPrice;

}

