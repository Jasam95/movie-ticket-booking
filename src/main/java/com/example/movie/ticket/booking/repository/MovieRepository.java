package com.example.movie.ticket.booking.repository;

import com.example.movie.ticket.booking.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository  extends JpaRepository<Movie, Long> {


    List<Movie> findByTitleContainingIgnoreCaseOrGenreContainingIgnoreCase(String query, String query1);
}
