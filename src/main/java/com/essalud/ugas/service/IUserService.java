package com.essalud.ugas.service;

import java.util.List;

import com.essalud.ugas.model.Report;
import com.essalud.ugas.model.Module;
import com.essalud.ugas.model.User;

public interface IUserService extends ICRUD<User, Integer>{

    User findOneByUsername(String username);

    // Métodos relacionados con los reportes de un usuario
    void addReportToUser(User user, Report report);
    void removeReportFromUser(User user, Report report);
    List<Report> getReportsForUser(User user);

    // Métodos relacionados con los reportes favoritos de un usuario
    void addFavoriteReportToUser(User user, Report report);
    void removeFavoriteReportFromUser(User user, Report report);
    List<Report> getFavoriteReportsForUser(User user);

    // Métodos relacionados con los módulos de un usuario
    void addReportsForModuleToUser(User user, Integer moduleId);
    void removeReportsForModuleFromUser(User user, Integer moduleId);
    void addAllReportsToUser(User user);
    void removeAllReportsFromUser(User user);

    List<Module> getModulesWithActiveReports(User user);

}
