package com.example.movie.ticket.booking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "bookings",
        uniqueConstraints = {
                // prevent double booking of same seat for same show
                @UniqueConstraint(columnNames = {"show_id", "seatCodes"})
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Booking {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        // User who booked
        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @Column(name = "username", nullable = false, length = 100)
        private String username;

        // The show (includes screen + movie + time)
        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(name = "show_id", nullable = false)
        private Show show;

        // Seat code (like A1, B5…)
        @Column(nullable = false, length = 500)
        @NotBlank(message = "At least one seat must be selected")
        private String seatCodes;

        // 🎬 Movie title
        @Column(name = "movie_title", nullable = false, length = 150)
        private String movieTitle;

        // 🎭 Theater name
        @Column(name = "theater_name", nullable = false, length = 150)
        private String theaterName;

        // 🕒 Show time
        @Column(name = "show_time", nullable = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        private LocalDateTime showTime;


        // Price per seat
        @Column(nullable = false, precision = 10, scale = 2)
        @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be positive")
        private BigDecimal amount;

        // When booking happened
        @Column(nullable = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        private LocalDateTime bookingTime = LocalDateTime.now();

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 20)
        private Status status = Status.CONFIRMED;


        public enum Status { PENDING, CONFIRMED, CANCELLED }
}


