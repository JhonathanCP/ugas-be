package com.essalud.ugas.repo;

import com.essalud.ugas.model.Report;
import com.essalud.ugas.model.User;
import com.essalud.ugas.model.UserFavoriteReport;
import com.essalud.ugas.model.UserFavoriteReportPK;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface IUserFavoriteReportRepo extends IGenericRepo<UserFavoriteReport, UserFavoriteReportPK> {

    // Verifica si existe una relación de favorito entre un usuario y un reporte
    boolean existsByUserAndReport(User user, Report report);

    // Encuentra todos los reportes favoritos de un usuario
    List<UserFavoriteReport> findByUser(User user);
}
