package com.example.movie.ticket.booking.service.impl;

import com.example.movie.ticket.booking.entity.Theater;
import com.example.movie.ticket.booking.repository.TheaterRepository;
import com.example.movie.ticket.booking.service.TheaterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TheaterServiceImplementation implements TheaterService {

    private TheaterRepository theaterRepository;

    public List<Theater> getAllTheaters() {
        return theaterRepository.findAll();
    }


    public Theater save(Theater theater) {
        return theaterRepository.save(theater);
    }

    public Theater findById(Long id) {
        return theaterRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Theater not found"));
    }

    public void deleteTheater(Long id) {
        theaterRepository.deleteById(id);
    }

    @Override
    public Theater updateTheater(Long id, Theater theater) {
        Theater existTheater = theaterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("theater not found"));
        existTheater.setId(id);
        existTheater.setName(theater.getName());
        existTheater.setLocation(theater.getLocation());
//        modelMapper.map(movieDto, movie);
        return theaterRepository.save(theater);
    }
}

