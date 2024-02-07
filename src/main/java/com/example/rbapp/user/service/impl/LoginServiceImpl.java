package com.example.rbapp.user.service.impl;

import com.example.rbapp.api.service.jwt.JwtService;
import com.example.rbapp.user.controller.api.LoginRequest;
import com.example.rbapp.user.controller.api.LoginResponse;
import com.example.rbapp.user.entity.User;
import com.example.rbapp.user.service.LoginService;
import com.example.rbapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userService;

    @Override
    public LoginResponse processLogin(LoginRequest request) {
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(request.username(), request.password());
        Authentication authenticate = authenticationManager.authenticate(authenticationRequest);
        String token = jwtService.generateToken(request.username());
        User user = (User) userService.loadUserByUsername(request.username());
        List<String> authorities = authenticate.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return new LoginResponse(token, authorities, user.getId());
    }
}
