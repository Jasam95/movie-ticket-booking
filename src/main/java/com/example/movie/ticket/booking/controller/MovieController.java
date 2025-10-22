package com.example.movie.ticket.booking.controller;


import com.example.movie.ticket.booking.dto.MovieDto;
import com.example.movie.ticket.booking.entity.Movie;
import com.example.movie.ticket.booking.entity.Show;
import com.example.movie.ticket.booking.service.MovieService;
import com.example.movie.ticket.booking.service.ShowService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class MovieController {

    private  MovieService movieService;
    private ShowService showService;

    @GetMapping("/movies")
    public String listMovies(@RequestParam(value = "query", required = false) String query, Model model) {
        List<Movie> movies;

        if (query != null && !query.trim().isEmpty()) {
            movies = movieService.searchMovies(query.trim());
            model.addAttribute("query", query);
        } else {
            movies = movieService.getAllMovies();
        }

        model.addAttribute("movies", movies);
        return "movies";
    }

    // List movies
    @GetMapping("/admin/movies")
    public String listMovies(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        return "movies";
    }


    // Movie details
    @GetMapping("/movies/{id}")
    public String movieDetails(@PathVariable Long id, Model model) {

        Movie movie = movieService.getMovieById(id);
        List<Show> shows = showService.getShowsByMovieId(id);


        // Group by theater name
        Map<String, List<Show>> showsByTheater = shows.stream()
                .collect(Collectors.groupingBy(s -> s.getScreen().getTheater().getName()));

        model.addAttribute("movie", movie);
        model.addAttribute("showsByTheater", showsByTheater);
        return "movie-show-details";
    }

    // Admin: new movie form
    @GetMapping("/admin/movies/new")
    public String newMovieForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "movie-form";
    }

    @PostMapping("/admin/movies/new")
    public String saveMovie(@Valid @ModelAttribute("movie") MovieDto movieDto,
                            BindingResult result,
                            Model model) throws IOException {
        if (result.hasErrors()) {
            return "movie-form"; // re-render form with validation errors
        }
        movieService.saveMovie(movieDto);
        return "redirect:/admin/movies"; // redirect to movies list
    }

    // Admin: edit movie form
    @GetMapping("/admin/movies/edit/{id}")
    public String editMovieForm(@PathVariable Long id, Model model) {
        model.addAttribute("movie", movieService.getMovieById(id));
        return "movie-form";
    }

    // Admin: update movie
    @PostMapping("/admin/movies/edit/{id}")
    public String updateMovie(@PathVariable Long id, @ModelAttribute("movie") MovieDto movieDto)
            throws IOException {
        movieService.updateMovie(id,movieDto);
        return "redirect:/admin/movies";
    }

    // Admin: delete movie
    @GetMapping("/admin/movies/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return "redirect:/admin/movies";
    }

}
