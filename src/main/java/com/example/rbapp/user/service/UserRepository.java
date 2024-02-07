package com.example.rbapp.user.service;

import com.example.rbapp.jooq.codegen.tables.records.AppUserRecord;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<AppUserRecord> findByUsername(String username);

    Long create(AppUserRecord user);

    Optional<AppUserRecord> findByUsernameAndPassword(String username, String password);

    void updatePassword(AppUserRecord user);

    String findFullNameByUserId(Long userId);

    void deleteById(Long id);

    List<Long> findAllIdByRoles(List<String> roles);
}
