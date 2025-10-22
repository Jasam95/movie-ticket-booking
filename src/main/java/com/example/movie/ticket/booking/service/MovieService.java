package com.example.movie.ticket.booking.service;

import com.example.movie.ticket.booking.dto.MovieDto;
import com.example.movie.ticket.booking.entity.Movie;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MovieService {

    List<Movie> getAllMovies();

    Movie getMovieById(Long id);

    Movie saveMovie(MovieDto movieDto ) throws IOException;

    Movie updateMovie(Long id,MovieDto movieDto) throws IOException;

    void deleteMovie(Long id);

    List<Movie> searchMovies(String query);
}
