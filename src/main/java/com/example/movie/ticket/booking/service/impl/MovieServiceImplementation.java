package com.example.movie.ticket.booking.service.impl;

import com.example.movie.ticket.booking.dto.MovieDto;
import com.example.movie.ticket.booking.entity.Movie;
import com.example.movie.ticket.booking.entity.User;
import com.example.movie.ticket.booking.repository.MovieRepository;
import com.example.movie.ticket.booking.service.MovieService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor

public class MovieServiceImplementation implements MovieService {

    private final MovieRepository movieRepository;
    private ModelMapper modelMapper;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Movie not found"));
    }

    @Override
    public Movie saveMovie(MovieDto movieDto) throws IOException {
        Movie movie = modelMapper.map(movieDto, Movie.class);
        return movieRepository.save(movie);
    }

    @Transactional
    @Override
    public Movie updateMovie(Long id, MovieDto movieDto) throws IOException{

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        modelMapper.map(movieDto, movie);
        return movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }


        @Override
        public List<Movie> searchMovies(String query) {
            if (query == null || query.trim().isEmpty()) {
                return movieRepository.findAll();
            }
            return movieRepository.findByTitleContainingIgnoreCaseOrGenreContainingIgnoreCase(query, query);
        }

}
