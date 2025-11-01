package com.example.movie.ticket.booking.api;

import com.example.movie.ticket.booking.entity.Show;
import com.example.movie.ticket.booking.service.ShowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shows")
@RequiredArgsConstructor
@Tag(name = "Show API", description = "Manage shows and schedules")
public class ShowApiController {

    private final ShowService showService;

    @Operation(summary = "Get all shows")
    @GetMapping
    public List<Show> getAllShows() {
        return showService.getAllShows();
    }

    @Operation(summary = "Get show by ID")
    @GetMapping("/{id}")
    public Show getShow(@PathVariable Long id) {
        return showService.getShowById(id);
    }

    @Operation(summary = "Get all shows for a specific movie")
    @GetMapping("/movie/{movieId}")
    public List<Show> getShowsByMovie(@PathVariable Long movieId) {
        return showService.getShowsByMovieId(movieId);
    }
}
