package com.example.movie.ticket.booking.controller;

import com.example.movie.ticket.booking.MovieTicketBookingApplication;
import com.example.movie.ticket.booking.entity.Theater;
import com.example.movie.ticket.booking.service.TheaterService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = MovieTicketBookingApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
class TheaterControllerTest {

    @Mock
    private TheaterService theaterService;

    @InjectMocks
    private TheaterController theaterController;

    private MockMvc mockMvc;
    private Theater theater;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        //  Avoid circular view path issues
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders
                .standaloneSetup(theaterController)
                .setViewResolvers(viewResolver)
                .build();

        theater = new Theater(1L, "PVR", "Chennai", List.of());
    }

    //  Public list of theaters
    @Test
    void theaters_shouldReturnTheatersView() throws Exception {
        when(theaterService.getAllTheaters()).thenReturn(List.of(theater));

        mockMvc.perform(get("/theaters"))
                .andExpect(status().isOk())
                .andExpect(view().name("theaters"))
                .andExpect(model().attributeExists("theaters"));

        verify(theaterService).getAllTheaters();
    }

    // Admin list of theaters
    @Test
    void listTheaters_shouldReturnTheatersView() throws Exception {
        when(theaterService.getAllTheaters()).thenReturn(List.of(theater));

        mockMvc.perform(get("/admin/theaters"))
                .andExpect(status().isOk())
                .andExpect(view().name("theaters"))
                .andExpect(model().attributeExists("theaters"));

        verify(theaterService, times(1)).getAllTheaters();
    }

    //  Add theater form
    @Test
    void addTheaterForm_shouldReturnTheaterFormView() throws Exception {
        mockMvc.perform(get("/admin/theaters/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("theater-form"))
                .andExpect(model().attributeExists("theater"));
    }

    //  Save theater
    @Test
    void saveTheater_shouldRedirectToAdminTheaters() throws Exception {
        when(theaterService.save(any(Theater.class))).thenReturn(theater);

        mockMvc.perform(post("/admin/theaters/new")
                        .flashAttr("theater", theater))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/theaters"));

        verify(theaterService, times(1)).save(any(Theater.class));
    }

    //  Edit theater form
    @Test
    void editMovieForm_shouldReturnTheaterFormView() throws Exception {
        when(theaterService.findById(1L)).thenReturn(theater);

        mockMvc.perform(get("/admin/theaters/edit/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("theater-form"))
                .andExpect(model().attributeExists("theater"));

        verify(theaterService).findById(1L);
    }

    //  Update theater
    @Test
    void updateTheater_shouldRedirectToAdminTheaters() throws Exception {
        when(theaterService.save(any(Theater.class))).thenReturn(theater);

        mockMvc.perform(post("/admin/theaters/edit/{id}", 1L)
                        .flashAttr("theater", theater))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/theaters"));

        verify(theaterService).save(any(Theater.class));
    }

    //  Delete theater
    @Test
    void deleteTheater_shouldRedirectToAdminTheaters() throws Exception {
        mockMvc.perform(get("/admin/theaters/delete/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/theaters"));

        verify(theaterService).deleteTheater(1L);
    }
}
