package com.essalud.ugas.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.essalud.ugas.service.IUserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import com.essalud.ugas.dto.UserDTO;
import com.essalud.ugas.exception.ModelNotFoundException;
import com.essalud.ugas.model.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService service;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> list = service.findAll().stream().map(p -> mapper.map(p, UserDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Integer id) {
        UserDTO dtoResponse;
        User obj = service.findById(id);

        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        } else {
            dtoResponse = mapper.map(obj, UserDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody UserDTO dto) {
        User p = service.save(mapper.map(dto, User.class));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(p.getIdUser()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Integer id, @Valid @RequestBody UserDTO dto) {
        // Verificar que el usuario exista
        User obj = service.findById(id);
        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }

        // Mapear el DTO a la entidad User
        User user = mapper.map(dto, User.class);

        // Asignar el ID desde la URL al objeto mapeado
        user.setIdUser(id);

        // Actualizar el usuario
        return new ResponseEntity<>(service.update(user), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        User obj = service.findById(id);

        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        } else {
            service.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}