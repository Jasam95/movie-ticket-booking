package com.example.movie.ticket.booking.dto;

import com.example.movie.ticket.booking.entity.Theater;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;

public class ScreenDto {

    @NotBlank(message = "Screen name is required")
    @Size(max = 100, message = "Screen name cannot exceed 100 characters")
    private String name;

    @NotNull(message = "Total seats is required")
    @Min(value = 1, message = "Total seats must be at least 1")
    @Max(value= 200,message = "Total seats should not exceed 200")
    private Integer totalSeats;


    private Theater theater;

}
