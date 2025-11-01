package com.example.movie.ticket.booking.controller;



import com.example.movie.ticket.booking.MovieTicketBookingApplication;
import com.example.movie.ticket.booking.dto.MovieDto;
import com.example.movie.ticket.booking.entity.Movie;
import com.example.movie.ticket.booking.entity.Screen;
import com.example.movie.ticket.booking.entity.Show;
import com.example.movie.ticket.booking.entity.Theater;
import com.example.movie.ticket.booking.service.MovieService;
import com.example.movie.ticket.booking.service.ShowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = MovieTicketBookingApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
class MovieControllerTest {

    @Mock
    private MovieService movieService;

    @Mock
    private ShowService showService;

    @Mock
    private Model model;

    @InjectMocks
    private MovieController movieController;

    private MockMvc mockMvc;

    private Movie movie;
    private Show show;
    private Theater theater;
    private Screen screen;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders
                .standaloneSetup(movieController)
                .setViewResolvers(viewResolver)
                .build();

        theater = new Theater(1L, "INOX", "Chennai", new ArrayList<>());
        screen = new Screen(1L, "Screen 1", 100, theater);
        movie = new Movie(1L, "Inception", "Sci-Fi", 148, "poster.jpg", 9.0);
        show = new Show(1L, movie, screen, LocalDateTime.now().plusHours(2), 250.0);
    }


    @Test
    void listMovies_withQuery_shouldReturnFilteredMovies() throws Exception {
        // given
        Movie movie = new Movie();
        movie.setTitle("Avatar");
        movie.setGenre("Action");
        when(movieService.searchMovies("Avatar")).thenReturn(List.of(movie));

        // when + then
        mockMvc.perform(get("/movies").param("query", "Avatar"))
                .andExpect(status().isOk())
                .andExpect(view().name("movies")) // ✅ logical view name resolved
                .andExpect(model().attributeExists("movies"))
                .andExpect(model().attributeExists("query"));

        verify(movieService, times(1)).searchMovies("Avatar");
    }



    @Test
    void listMovies_withoutQuery_shouldReturnAllMovies() throws Exception {
        when(movieService.getAllMovies()).thenReturn(List.of(movie));

        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(view().name("movies"))
                .andExpect(model().attributeExists("movies"));

        verify(movieService).getAllMovies();
    }

    @Test
    void adminListMovies_shouldReturnMoviesView() throws Exception {
        when(movieService.getAllMovies()).thenReturn(List.of(new Movie()));

        mockMvc.perform(get("/admin/movies"))
                .andExpect(status().isOk())
                .andExpect(view().name("movies"))
                .andExpect(model().attributeExists("movies"));

        verify(movieService, times(1)).getAllMovies();
    }


    @Test
    void movieDetails_shouldReturnShowDetailsGroupedByTheater() throws Exception {
        when(movieService.getMovieById(1L)).thenReturn(movie);
        when(showService.getShowsByMovieId(1L)).thenReturn(List.of(show));

        mockMvc.perform(get("/movies/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("movie-show-details"))
                .andExpect(model().attributeExists("movie"))
                .andExpect(model().attributeExists("showsByTheater"));

        verify(movieService).getMovieById(1L);
        verify(showService).getShowsByMovieId(1L);
    }

    @Test
    void newMovieForm_shouldReturnMovieFormView() throws Exception {
        mockMvc.perform(get("/admin/movies/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("movie-form"))
                .andExpect(model().attributeExists("movie"));
    }

    @Test
    void saveMovie_shouldRedirectToAdminMovies() throws Exception {
        // given
        MovieDto dto = new MovieDto();
        dto.setTitle("Avatar");
        dto.setGenre("Action");
        dto.setDuration(180);
        dto.setPosterUrl("poster.jpg");
        dto.setRating(8.5);

        // movieService.saveMovie returns a Movie object
        when(movieService.saveMovie(any(MovieDto.class))).thenReturn(new Movie());

        // when + then
        mockMvc.perform(post("/admin/movies/new")
                        .flashAttr("movie", dto))
                .andExpect(status().is3xxRedirection())           // ✅ expect a redirect status
                .andExpect(redirectedUrl("/admin/movies"));        // ✅ expect correct redirect target

        // verify interaction
        verify(movieService, times(1)).saveMovie(any(MovieDto.class));
    }



    @Test
    void editMovieForm_shouldReturnMovieFormView() throws Exception {
        when(movieService.getMovieById(1L)).thenReturn(movie);

        mockMvc.perform(get("/admin/movies/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("movie-form"))
                .andExpect(model().attributeExists("movie"));
    }

    @Test
    void updateMovie_shouldRedirectToAdminMovies() throws Exception {
        MovieDto dto = new MovieDto();
        dto.setTitle("Inception");
        dto.setGenre("Sci-Fi");
        dto.setDuration(160);
        dto.setPosterUrl("poster.jpg");
        dto.setRating(9.0);

        when(movieService.updateMovie(anyLong(), any(MovieDto.class))).thenReturn(new Movie());

        mockMvc.perform(post("/admin/movies/edit/{id}", 1L)
                        .flashAttr("movie", dto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/movies"));

        verify(movieService).updateMovie(eq(1L), any(MovieDto.class));
    }


    @Test
    void deleteMovie_shouldRedirectToAdminMovies() throws Exception {
        mockMvc.perform(get("/admin/movies/delete/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/movies"));

        verify(movieService).deleteMovie(1L);
    }




}

