package com.essalud.ugas.service.impl;

import com.essalud.ugas.model.Report;
import com.essalud.ugas.repo.IGenericRepo;
import com.essalud.ugas.repo.IReportRepo;
import com.essalud.ugas.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl extends CRUDImpl<Report, Integer> implements IReportService{

    @Autowired
    private IReportRepo repo;

    @Override
    protected IGenericRepo<Report, Integer> getRepo() {
        return repo;
    }

}
