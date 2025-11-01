package com.example.movie.ticket.booking.controller;

import com.example.movie.ticket.booking.MovieTicketBookingApplication;
import com.example.movie.ticket.booking.entity.Screen;
import com.example.movie.ticket.booking.entity.Theater;
import com.example.movie.ticket.booking.service.ScreenService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = MovieTicketBookingApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
class ScreenControllerTest {

    @Mock
    private ScreenService screenService;

    @Mock
    private TheaterService theaterService;

    @InjectMocks
    private ScreenController screenController;

    private MockMvc mockMvc;

    private Theater theater;
    private Screen screen;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        //  Setup mock view resolver to avoid circular view path
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders
                .standaloneSetup(screenController)
                .setViewResolvers(viewResolver)
                .build();

        theater = new Theater(1L, "PVR", "Chennai", List.of());
        screen = new Screen(1L, "Screen 1", 100, theater);
    }

    // List all screens
    @Test
    void listScreens_shouldReturnScreensView() throws Exception {
        when(screenService.getAllScreens()).thenReturn(List.of(screen));

        mockMvc.perform(get("/admin/screens"))
                .andExpect(status().isOk())
                .andExpect(view().name("screens"))
                .andExpect(model().attributeExists("screens"));

        verify(screenService, times(1)).getAllScreens();
    }

    //  New screen form
    @Test
    void addScreenForm_shouldReturnScreenFormView() throws Exception {
        when(theaterService.getAllTheaters()).thenReturn(List.of(theater));

        mockMvc.perform(get("/admin/screens/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("screen-form"))
                .andExpect(model().attributeExists("screen"))
                .andExpect(model().attributeExists("theaters"))
                .andExpect(model().attribute("selectedTheaterId", (Object) null));


        verify(theaterService, times(1)).getAllTheaters();
    }

    // Save new screen
    @Test
    void saveScreen_shouldRedirectToAdminScreens() throws Exception {
        when(screenService.save(anyLong(), any(Screen.class))).thenReturn(screen);

        mockMvc.perform(post("/admin/screens/new")
                        .param("theaterId", "1")
                        .flashAttr("screen", screen))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/screens"));

        verify(screenService, times(1)).save(eq(1L), any(Screen.class));
    }

    //  Edit form
    @Test
    void editScreenForm_shouldReturnScreenFormView() throws Exception {
        when(screenService.getScreenById(1L)).thenReturn(screen);
        when(theaterService.getAllTheaters()).thenReturn(List.of(theater));

        mockMvc.perform(get("/admin/screens/edit/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("screen-form"))
                .andExpect(model().attributeExists("screen"))
                .andExpect(model().attributeExists("theaters"))
                .andExpect(model().attributeExists("selectedTheaterId"));

        verify(screenService).getScreenById(1L);
        verify(theaterService).getAllTheaters();
    }

    //  Update screen
    @Test
    void updateScreen_shouldRedirectToAdminScreens() throws Exception {
        when(screenService.updateScreen(anyLong(), any(Screen.class), anyLong())).thenReturn(screen);

        mockMvc.perform(post("/admin/screens/edit/{id}", 1L)
                        .param("theaterId", "1")
                        .flashAttr("screen", screen))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/screens"));

        verify(screenService, times(1)).updateScreen(eq(1L), any(Screen.class), eq(1L));
    }

    // Delete screen
    @Test
    void deleteScreen_shouldRedirectToAdminScreens() throws Exception {
        mockMvc.perform(get("/admin/screens/delete/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/screens"));

        verify(screenService, times(1)).deleteScreen(1L);
    }
}
