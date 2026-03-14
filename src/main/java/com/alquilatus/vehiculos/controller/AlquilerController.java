package com.alquilatus.vehiculos.controller;

import com.alquilatus.vehiculos.repository.AlquilerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AlquilerController {

    private final AlquilerRepository alquilerRepository;

    public AlquilerController(AlquilerRepository alquilerRepository) {
        this.alquilerRepository = alquilerRepository;
    }

    @GetMapping("/alquileres")
    public String listarAlquileres(Model model) {
        var alquileres = alquilerRepository.findAll();
        System.out.println("ALQUILERES ENCONTRADOS: " + alquileres.size());
        System.out.println("LISTA ALQUILERES: " + alquileres);
        model.addAttribute("alquileres", alquileres);
        return "alquileres/lista";
    }
}