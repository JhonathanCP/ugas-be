package com.essalud.ugas.service.impl;

import com.essalud.ugas.exception.ModelNotFoundException;
import com.essalud.ugas.model.Report;
import com.essalud.ugas.model.Module;
import com.essalud.ugas.model.User;
import com.essalud.ugas.model.UserFavoriteReport;
import com.essalud.ugas.model.UserFavoriteReportPK;
import com.essalud.ugas.model.UserReport;
import com.essalud.ugas.model.UserReportPK;
import com.essalud.ugas.repo.IGenericRepo;
import com.essalud.ugas.repo.IModuleRepo;
import com.essalud.ugas.repo.IReportRepo;
import com.essalud.ugas.repo.IUserFavoriteReportRepo;
import com.essalud.ugas.repo.IUserRepo;
import com.essalud.ugas.repo.IUserReportRepo;
import com.essalud.ugas.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends CRUDImpl<User, Integer> implements IUserService, UserDetailsService {

    @Autowired
    private IUserRepo userRepo;

    @Autowired
    private IUserReportRepo userReportRepo;

    @Autowired
    private IUserFavoriteReportRepo userFavoriteReportRepo;

    @Autowired
    private IReportRepo reportRepo;

    @Autowired
    private IModuleRepo moduleRepo;


    @Override
    protected IGenericRepo<User, Integer> getRepo() {
        return userRepo;
    }

    @Override
    public User findOneByUsername(String username) {
        return userRepo.findOneByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findOneByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User not exists", username));
        }

        List<GrantedAuthority> roles = new ArrayList<>();

        user.getRoles().forEach(rol -> {
            roles.add(new SimpleGrantedAuthority(rol.getName()));
        });

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getEnabled(), true, true, true, roles);
    }

    // Métodos relacionados con los reportes de un usuario
    @Override
    public void addReportToUser(User user, Report report) {
        if (user == null || report == null) {
            throw new IllegalArgumentException("User or Report cannot be null");
        }

        UserReport userReport = new UserReport(user, report);
        userReportRepo.save(userReport);
    }

    @Override
    public void removeReportFromUser(User user, Report report) {
        if (user == null || report == null) {
            throw new IllegalArgumentException("User or Report cannot be null");
        }

        UserReportPK userReportPK = new UserReportPK(user, report);
        userReportRepo.deleteById(userReportPK);
    }

    @Override
    public List<Report> getReportsForUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        List<UserReport> userReports = userReportRepo.findByUser(user);
        return userReports.stream().map(userReport -> userReport.getReport()).collect(Collectors.toList());
    }

    // Métodos relacionados con los reportes favoritos de un usuario
    @Override
    public void addFavoriteReportToUser(User user, Report report) {
        if (user == null || report == null) {
            throw new IllegalArgumentException("User or Report cannot be null");
        }

        UserFavoriteReport userFavoriteReport = new UserFavoriteReport(user, report);
        userFavoriteReportRepo.save(userFavoriteReport);
    }

    @Override
    public void removeFavoriteReportFromUser(User user, Report report) {
        if (user == null || report == null) {
            throw new IllegalArgumentException("User or Report cannot be null");
        }

        UserFavoriteReportPK userFavoriteReportPK = new UserFavoriteReportPK(user, report);
        userFavoriteReportRepo.deleteById(userFavoriteReportPK);
    }

    @Override
    public List<Report> getFavoriteReportsForUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        List<UserFavoriteReport> userFavoriteReports = userFavoriteReportRepo.findByUser(user);
        return userFavoriteReports.stream().map(userFavoriteReport -> userFavoriteReport.getReport()).collect(Collectors.toList());
    }

    // Métodos relacionados con los módulos de un usuario

    @Override
    public void addReportsForModuleToUser(User user, Integer moduleId) {
        if (user == null || moduleId == null) {
            throw new IllegalArgumentException("User or ModuleId cannot be null");
        }

        // Buscar el módulo en la base de datos
        Module module = moduleRepo.findById(moduleId)
                .orElseThrow(() -> new ModelNotFoundException("Module not found with ID: " + moduleId));

        // Obtener los reportes asociados al módulo
        List<Report> reports = reportRepo.findByModule(module);

        // Agregar los reportes al usuario
        reports.forEach(report -> addReportToUser(user, report));
    }

    @Override
    public void removeReportsForModuleFromUser(User user, Integer moduleId) {
        if (user == null || moduleId == null) {
            throw new IllegalArgumentException("User or ModuleId cannot be null");
        }

        // Buscar el módulo en la base de datos
        Module module = moduleRepo.findById(moduleId)
                .orElseThrow(() -> new ModelNotFoundException("Module not found with ID: " + moduleId));

        // Obtener los reportes asociados al módulo
        List<Report> reports = reportRepo.findByModule(module);

        // Remover los reportes del usuario
        reports.forEach(report -> removeReportFromUser(user, report));
    }


    @Override
    public void addAllReportsToUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        List<Report> allReports = reportRepo.findAll();
        allReports.forEach(report -> addReportToUser(user, report));
    }

    @Override
    public void removeAllReportsFromUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        List<Report> allReports = reportRepo.findAll();
        allReports.forEach(report -> removeReportFromUser(user, report));
    }

    @Override
    public List<Module> getModulesWithActiveReports(User user) {

        return userRepo.getModulesWithActiveReports(user);
        
    }

    @Override
    public List<Report> getActiveReportsFromModule(User user, Module module) {

        return userRepo.getActiveReportsFromModule(user, module);
        
    }
    
}