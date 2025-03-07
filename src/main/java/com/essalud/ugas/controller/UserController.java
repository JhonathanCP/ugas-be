package com.essalud.ugas.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.essalud.ugas.service.IModuleService;
import com.essalud.ugas.service.IReportService;
import com.essalud.ugas.service.IUserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import com.essalud.ugas.dto.ModuleDTO;
import com.essalud.ugas.dto.ReportDTO;
import com.essalud.ugas.dto.UserDTO;
import com.essalud.ugas.exception.ModelNotFoundException;
import com.essalud.ugas.model.Report;
import com.essalud.ugas.model.User;
import com.essalud.ugas.model.Module;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IModuleService moduleService;

    @Autowired
    private IReportService reportService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> list = userService.findAll().stream().map(p -> mapper.map(p, UserDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Integer id) {
        UserDTO dtoResponse;
        User obj = userService.findById(id);

        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        } else {
            dtoResponse = mapper.map(obj, UserDTO.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PreAuthorize("@authServiceImpl.hasAccess('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody UserDTO dto) {
        User p = userService.save(mapper.map(dto, User.class));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(p.getIdUser()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Integer id, @Valid @RequestBody UserDTO dto) {
        // Verificar que el usuario exista
        User obj = userService.findById(id);
        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        }

        // Mapear el DTO a la entidad User
        User user = mapper.map(dto, User.class);

        // Asignar el ID desde la URL al objeto mapeado
        user.setIdUser(id);

        // Actualizar el usuario
        return new ResponseEntity<>(userService.update(user), HttpStatus.OK);
    }


    @PreAuthorize("@authServiceImpl.hasAccess('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        User obj = userService.findById(id);

        if (obj == null) {
            throw new ModelNotFoundException("ID DOES NOT EXIST: " + id);
        } else {
            userService.delete(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Métodos relacionados con los reportes de un usuario
    @PreAuthorize("@authServiceImpl.hasAccess('ADMIN')")
    @PostMapping("/{userId}/reports/{reportId}")
    public ResponseEntity<Void> addReportToUser(@PathVariable Integer userId, @PathVariable Integer reportId) {
        User user = userService.findById(userId);
        Report report = reportService.findById(reportId);

        if (user == null || report == null) {
            throw new ModelNotFoundException("User or Report not found");
        }

        userService.addReportToUser(user, report);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}/reports/{reportId}")
    @PreAuthorize("@authServiceImpl.hasAccess('ADMIN')")
    public ResponseEntity<Void> removeReportFromUser(@PathVariable Integer userId, @PathVariable Integer reportId) {
        User user = userService.findById(userId);
        Report report = reportService.findById(reportId);

        if (user == null || report == null) {
            throw new ModelNotFoundException("User or Report not found");
        }

        userService.removeReportFromUser(user, report);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{userId}/reports")
    public ResponseEntity<List<ReportDTO>> getReportsForUser(@PathVariable Integer userId) {
        User user = userService.findById(userId);

        if (user == null) {
            throw new ModelNotFoundException("User not found");
        }

        List<ReportDTO> reports = userService.getReportsForUser(user)
                                            .stream()
                                            .map(report -> mapper.map(report, ReportDTO.class))
                                            .collect(Collectors.toList());
        
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    // Métodos relacionados con los reportes favoritos de un usuario
    @PostMapping("/{userId}/favoriteReports/{reportId}")
    public ResponseEntity<Void> addFavoriteReportToUser(@PathVariable Integer userId, @PathVariable Integer reportId) {
        User user = userService.findById(userId);
        Report report = reportService.findById(reportId);

        if (user == null || report == null) {
            throw new ModelNotFoundException("User or Report not found");
        }

        userService.addFavoriteReportToUser(user, report);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}/favoriteReports/{reportId}")
    public ResponseEntity<Void> removeFavoriteReportFromUser(@PathVariable Integer userId, @PathVariable Integer reportId) {
        User user = userService.findById(userId);
        Report report = reportService.findById(reportId);

        if (user == null || report == null) {
            throw new ModelNotFoundException("User or Report not found");
        }

        userService.removeFavoriteReportFromUser(user, report);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{userId}/favoriteReports")
    public ResponseEntity<List<ReportDTO>> getFavoriteReportsForUser(@PathVariable Integer userId) {
        User user = userService.findById(userId);

        if (user == null) {
            throw new ModelNotFoundException("User not found");
        }

        List<ReportDTO> reports = userService.getFavoriteReportsForUser(user)
                                            .stream()
                                            .map(report -> mapper.map(report, ReportDTO.class))
                                            .collect(Collectors.toList());
        
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    // Métodos relacionados con los módulos de un usuario
    @PreAuthorize("@authServiceImpl.hasAccess('ADMIN')")
    @PostMapping("/{userId}/modules/{moduleId}")
    public ResponseEntity<Void> addReportsForModuleToUser(@PathVariable Integer userId, @PathVariable Integer moduleId) {
        User user = userService.findById(userId);

        if (user == null) {
            throw new ModelNotFoundException("User not found");
        }

        userService.addReportsForModuleToUser(user, moduleId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("@authServiceImpl.hasAccess('ADMIN')")
    @DeleteMapping("/{userId}/modules/{moduleId}")
    public ResponseEntity<Void> removeReportsForModuleFromUser(@PathVariable Integer userId, @PathVariable Integer moduleId) {
        User user = userService.findById(userId);

        if (user == null) {
            throw new ModelNotFoundException("User not found");
        }

        userService.removeReportsForModuleFromUser(user, moduleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("@authServiceImpl.hasAccess('ADMIN')")
    @PostMapping("/{userId}/all-reports")
    public ResponseEntity<Void> addAllReportsToUser(@PathVariable Integer userId) {
        User user = userService.findById(userId);

        if (user == null) {
            throw new ModelNotFoundException("User not found");
        }

        userService.addAllReportsToUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("@authServiceImpl.hasAccess('ADMIN')")
    @DeleteMapping("/{userId}/all-reports")
    public ResponseEntity<Void> removeAllReportsFromUser(@PathVariable Integer userId) {
        User user = userService.findById(userId);

        if (user == null) {
            throw new ModelNotFoundException("User not found");
        }

        userService.removeAllReportsFromUser(user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{userId}/modules")
    public ResponseEntity<List<ModuleDTO>> getModulesWithActiveReports(@PathVariable Integer userId) {
        User user = userService.findById(userId);
    
        if (user == null) {
            throw new ModelNotFoundException("User not found");
        }
    
        List<Module> modules = userService.getModulesWithActiveReports(user);
        List<ModuleDTO> moduleDTOs = modules.stream()
                                            .map(module -> mapper.map(module, ModuleDTO.class))
                                            .collect(Collectors.toList());
    
        return new ResponseEntity<>(moduleDTOs, HttpStatus.OK);
    }

    @GetMapping("/{userId}/module/{moduleId}/reports")
    public ResponseEntity<List<ReportDTO>> getActiveReportsFromModule(@PathVariable Integer userId, @PathVariable Integer moduleId) {
        User user = userService.findById(userId);
    
        if (user == null) {
            throw new ModelNotFoundException("User not found");
        }

        Module module = moduleService.findById(moduleId);

        if (module == null) {
            throw new ModelNotFoundException("Module not found");
        }
    
        List<Report> reports = userService.getActiveReportsFromModule(user, module);
        List<ReportDTO> reportDTOs = reports.stream()
                                            .map(report -> mapper.map(report, ReportDTO.class))
                                            .collect(Collectors.toList());
    
        return new ResponseEntity<>(reportDTOs, HttpStatus.OK);
    }

}