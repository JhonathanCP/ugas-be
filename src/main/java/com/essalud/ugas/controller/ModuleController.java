package com.essalud.ugas.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.essalud.ugas.service.IModuleService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import com.essalud.ugas.dto.ModuleDTO;
import com.essalud.ugas.dto.ReportDTO;
import com.essalud.ugas.exception.ModelNotFoundException;
import com.essalud.ugas.model.Module;
import com.essalud.ugas.model.Report;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/modules")
public class ModuleController {

    @Autowired
    private IModuleService service;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<ModuleDTO>> findAll() {
        List<ModuleDTO> list = service.findAll().stream().map(p -> mapper.map(p, ModuleDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuleDTO> findById(@PathVariable Integer id) {
        ModuleDTO dtoResponse;
        Module obj = service.findById(id);

        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        } else {
            dtoResponse = mapper.map(obj, ModuleDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody ModuleDTO dto) {
        Module p = service.save(mapper.map(dto, Module.class));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(p.getIdModule()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Module> update(@PathVariable Integer id, @Valid @RequestBody ModuleDTO dto) {
        // Verificar que el usuario exista
        Module obj = service.findById(id);
        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }

        // Mapear el DTO a la entidad Module
        Module Module = mapper.map(dto, Module.class);

        // Asignar el ID desde la URL al objeto mapeado
        Module.setIdModule(id);

        // Actualizar el usuario
        return new ResponseEntity<>(service.update(Module), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        Module obj = service.findById(id);

        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        } else {
            service.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/active-with-reports")
    public ResponseEntity<List<ModuleDTO>> getActiveModulesWithReports() {
        List<ModuleDTO> list = service.isActiveAndHasActiveReports()
                                    .stream()
                                    .map(p -> mapper.map(p, ModuleDTO.class))
                                    .collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/active-with-reports-in-production")
    public ResponseEntity<List<ModuleDTO>> getActiveModulesWithReportsInProduction() {
        List<ModuleDTO> list = service.isActiveAndHasActiveReportsInProduction()
                                    .stream()
                                    .map(p -> mapper.map(p, ModuleDTO.class))
                                    .collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}/reports")
    public ResponseEntity<List<ReportDTO>> getReportsByModule(@PathVariable Integer id) {
        List<Report> reports = service.getReportsByModule(id);

        if (reports.isEmpty()) {
            throw new ModelNotFoundException("No reports found for module ID: " + id);
        }

        List<ReportDTO> list = reports.stream()
                                    .map(r -> mapper.map(r, ReportDTO.class))
                                    .collect(Collectors.toList());

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}