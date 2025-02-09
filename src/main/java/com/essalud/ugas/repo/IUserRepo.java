package com.essalud.ugas.repo;

import com.essalud.ugas.model.User;


public interface IUserRepo extends IGenericRepo<User, Integer>{

    //@Query("FROM User u WHERE u.username = :username")
    //DerivedQuery
    User findOneByUsername(String username);
}