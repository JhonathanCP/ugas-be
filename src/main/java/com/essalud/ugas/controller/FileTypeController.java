package com.essalud.ugas.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.essalud.ugas.service.IFileTypeService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import com.essalud.ugas.dto.FileTypeDTO;
import com.essalud.ugas.exception.ModelNotFoundException;
import com.essalud.ugas.model.FileType;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@PreAuthorize("@authServiceImpl.hasAccess('ADMIN')")
@RequestMapping("/file-types")
public class FileTypeController {

    @Autowired
    private IFileTypeService service;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<FileTypeDTO>> findAll() {
        List<FileTypeDTO> list = service.findAll().stream().map(p -> mapper.map(p, FileTypeDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileTypeDTO> findById(@PathVariable Integer id) {
        FileTypeDTO dtoResponse;
        FileType obj = service.findById(id);

        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        } else {
            dtoResponse = mapper.map(obj, FileTypeDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody FileTypeDTO dto) {
        FileType p = service.save(mapper.map(dto, FileType.class));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(p.getIdFileType()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<FileType> update(@PathVariable Integer id, @Valid @RequestBody FileTypeDTO dto) {
        // Verificar que el usuario exista
        FileType obj = service.findById(id);
        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }

        // Mapear el DTO a la entidad FileType
        FileType FileType = mapper.map(dto, FileType.class);

        // Asignar el ID desde la URL al objeto mapeado
        FileType.setIdFileType(id);

        // Actualizar el usuario
        return new ResponseEntity<>(service.update(FileType), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        FileType obj = service.findById(id);

        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        } else {
            service.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<Void> updateImage(@PathVariable Integer id, @RequestParam("image") MultipartFile imageFile) {
        FileType obj = service.findById(id);
        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }

        try {
            obj.setImage(imageFile.getBytes());
            service.update(obj);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}