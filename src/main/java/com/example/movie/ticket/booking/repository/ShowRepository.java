package com.example.movie.ticket.booking.repository;

import com.example.movie.ticket.booking.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShowRepository extends JpaRepository<Show,Long> {

    List<Show> findByScreenId(Long screenId);

    List<Show> findByMovieId(Long movieId);
}
