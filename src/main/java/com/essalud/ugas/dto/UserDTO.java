package com.essalud.ugas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserDTO {

    @EqualsAndHashCode.Include
    private Integer idUser;

    @NotNull
    @Size(max = 50)
    private String username;

    @NotNull
    @Email
    @Size(max = 250)
    private String email;

    @NotNull
    @Size(max = 80)
    private String password;

    @NotNull
    private Boolean ldap;

    @NotNull
    private Boolean enabled;

    @NotNull
    private List<RoleDTO> roles;

}