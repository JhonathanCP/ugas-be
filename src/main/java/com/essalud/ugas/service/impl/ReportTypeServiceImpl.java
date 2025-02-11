package com.essalud.ugas.service.impl;

import com.essalud.ugas.model.ReportType;
import com.essalud.ugas.repo.IGenericRepo;
import com.essalud.ugas.repo.IReportTypeRepo;
import com.essalud.ugas.service.IReportTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportTypeServiceImpl extends CRUDImpl<ReportType, Integer> implements IReportTypeService{

    @Autowired
    private IReportTypeRepo repo;

    @Override
    protected IGenericRepo<ReportType, Integer> getRepo() {
        return repo;
    }

}
