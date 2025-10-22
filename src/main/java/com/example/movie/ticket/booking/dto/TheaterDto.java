package com.example.movie.ticket.booking.dto;

import com.example.movie.ticket.booking.entity.Screen;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class TheaterDto {
    @NotBlank(message = "Theater name is required")
    @Size(max = 150, message = "Theater name cannot exceed 150 characters")
    private String name;

    @NotBlank(message = "Location is required")
    @Size(max = 250, message = "Location cannot exceed 250 characters")
    private String location;


    private List<Screen> screens = new ArrayList<>();
}
