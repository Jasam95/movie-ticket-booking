package com.example.movie.ticket.booking.api;

import com.example.movie.ticket.booking.entity.Screen;
import com.example.movie.ticket.booking.service.ScreenService;
import com.example.movie.ticket.booking.service.TheaterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/screens")
@RequiredArgsConstructor
@Tag(name = "Screen API", description = "Manage movie screens under theaters")
public class ScreenApiController {

    private final ScreenService screenService;
    private final TheaterService theaterService;

    @Operation(summary = "Get all screens")
    @GetMapping
    public List<Screen> getAllScreens() {
        return screenService.getAllScreens();
    }



    @Operation(summary = "Get a screen by ID")
    @GetMapping("/{id}")
    public Screen getScreenById(@PathVariable Long id) {
        return screenService.getScreenById(id);
    }

    @Operation(summary = "Add a new screen to a theater")
    @PostMapping
    public Screen createScreen(@RequestParam Long theaterId, @RequestBody Screen screen) {
        return screenService.save(theaterId, screen);
    }

    @Operation(summary = "Update screen details")
    @PutMapping("/{id}")
    public Screen updateScreen(
            @PathVariable Long id,
            @RequestParam Long theaterId,
            @RequestBody Screen screen) {
        return screenService.updateScreen(id, screen, theaterId);
    }

    @Operation(summary = "Delete screen by ID")
    @DeleteMapping("/{id}")
    public void deleteScreen(@PathVariable Long id) {
        screenService.deleteScreen(id);
    }
}
