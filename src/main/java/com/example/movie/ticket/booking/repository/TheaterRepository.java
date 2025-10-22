package com.example.movie.ticket.booking.repository;

import com.example.movie.ticket.booking.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater , Long> {
}
