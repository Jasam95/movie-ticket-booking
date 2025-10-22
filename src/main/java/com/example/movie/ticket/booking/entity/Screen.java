package com.example.movie.ticket.booking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table (name="screens")
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Screen name is required")
    @Size(max = 100, message = "Screen name cannot exceed 100 characters")
    private String name;

    @NotNull(message = "Total seats is required")
    @Min(value = 1, message = "Total seats must be at least 1")
    @Max(value= 200,message = "Total seats should not exceed 200")
    private Integer totalSeats;

    @ManyToOne
    @JoinColumn(name = "theater_id" , nullable = false)
    private Theater theater;

}
