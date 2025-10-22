package com.example.movie.ticket.booking.service;

import com.example.movie.ticket.booking.entity.Screen;
import jakarta.validation.Valid;

import java.util.List;

public interface ScreenService {

    List<Screen> getAllScreens();

    Screen save(Long theaterId, @Valid  Screen screen);

    void deleteScreen(Long id);

    Screen getScreenById(Long id);

    Screen updateScreen(Long screenId, @Valid Screen screen , Long theaterId);
}
