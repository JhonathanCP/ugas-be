package com.essalud.ugas.controller;

import java.util.Map;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.essalud.ugas.dto.UserDTO;
import com.essalud.ugas.model.User;
import com.essalud.ugas.security.JwtRequest;
import com.essalud.ugas.security.JwtResponse;
import com.essalud.ugas.security.JwtTokenUtil;
import com.essalud.ugas.security.JwtUserDetailsService;
import com.essalud.ugas.service.IUserService;
import com.essalud.ugas.service.impl.RecaptchaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final RecaptchaService recaptchaService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IUserService userService;

    // Endpoint para login
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest req) throws Exception {
        // Verificar el token de reCAPTCHA
        if (!recaptchaService.verifyRecaptcha(req.getRecaptchaToken())) {
            return ResponseEntity.badRequest().body(new JwtResponse(null, "Invalid reCAPTCHA token"));
        }

        // Obtener el usuario desde el servicio
        User obj = userService.findOneByUsername(req.getUsername().toLowerCase());
        UserDTO user = mapper.map(obj, UserDTO.class);

        // Check if userLdap is true
        if (user != null && user.getLdap()) {
            // Perform LDAP authentication
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> ldapRequest = Map.of(
                "username", req.getUsername().toLowerCase(),
                "password", req.getPassword(),
                "systemcode", "SGS"
            );

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(ldapRequest, headers);

            ResponseEntity<String> ldapResponse = restTemplate.exchange(
                "http://10.0.1.230:8089/login",
                HttpMethod.POST,
                entity,
                String.class
            );
            
            

            if (ldapResponse.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.badRequest().body(new JwtResponse(null, "LDAP authentication failed"));
            }
        } else {
            // Autenticar las credenciales del usuario
            authenticate(req.getUsername().toLowerCase(), req.getPassword());
        }

        // Cargar los detalles del usuario
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(req.getUsername().toLowerCase());

        // Generar el access token
        final String accessToken = jwtTokenUtil.generateToken(userDetails);

        // Generar el refresh token
        final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

        // Devolver ambos tokens en la respuesta
        return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
    }

    // Endpoint para refrescar el token
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

        // Generar un nuevo refresh token
        final String newRefreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

        // Devolver el nuevo access token junto con el nuevo refresh token
        return ResponseEntity.ok(new JwtResponse(newAccessToken, newRefreshToken));
    }

    // Endpoint para dar de baja el token
    @PostMapping("/logout-token")
    public ResponseEntity<String> logout(@RequestBody Map<String, String> tokenRequest) throws Exception{
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
            throw new Exception("Usuario deshabilitado", e);
        } catch (BadCredentialsException e) {
            throw new Exception("Credenciales inválidas", e);
        }
    }
}