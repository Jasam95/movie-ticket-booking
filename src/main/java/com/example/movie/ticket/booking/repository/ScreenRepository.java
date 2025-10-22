package com.example.movie.ticket.booking.repository;

import com.example.movie.ticket.booking.entity.Screen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScreenRepository extends JpaRepository<Screen,Long> {

    List<Screen> findByTheaterId(Long theaterId);
}
