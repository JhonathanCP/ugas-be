package com.essalud.ugas.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.essalud.ugas.service.IReportStatusService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import com.essalud.ugas.dto.ReportStatusDTO;
import com.essalud.ugas.exception.ModelNotFoundException;
import com.essalud.ugas.model.ReportStatus;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/report-statuses")
public class ReportStatusController {

    @Autowired
    private IReportStatusService service;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<ReportStatusDTO>> findAll() {
        List<ReportStatusDTO> list = service.findAll().stream().map(p -> mapper.map(p, ReportStatusDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportStatusDTO> findById(@PathVariable Integer id) {
        ReportStatusDTO dtoResponse;
        ReportStatus obj = service.findById(id);

        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        } else {
            dtoResponse = mapper.map(obj, ReportStatusDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody ReportStatusDTO dto) {
        ReportStatus p = service.save(mapper.map(dto, ReportStatus.class));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(p.getIdReportStatus()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportStatus> update(@PathVariable Integer id, @Valid @RequestBody ReportStatusDTO dto) {
        // Verificar que el usuario exista
        ReportStatus obj = service.findById(id);
        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }

        // Mapear el DTO a la entidad ReportStatus
        ReportStatus ReportStatus = mapper.map(dto, ReportStatus.class);

        // Asignar el ID desde la URL al objeto mapeado
        ReportStatus.setIdReportStatus(id);

        // Actualizar el usuario
        return new ResponseEntity<>(service.update(ReportStatus), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        ReportStatus obj = service.findById(id);

        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        } else {
            service.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}