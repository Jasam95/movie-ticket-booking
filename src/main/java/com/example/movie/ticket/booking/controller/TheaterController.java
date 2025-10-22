package com.example.movie.ticket.booking.controller;

import com.example.movie.ticket.booking.dto.MovieDto;
import com.example.movie.ticket.booking.entity.Theater;
import com.example.movie.ticket.booking.service.TheaterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@AllArgsConstructor

public class TheaterController {

    private TheaterService theaterService;

    @GetMapping("/theaters")
    public String theaters(Model model) {
        model.addAttribute("theaters", theaterService.getAllTheaters());
        return "theaters";
    }

    @GetMapping("/admin/theaters")
    public String listTheaters(Model model) {
        model.addAttribute("theaters", theaterService.getAllTheaters());
        return "theaters";
    }

    @GetMapping("/admin/theaters/new")
    public String addTheaterForm(Model model) {
        model.addAttribute("theater", new Theater());
        return "theater-form";
    }

    @PostMapping("/admin/theaters/new")
    public String saveTheater(@Valid @ModelAttribute Theater theater, BindingResult result) {
        if (result.hasErrors()) {
            return "theater-form";
            }
        theaterService.save(theater);
        return "redirect:/admin/theaters";
    }

    // Admin: edit theater form
    @GetMapping("/admin/theaters/edit/{id}")
    public String editMovieForm(@PathVariable Long id, Model model) {
        model.addAttribute("theater", theaterService.findById(id));
        return "theater-form";
    }

    //Admin:Update theater form
    @PostMapping("/admin/theaters/edit/{id}")
    public String updateTheater(@PathVariable Long id,
                                @Valid @ModelAttribute("theater") Theater theater,
                                BindingResult result) {
        if (result.hasErrors()){
            return "theater-form";
        }
        theater.setId(id);
        theaterService.save(theater);
        return "redirect:/admin/theaters";
    }

    @GetMapping ("/admin/theaters/delete/{id}")
    public String deleteTheater(@PathVariable Long id) {
        theaterService.deleteTheater(id);
        return "redirect:/admin/theaters";
    }
}
