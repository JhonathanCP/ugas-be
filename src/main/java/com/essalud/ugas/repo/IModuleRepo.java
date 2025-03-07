package com.essalud.ugas.repo;

import com.essalud.ugas.model.Module;
import com.essalud.ugas.model.Report;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface IModuleRepo extends IGenericRepo<Module, Integer> {

    @Query("SELECT m FROM Module m WHERE m.active = true AND EXISTS (SELECT r FROM Report r WHERE r.module = m AND r.active = true)")
    List<Module> isActiveAndHasActiveReports();

    @Query("SELECT m FROM Module m WHERE m.active = true AND EXISTS " + "(SELECT r FROM Report r WHERE r.module = m AND r.active = true AND r.reportStatus.idReportStatus = 1)")
    List<Module> isActiveAndHasActiveReportsInProduction();

    @Query("SELECT r FROM Report r WHERE r.module.idModule = :id AND r.active = true")
    List<Report> findReportsByModuleId(@Param("id") Integer id);

}