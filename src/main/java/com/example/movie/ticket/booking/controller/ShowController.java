package com.example.movie.ticket.booking.controller;

import com.example.movie.ticket.booking.entity.Show;
import com.example.movie.ticket.booking.repository.MovieRepository;
import com.example.movie.ticket.booking.repository.ScreenRepository;
import com.example.movie.ticket.booking.repository.ShowRepository;
import com.example.movie.ticket.booking.service.ShowService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class ShowController {

    private ShowService showService;

    private ShowRepository showRepository;

    private MovieRepository movieRepository;

    private ScreenRepository screenRepository;

    @GetMapping("/shows")
    public String movies(Model model) {
        model.addAttribute("shows", showService.getAllShows());
        return "shows";
    }

    @GetMapping("admin/shows")
    public String listShows(Model model) {
        model.addAttribute("shows", showService.getAllShows());
        return "shows";
    }

    @GetMapping("/admin/shows/new")
    public String newShowForm(Model model) {
        model.addAttribute("show", new Show());
        model.addAttribute("movies", movieRepository.findAll());
        model.addAttribute("screens", screenRepository.findAll());
        return "show-form";
    }

    @PostMapping("/admin/shows/new")
    public String createShow(@Valid @ModelAttribute("show") Show show,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("movies", movieRepository.findAll());
            model.addAttribute("screens", screenRepository.findAll());
            return "show-form"; // back to form
        }
        showService.saveShow(show);
        return "redirect:/admin/shows";
    }

    @GetMapping("/admin/shows/edit/{id}")
    public String editShowForm(@PathVariable Long id, Model model) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found"));

        model.addAttribute("show", show);
        model.addAttribute("movies", movieRepository.findAll());
        model.addAttribute("screens", screenRepository.findAll());

        return "show-form"; // reuse same form
    }

    @PostMapping("/admin/shows/edit/{id}")
    public String updateShow(@PathVariable Long id,
                             @Valid @ModelAttribute("show") Show show,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("movies", movieRepository.findAll());
            model.addAttribute("screens", screenRepository.findAll());
            return "show-form"; // back to form
        }
        show.setId(id);
        showService.saveShow(show);
        return "redirect:/admin/shows";
    }

    @GetMapping("/admin/shows/delete/{id}")
    public String deleteShow(@PathVariable Long id) {
        showService.deleteShow(id);
        return "redirect:/admin/shows";
    }
}
