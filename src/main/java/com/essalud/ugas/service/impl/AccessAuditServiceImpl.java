package com.essalud.ugas.service.impl;

import com.essalud.ugas.model.AccessAudit;
import com.essalud.ugas.repo.IGenericRepo;
import com.essalud.ugas.repo.IAccessAuditRepo;
import com.essalud.ugas.service.IAccessAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccessAuditServiceImpl extends CRUDImpl<AccessAudit, Integer> implements IAccessAuditService{

    @Autowired
    private IAccessAuditRepo repo;

    @Override
    protected IGenericRepo<AccessAudit, Integer> getRepo() {
        return repo;
    }

}
