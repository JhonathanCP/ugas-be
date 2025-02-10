package com.essalud.ugas.repo;

import com.essalud.ugas.model.User;
import com.essalud.ugas.model.UserModule;
import com.essalud.ugas.model.UserModulePK;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface IUserModuleRepo extends IGenericRepo<UserModule, UserModulePK> {

    List<UserModule> findByUser(User user);  // Método de consulta

}