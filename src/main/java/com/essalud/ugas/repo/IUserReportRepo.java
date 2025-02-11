package com.essalud.ugas.repo;

import com.essalud.ugas.model.Report;
import com.essalud.ugas.model.User;
import com.essalud.ugas.model.UserReport;
import com.essalud.ugas.model.UserReportPK;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface IUserReportRepo extends IGenericRepo<UserReport, UserReportPK> {

    // Verifica si existe una relación entre un usuario y un reporte
    boolean existsByUserAndReport(User user, Report report);

    // Encuentra todos los reportes asociados a un usuario
    List<UserReport> findByUser(User user);
}
