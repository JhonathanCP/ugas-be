package com.essalud.ugas.repo;

import com.essalud.ugas.model.LoginAudit;
import org.springframework.stereotype.Repository;

@Repository
public interface ILoginAuditRepo extends IGenericRepo<LoginAudit, Integer> {

}