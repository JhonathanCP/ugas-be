package com.essalud.ugas.service.impl;

import com.essalud.ugas.model.Module;
import com.essalud.ugas.model.Report;
import com.essalud.ugas.repo.IGenericRepo;
import com.essalud.ugas.repo.IModuleRepo;
import com.essalud.ugas.service.IModuleService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleServiceImpl extends CRUDImpl<Module, Integer> implements IModuleService{

    @Autowired
    private IModuleRepo repo;

    @Override
    protected IGenericRepo<Module, Integer> getRepo() {
        return repo;
    }

    @Override
    public List<Module> isActiveAndHasActiveReports() {
        return repo.isActiveAndHasActiveReports();
    }

    @Override
    public List<Module> isActiveAndHasActiveReportsInProduction() {
        return repo.isActiveAndHasActiveReportsInProduction();
    }

    @Override
    public List<Report> getReportsByModule(Integer id) {
        return repo.findReportsByModuleId(id);
    }

}
