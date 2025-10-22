package com.example.movie.ticket.booking.controller;

import com.example.movie.ticket.booking.entity.Screen;
import com.example.movie.ticket.booking.entity.Theater;
import com.example.movie.ticket.booking.service.ScreenService;
import com.example.movie.ticket.booking.service.TheaterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class ScreenController {

    private ScreenService screenService;

    private TheaterService theaterService;

    @GetMapping("/admin/screens")
    public String listScreens(Model model) {
        model.addAttribute("screens", screenService.getAllScreens());
        return "screens";
    }


    @GetMapping("/admin/screens/new")
    public String addScreenForm(Model model) {
        model.addAttribute("screen", new Screen());
        model.addAttribute("theaters", theaterService.getAllTheaters());
        model.addAttribute("selectedTheaterId", null);
        return "screen-form";
    }


    @PostMapping("/admin/screens/new")
    public String saveScreen(@RequestParam("theaterId") Long theaterId,
                             @Valid @ModelAttribute Screen screen,
                             BindingResult result,
                             Model model) {

        if (result.hasErrors()) {
            model.addAttribute("theaters", theaterService.getAllTheaters());
            model.addAttribute("selectedTheaterId", theaterId);
            return "screen-form";
        }
        screenService.save(theaterId, screen);
        return "redirect:/admin/screens";
    }

    // Admin: edit theater form
    @GetMapping("/admin/screens/edit/{id}")
    public String editScreenForm(@PathVariable Long id, Model model) {
        Screen screen = screenService.getScreenById(id);
        model.addAttribute("screen", screen);
        model.addAttribute("theaters", theaterService.getAllTheaters());
        model.addAttribute("selectedTheaterId", screen.getTheater().getId());
        return "screen-form";
    }



    @PostMapping("/admin/screens/edit/{id}")
    public String updateScreen(@RequestParam Long theaterId, @PathVariable Long id,
                                @Valid @ModelAttribute("screen") Screen screen,
                                BindingResult result,Model model) {
        if (result.hasErrors()) {
            model.addAttribute("theaters", theaterService.getAllTheaters());
            model.addAttribute("selectedTheaterId", theaterId);
            return "screen-form";
        }

        screenService.updateScreen(id, screen,theaterId);
        return "redirect:/admin/screens";
    }

    @GetMapping("/admin/screens/delete/{id}")
    public String deleteScreen(@PathVariable Long id) {
        screenService.deleteScreen(id);
        return "redirect:/admin/screens";
    }
}
