package com.example.rbapp.user.service;

import com.example.rbapp.config.AppMapperConfig;
import com.example.rbapp.jooq.codegen.tables.records.AppUserRecord;
import com.example.rbapp.user.controller.api.UserResponse;
import com.example.rbapp.api.service.registration.RegistrationRequest;
import com.example.rbapp.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(config = AppMapperConfig.class)
public interface UserMapper {

    User mapRegistrationRequestToEntity(RegistrationRequest request);

    default User mapRegistrationRequestToEntity(RegistrationRequest request, String grantType) {
        User user = mapRegistrationRequestToEntity(request);
        user.setGrantType(grantType);
        return user;
    }

    UserResponse mapToResponse(User user);

    AppUserRecord mapToRecord(User user);

    User mapRegistrationRequestToEntity(AppUserRecord userRecord);
}
