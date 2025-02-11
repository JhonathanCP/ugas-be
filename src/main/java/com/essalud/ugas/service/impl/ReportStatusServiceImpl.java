package com.essalud.ugas.service.impl;

import com.essalud.ugas.model.ReportStatus;
import com.essalud.ugas.repo.IGenericRepo;
import com.essalud.ugas.repo.IReportStatusRepo;
import com.essalud.ugas.service.IReportStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportStatusServiceImpl extends CRUDImpl<ReportStatus, Integer> implements IReportStatusService{

    @Autowired
    private IReportStatusRepo repo;

    @Override
    protected IGenericRepo<ReportStatus, Integer> getRepo() {
        return repo;
    }

}
