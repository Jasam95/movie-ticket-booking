package com.example.movie.ticket.booking.service.impl;

import com.example.movie.ticket.booking.entity.Movie;
import com.example.movie.ticket.booking.entity.Screen;
import com.example.movie.ticket.booking.entity.Show;
import com.example.movie.ticket.booking.repository.MovieRepository;
import com.example.movie.ticket.booking.repository.ScreenRepository;
import com.example.movie.ticket.booking.repository.ShowRepository;
import com.example.movie.ticket.booking.service.ShowService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ShowServiceImplementation implements ShowService {

    private MovieRepository movieRepository;

    private ScreenRepository screenRepository;

    private ShowRepository showRepository;

    @Override
    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

    public Show saveShow( Show show) {
        Movie movie = movieRepository.findById(show.getMovie().getId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        Screen screen = screenRepository.findById(show.getScreen().getId())
                .orElseThrow(() -> new RuntimeException("Screen not found"));

        show.setMovie(movie);
        show.setScreen(screen);
        return showRepository.save(show);
    }

    @Override
    public void deleteShow(Long id) {
        showRepository.deleteById(id);
    }

    @Override
    public Show getShowById(Long id) {
        return showRepository.getReferenceById(id);
    }

    @Override
    public List<Show> getShowsByMovieId(Long movieId) {
        return showRepository.findByMovieId(movieId);
    }
}
