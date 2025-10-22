package com.example.movie.ticket.booking.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {

    @NotBlank(message = "Title is required")
    @Column(nullable = false, unique = true, length = 150)
    private String title;

    @NotBlank(message = "Genre is required")
    @Column(nullable = false, length = 500)
    private String genre;

    @Min(value = 30, message = "Duration must be at least 30 minutes")
    @Max(value = 500, message = "Duration cannot exceed 500 minutes")
    @Column(nullable = false)
    private int duration; // in minutes

    @NotBlank(message = "Poster URL is required")
    @Column(nullable = false, length = 500)
    private String posterUrl; // path or URL to poster image

    @DecimalMin(value = "0.0", message = "Rating must be 0 or higher")
    @DecimalMax(value = "10.0", message = "Rating cannot exceed 10.0")
    @Column(nullable = false)
    private double rating;
}
