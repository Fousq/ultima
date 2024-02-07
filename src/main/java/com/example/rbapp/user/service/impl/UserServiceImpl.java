package com.example.rbapp.user.service.impl;

import com.example.rbapp.api.exception.NotFoundException;
import com.example.rbapp.api.service.jwt.JwtService;
import com.example.rbapp.jooq.codegen.tables.records.AppUserRecord;
import com.example.rbapp.user.controller.api.ChangePasswordRequest;
import com.example.rbapp.user.entity.User;
import com.example.rbapp.user.exception.UserPasswordNotMatchedException;
import com.example.rbapp.user.service.UserMapper;
import com.example.rbapp.user.service.UserRepository;
import com.example.rbapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(userMapper::mapRegistrationRequestToEntity)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public Long create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        AppUserRecord userRecord = userMapper.mapToRecord(user);
        return userRepository.create(userRecord);
    }

    @Override
    public void changePassword(ChangePasswordRequest request, String token) {
        String username = jwtService.extractUsername(token);
        userRepository.findByUsername(username).ifPresent(user -> {
            if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
                throw new UserPasswordNotMatchedException();
            }
            user.setPassword(passwordEncoder.encode(request.newPassword()));
            userRepository.updatePassword(user);
        });
    }

    @Override
    public User loadUserByToken(String token) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        return (User) loadUserByUsername(username);
    }

    @Override
    public String getUserFullName(Long userId) {
        return userRepository.findFullNameByUserId(userId);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<Long> getUserIdListForRoles(List<String> roles) {
        return userRepository.findAllIdByRoles(roles);
    }
}
