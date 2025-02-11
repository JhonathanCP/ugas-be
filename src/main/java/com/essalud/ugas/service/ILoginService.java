package com.essalud.ugas.service;

import com.essalud.ugas.model.User;

public interface ILoginService {

    // void addLoginAudit(String username);
    User checkUsername(String username);
    void changePassword(String password, String username);
}
