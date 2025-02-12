package com.essalud.ugas.service.impl;

import com.essalud.ugas.model.LoginAudit;
import com.essalud.ugas.repo.IGenericRepo;
import com.essalud.ugas.repo.ILoginAuditRepo;
import com.essalud.ugas.service.ILoginAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginAuditServiceImpl extends CRUDImpl<LoginAudit, Integer> implements ILoginAuditService{

    @Autowired
    private ILoginAuditRepo repo;

    @Override
    protected IGenericRepo<LoginAudit, Integer> getRepo() {
        return repo;
    }

}
