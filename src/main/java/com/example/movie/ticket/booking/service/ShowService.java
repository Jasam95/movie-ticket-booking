package com.example.movie.ticket.booking.service;

import com.example.movie.ticket.booking.entity.Show;
import jakarta.validation.Valid;

import java.util.List;

public interface ShowService {

    List<Show> getAllShows();

    Show saveShow( Show show);

    void deleteShow(Long id);

    Show getShowById(Long id);

    List<Show> getShowsByMovieId(Long id);
}
