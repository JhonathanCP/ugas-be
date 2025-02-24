package com.essalud.ugas.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.essalud.ugas.service.IAccessAuditService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import com.essalud.ugas.dto.AccessAuditDTO;
import com.essalud.ugas.exception.ModelNotFoundException;
import com.essalud.ugas.model.AccessAudit;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

    @RestController
    @RequestMapping("/access-audit")
    public class AccessAuditController {

        @Autowired
        private IAccessAuditService service;

        @Autowired
        private ModelMapper mapper;

        @GetMapping
        public ResponseEntity<List<AccessAuditDTO>> findAll() {
            List<AccessAuditDTO> list = service.findAll().stream().map(p -> mapper.map(p, AccessAuditDTO.class)).collect(Collectors.toList());
            return new ResponseEntity<>(list, HttpStatus.OK);
        }

        @GetMapping("/{id}")
        public ResponseEntity<AccessAuditDTO> findById(@PathVariable Integer id) {
            AccessAuditDTO dtoResponse;
            AccessAudit obj = service.findById(id);

            if (obj == null) {
                throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
            } else {
                dtoResponse = mapper.map(obj, AccessAuditDTO.class);
            }
            return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
        }

        @PostMapping
        public ResponseEntity<Void> save(@Valid @RequestBody AccessAuditDTO dto) {
            AccessAudit p = service.save(mapper.map(dto, AccessAudit.class));
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(p.getIdAccessAudit()).toUri();
            return ResponseEntity.created(location).build();
        }

        @PutMapping("/{id}")
        public ResponseEntity<AccessAudit> update(@PathVariable Integer id, @Valid @RequestBody AccessAuditDTO dto) {
            // Verificar que el usuario exista
            AccessAudit obj = service.findById(id);
            if (obj == null) {
                throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
            }

            // Mapear el DTO a la entidad AccessAudit
            AccessAudit AccessAudit = mapper.map(dto, AccessAudit.class);

            // Asignar el ID desde la URL al objeto mapeado
            AccessAudit.setIdAccessAudit(id);

            // Actualizar el usuario
            return new ResponseEntity<>(service.update(AccessAudit), HttpStatus.OK);
        }


        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
            AccessAudit obj = service.findById(id);

            if (obj == null) {
                throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
            } else {
                service.delete(id);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }