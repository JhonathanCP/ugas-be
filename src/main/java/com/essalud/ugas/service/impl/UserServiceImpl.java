package com.essalud.ugas.service.impl;

import com.essalud.ugas.model.User;
import com.essalud.ugas.repo.IGenericRepo;
import com.essalud.ugas.repo.IUserRepo;
import com.essalud.ugas.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends CRUDImpl<User, Integer> implements IUserService, UserDetailsService {

    @Autowired
    private IUserRepo userRepo;

    @Override
    protected IGenericRepo<User, Integer> getRepo() {
        return userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findOneByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User not exists", username));
        }

        List<GrantedAuthority> roles = new ArrayList<>();

        user.getRoles().forEach(rol -> {
            roles.add(new SimpleGrantedAuthority(rol.getName()));
        });

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true, roles);
    }
    
}