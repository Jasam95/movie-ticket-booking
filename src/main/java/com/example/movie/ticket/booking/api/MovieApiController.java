package com.example.movie.ticket.booking.api;

import com.example.movie.ticket.booking.dto.MovieDto;
import com.example.movie.ticket.booking.entity.Movie;
import com.example.movie.ticket.booking.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@Tag(name = "Movie API", description = "Operations related to movies")
public class MovieApiController {

    private final MovieService movieService;

    @Operation(summary = "Get all movies")
    @GetMapping
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @Operation(summary = "Get movie by ID")
    @GetMapping("/{id}")
    public Movie getMovie(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    @Operation(summary = "Create new movie")
    @PostMapping
    public Movie createMovie(@RequestBody MovieDto movieDto) throws IOException {
        return movieService.saveMovie(movieDto);
    }

    @Operation(summary = "Update an existing movie")
    @PutMapping("/{id}")
    public Movie updateMovie(@PathVariable Long id, @RequestBody MovieDto movieDto) throws IOException {
        return movieService.updateMovie(id, movieDto);
    }

    @Operation(summary = "Delete movie by ID")
    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
    }
}
