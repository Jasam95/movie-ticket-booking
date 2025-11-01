package com.example.movie.ticket.booking.api;

import com.example.movie.ticket.booking.entity.Theater;
import com.example.movie.ticket.booking.service.TheaterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theaters")
@RequiredArgsConstructor
@Tag(name = "Theater API", description = "Manage theaters")
public class TheaterApiController {

    private final TheaterService theaterService;

    @Operation(summary = "List all theaters")
    @GetMapping
    public List<Theater> getAllTheaters() {
        return theaterService.getAllTheaters();
    }

    @Operation(summary = "Get theater by ID")
    @GetMapping("/{id}")
    public Theater getTheater(@PathVariable Long id) {
        return theaterService.findById(id);
    }

    @Operation(summary = "Add a new theater")
    @PostMapping
    public Theater addTheater(@RequestBody Theater theater) {
        return theaterService.save(theater);
    }

    @Operation(summary = "Update existing theater")
    @PutMapping("/{id}")
    public Theater updateTheater(@PathVariable Long id, @RequestBody Theater theater) {
        return theaterService.updateTheater(id, theater);
    }

    @Operation(summary = "Delete theater by ID")
    @DeleteMapping("/{id}")
    public void deleteTheater(@PathVariable Long id) {
        theaterService.deleteTheater(id);
    }
}
