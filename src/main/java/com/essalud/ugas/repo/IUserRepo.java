package com.essalud.ugas.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.essalud.ugas.model.User;
import com.essalud.ugas.model.Module;


public interface IUserRepo extends IGenericRepo<User, Integer>{

    //@Query("FROM User u WHERE u.username = :username")
    //DerivedQuery
    User findOneByUsername(String username);

    @Query("SELECT DISTINCT m FROM Module m JOIN Report r ON m.idModule = r.module.idModule JOIN UserReport ur ON ur.report.idReport = r.idReport JOIN User u ON u.idUser = ur.user.idUser WHERE u = :user AND r.active = true")
    List<Module> getModulesWithActiveReports(User user);
}