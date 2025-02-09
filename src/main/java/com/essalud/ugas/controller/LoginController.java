package com.essalud.ugas.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.essalud.ugas.security.JwtRequest;
import com.essalud.ugas.security.JwtResponse;
import com.essalud.ugas.security.JwtTokenUtil;
import com.essalud.ugas.security.JwtUserDetailsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;

    // Endpoint para login
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest req) throws Exception {
        // Autenticar las credenciales del usuario
        authenticate(req.getUsername(), req.getPassword());

        // Cargar los detalles del usuario
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(req.getUsername());

        // Generar el access token
        final String accessToken = jwtTokenUtil.generateToken(userDetails);

        // Generar el refresh token
        final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

        // Devolver ambos tokens en la respuesta
        return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponse> refreshToken(@RequestBody Map<String, String> tokenRequest) throws Exception {
        // Extraer el refresh token del cuerpo de la solicitud
        String refreshToken = tokenRequest.get("refresh_token");

        // Validar si el refresh token es nulo o está vacío
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new JwtResponse(null, "Refresh token is missing"));
        }

        // Validar si es un Refresh Token
        if (!jwtTokenUtil.isRefreshToken(refreshToken)) {
            return ResponseEntity.badRequest().body(new JwtResponse(null, "Invalid token: Expected refresh token"));
        }

        // Validar si el refresh token ha expirado
        if (jwtTokenUtil.isTokenExpired(refreshToken)) {
            return ResponseEntity.badRequest().body(new JwtResponse(null, "Refresh token has expired"));
        }

        // Extraer el nombre de usuario desde el refresh token
        String username = jwtTokenUtil.getUsernameFromToken(refreshToken);

        // Cargar los detalles del usuario
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);

        // Generar un nuevo access token usando los detalles del usuario
        final String newAccessToken = jwtTokenUtil.generateToken(userDetails);

        // Devolver el nuevo access token junto con el mismo refresh token
        return ResponseEntity.ok(new JwtResponse(newAccessToken, refreshToken));
    }

    // Endpoint para logout
    @PostMapping("/exit")
    public ResponseEntity<String> logout(@RequestBody Map<String, String> tokenRequest) {
        String accessToken = tokenRequest.get("access_token");
        String refreshToken = tokenRequest.get("refresh_token");

        if (accessToken != null && !accessToken.trim().isEmpty()) {
            // Invalidar el access token en la base de datos
            jwtTokenUtil.revokeToken(accessToken);
        }

        if (refreshToken != null && !refreshToken.trim().isEmpty()) {
            // Invalidar el refresh token en la base de datos
            jwtTokenUtil.revokeToken(refreshToken);
        }

        return ResponseEntity.ok("Logged out successfully");
    }
    

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
