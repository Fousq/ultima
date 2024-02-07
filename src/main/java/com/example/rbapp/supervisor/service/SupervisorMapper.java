package com.example.rbapp.supervisor.service;

import com.example.rbapp.api.service.registration.RegistrationRequest;
import com.example.rbapp.config.AppMapperConfig;
import com.example.rbapp.jooq.codegen.tables.records.SupervisorRecord;
import com.example.rbapp.supervisor.contoller.api.SupervisorResponse;
import com.example.rbapp.supervisor.entity.Supervisor;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = AppMapperConfig.class)
public interface SupervisorMapper {


    Supervisor mapRegistrationToEntity(RegistrationRequest entity);

    SupervisorRecord mapEntityToRecord(Supervisor supervisor);

    SupervisorResponse mapRecordToResponse(SupervisorRecord supervisorRecord);

    List<SupervisorResponse> mapRecordToResponse(List<SupervisorRecord> supervisors);
}
