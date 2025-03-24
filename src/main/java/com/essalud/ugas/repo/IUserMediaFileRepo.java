package com.essalud.ugas.repo;

import java.util.List;

import com.essalud.ugas.model.User;
import com.essalud.ugas.model.UserMediaFile;
import com.essalud.ugas.model.UserMediaFilePK;

public interface IUserMediaFileRepo extends IGenericRepo<UserMediaFile, UserMediaFilePK>{

    // Encuentra todos los reportes favoritos de un usuario
    List<UserMediaFile> findByUser(User user);

}