package com.example.rbapp.user.service;

import com.example.rbapp.user.controller.api.ChangePasswordRequest;
import com.example.rbapp.user.controller.api.UserRoleResponse;
import com.example.rbapp.user.controller.api.UserRolesRequest;
import com.example.rbapp.user.entity.User;

import java.util.List;

public interface UserService {
    Long create(User user);

    void changePassword(ChangePasswordRequest request, String token);

    User loadUserByToken(String token);

    String getUserFullName(Long userId);

    void delete(Long id);

    List<Long> getUserIdListForRoles(List<String> role);

    List<UserRoleResponse> getUserRoles(UserRolesRequest request);
}
