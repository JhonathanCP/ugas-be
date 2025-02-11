package com.essalud.ugas.repo;

import com.essalud.ugas.model.Report;

import com.essalud.ugas.model.Module;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface IReportRepo extends IGenericRepo<Report, Integer> {

    Report findByName(String name);

    Report findByDescription(String description);

    List<Report> findByModule(Module module);

}