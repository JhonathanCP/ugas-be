package com.essalud.ugas.repo;

import com.essalud.ugas.model.User;
import com.essalud.ugas.model.UserReport;
import com.essalud.ugas.model.UserReportPK;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface IUserReportRepo extends IGenericRepo<UserReport, UserReportPK> {

    List<UserReport> findByUser(User user);  // Método de consulta

}