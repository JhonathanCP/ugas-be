package com.essalud.ugas.service;

import java.util.List;

import com.essalud.ugas.model.Module;
import com.essalud.ugas.model.Report;

public interface IModuleService extends ICRUD<Module, Integer>{

    List<Module> isActiveAndHasActiveReports();

    List<Module> isActiveAndHasActiveReportsInProduction();

    List<Report> getReportsByModule(Integer id);

}
