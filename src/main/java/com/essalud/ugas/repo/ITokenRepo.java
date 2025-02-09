package com.essalud.ugas.repo;

import java.util.List;
import java.util.Optional;

import com.essalud.ugas.model.Token;

public interface ITokenRepo extends IGenericRepo<Token, Integer>{

    Optional<Token> findByToken(String token);

    void deleteByToken(String token);

    List<Token> findByUsernameAndIsValidTrue(String username);  // Obtener tokens válidos de un usuario

}
