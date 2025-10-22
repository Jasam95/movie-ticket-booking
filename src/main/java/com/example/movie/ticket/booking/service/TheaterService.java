package com.example.movie.ticket.booking.service;

import com.example.movie.ticket.booking.entity.Theater;

import java.util.List;

public interface TheaterService {

    List<Theater> getAllTheaters();

    Theater save(Theater theater);

    Theater findById(Long id);

    void deleteTheater(Long id);

    Theater updateTheater(Long id, Theater theater);

}
