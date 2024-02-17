package com.example.rbapp.user.service.recordmapper;

import com.example.rbapp.user.controller.api.UserRoleResponse;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import static com.example.rbapp.jooq.codegen.Tables.APP_USER;

@Component
public class UserRoleRecordMapper implements RecordMapper<Record, UserRoleResponse> {

    @Override
    public UserRoleResponse map(Record record) {
        Long id = record.getValue(APP_USER.ID);
        String role = record.getValue(APP_USER.GRANT_TYPE);

        return new UserRoleResponse(id, role);
    }
}
