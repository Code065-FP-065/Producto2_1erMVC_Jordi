package com.alquilatus.vehiculos.controller;

import com.alquilatus.vehiculos.model.EstadoVehiculo;
import com.alquilatus.vehiculos.model.Vehiculo;
import com.alquilatus.vehiculos.repository.VehiculoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoController {

    private final VehiculoRepository vehiculoRepository;

    public VehiculoController(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    @GetMapping
    public String listarVehiculos(Model model) {
        var vehiculos = vehiculoRepository.findAll();
        System.out.println("VEHICULOS ENCONTRADOS: " + vehiculos.size());
        model.addAttribute("vehiculos", vehiculos);
        return "vehiculos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        model.addAttribute("estados", EstadoVehiculo.values());
        return "vehiculos/formulario";
    }

    @PostMapping("/guardar")
    public String guardarVehiculo(@ModelAttribute Vehiculo vehiculo) {

        // Si tiene ID, es edición
        if (vehiculo.getIdVehiculo() != null) {
            Vehiculo vehiculoExistente = vehiculoRepository.findById(vehiculo.getIdVehiculo())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "ID de vehículo no válido: " + vehiculo.getIdVehiculo()));

            vehiculoExistente.setMatricula(vehiculo.getMatricula());
            vehiculoExistente.setMarca(vehiculo.getMarca());
            vehiculoExistente.setModelo(vehiculo.getModelo());
            vehiculoExistente.setTipo(vehiculo.getTipo());
            vehiculoExistente.setPrecioDia(vehiculo.getPrecioDia());
            vehiculoExistente.setEstado(vehiculo.getEstado());

            vehiculoRepository.save(vehiculoExistente);

        } else {
            // Si no tiene ID, es un alta nueva
            vehiculoRepository.save(vehiculo);
        }

        return "redirect:/vehiculos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de vehículo no válido: " + id));

        model.addAttribute("vehiculo", vehiculo);
        model.addAttribute("estados", EstadoVehiculo.values());
        return "vehiculos/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarVehiculo(@PathVariable Long id, Model model) {
        try {
            vehiculoRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            System.out.println("No se puede eliminar el vehículo porque está relacionado con un alquiler.");
        }
        return "redirect:/vehiculos";
    }
}