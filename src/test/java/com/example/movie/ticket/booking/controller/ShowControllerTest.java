package com.example.movie.ticket.booking.controller;

import com.example.movie.ticket.booking.MovieTicketBookingApplication;
import com.example.movie.ticket.booking.entity.Movie;
import com.example.movie.ticket.booking.entity.Screen;
import com.example.movie.ticket.booking.entity.Show;
import com.example.movie.ticket.booking.repository.MovieRepository;
import com.example.movie.ticket.booking.repository.ScreenRepository;
import com.example.movie.ticket.booking.repository.ShowRepository;
import com.example.movie.ticket.booking.service.ShowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = MovieTicketBookingApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
class ShowControllerTest {

    @Mock private ShowService showService;
    @Mock private ShowRepository showRepository;
    @Mock private MovieRepository movieRepository;
    @Mock private ScreenRepository screenRepository;

    @InjectMocks private ShowController showController;

    private MockMvc mockMvc;
    private Movie movie;
    private Screen screen;
    private Show show;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders
                .standaloneSetup(showController)
                .setViewResolvers(viewResolver)
                .build();

        movie = new Movie(1L, "Interstellar", "Sci-Fi", 169, "poster.jpg", 9.0);
        screen = new Screen(1L, "Screen 1", 100, null);
        show = new Show(1L, movie, screen, LocalDateTime.now().plusHours(2), 250.0);
    }

    // Public shows page
    @Test
    void movies_shouldReturnShowsView() throws Exception {
        when(showService.getAllShows()).thenReturn(List.of(show));

        mockMvc.perform(get("/shows"))
                .andExpect(status().isOk())
                .andExpect(view().name("shows"))
                .andExpect(model().attributeExists("shows"));

        verify(showService).getAllShows();
    }

    //Admin list shows
    @Test
    void listShows_shouldReturnShowsView() throws Exception {
        when(showService.getAllShows()).thenReturn(List.of(show));

        mockMvc.perform(get("/admin/shows"))
                .andExpect(status().isOk())
                .andExpect(view().name("shows"))
                .andExpect(model().attributeExists("shows"));

        verify(showService).getAllShows();
    }

    // New show form
    @Test
    void newShowForm_shouldReturnShowForm() throws Exception {
        when(movieRepository.findAll()).thenReturn(List.of(movie));
        when(screenRepository.findAll()).thenReturn(List.of(screen));

        mockMvc.perform(get("/admin/shows/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("show-form"))
                .andExpect(model().attributeExists("show"))
                .andExpect(model().attributeExists("movies"))
                .andExpect(model().attributeExists("screens"));

        verify(movieRepository).findAll();
        verify(screenRepository).findAll();
    }

    //  Create show
    @Test
    void createShow_shouldRedirectToAdminShows() throws Exception {
        when(showService.saveShow(any(Show.class))).thenReturn(show);

        mockMvc.perform(post("/admin/shows/new")
                        .flashAttr("show", show))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/shows"));

        verify(showService, times(1)).saveShow(any(Show.class));
    }

    // Edit form
    @Test
    void editShowForm_shouldReturnShowFormView() throws Exception {
        when(showRepository.findById(1L)).thenReturn(Optional.of(show));
        when(movieRepository.findAll()).thenReturn(List.of(movie));
        when(screenRepository.findAll()).thenReturn(List.of(screen));

        mockMvc.perform(get("/admin/shows/edit/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("show-form"))
                .andExpect(model().attributeExists("show"))
                .andExpect(model().attributeExists("movies"))
                .andExpect(model().attributeExists("screens"));

        verify(showRepository).findById(1L);
        verify(movieRepository).findAll();
        verify(screenRepository).findAll();
    }

    // Update show
    @Test
    void updateShow_shouldRedirectToAdminShows() throws Exception {
        when(showService.saveShow(any(Show.class))).thenReturn(show);

        mockMvc.perform(post("/admin/shows/edit/{id}", 1L)
                        .flashAttr("show", show))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/shows"));

        verify(showService, times(1)).saveShow(any(Show.class));
    }

    // Delete show
    @Test
    void deleteShow_shouldRedirectToAdminShows() throws Exception {
        mockMvc.perform(get("/admin/shows/delete/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/shows"));

        verify(showService, times(1)).deleteShow(1L);
    }
}
