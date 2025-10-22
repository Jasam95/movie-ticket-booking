package com.example.movie.ticket.booking.dto;

import com.example.movie.ticket.booking.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

        private Long id;

        @NotEmpty
        private String fullName;

        @NotEmpty(message = "Email should not be empty")
        @Email
        private String email;

        @Size(min = 6, message = "Password must be at least 6 characters")
        @NotEmpty(message = "Password should not be empty")
        private String password;

        private List<Role> roles = new ArrayList<>();
    }

