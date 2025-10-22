package com.example.movie.ticket.booking.service.impl;

import com.example.movie.ticket.booking.entity.Screen;
import com.example.movie.ticket.booking.entity.Theater;
import com.example.movie.ticket.booking.repository.ScreenRepository;
import com.example.movie.ticket.booking.repository.TheaterRepository;
import com.example.movie.ticket.booking.service.ScreenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@AllArgsConstructor
@Service
public class ScreenServiceImplementation implements ScreenService {

    private ScreenRepository screenRepository;

    private TheaterRepository theaterRepository;

    @Override
    public List<Screen> getAllScreens() {
        return screenRepository.findAll();
    }

    @Override
    public Screen save(Long theaterId, Screen screen) {
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new RuntimeException("Theater not found"));
        screen.setTheater(theater);
        return screenRepository.save(screen);
    }

    public void deleteScreen(Long id) {
        screenRepository.deleteById(id);
    }

    @Override
    public Screen getScreenById(Long id) {
        return screenRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Screen not found"));
    }


    // Update
    public Screen updateScreen(Long screenId, Screen screen, Long theaterId) {
        Screen existingScreen = screenRepository.findById(screenId)
                .orElseThrow(() -> new RuntimeException("Screen not found"));

        existingScreen.setName(screen.getName());
        existingScreen.setTotalSeats(screen.getTotalSeats());

        Theater theater = theaterRepository.getReferenceById(theaterId);
        existingScreen.setTheater(theater);

        return screenRepository.save(existingScreen); // now theater_id is updated
    }
}
