package com.essalud.ugas.repo;

import com.essalud.ugas.model.User;
import com.essalud.ugas.model.UserFavoriteReport;
import com.essalud.ugas.model.UserFavoriteReportPK;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface IUserFavoriteReportRepo extends IGenericRepo<UserFavoriteReport, UserFavoriteReportPK> {

    List<UserFavoriteReport> findByUser(User user);  // Método de consulta

}