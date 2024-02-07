package com.example.rbapp.timepackage.service;

import com.example.rbapp.config.AppMapperConfig;
import com.example.rbapp.jooq.codegen.tables.records.TimePackageRecord;
import com.example.rbapp.timepackage.controller.api.TimePackageResponse;
import com.example.rbapp.timepackage.controller.api.TimePackageSaveRequest;
import com.example.rbapp.timepackage.entity.TimePackage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = AppMapperConfig.class)
public interface TimePackageMapper {

    @Mapping(target = "amount", source = "amountInMinutes")
    @Mapping(target = "initialAmount", source = "initialAmountInMinutes")
    TimePackageResponse mapRecordToResponse(TimePackageRecord record);

    List<TimePackageResponse> mapRecordToResponse(List<TimePackageRecord> timePackageRecords);

    @Mapping(target = "amountInMinutes", source = "amount")
    TimePackageRecord mapSaveRequestToRecord(TimePackageSaveRequest request);

    default TimePackageRecord mapSaveRequestToRecord(TimePackageSaveRequest request, Long studentId) {
        TimePackageRecord timePackageRecord = mapSaveRequestToRecord(request);
        timePackageRecord.setStudentId(studentId);
        return timePackageRecord;
    }

    @Mapping(target = "amount", source = "amountInMinutes")
    @Mapping(target = "initialAmount", source = "initialAmountInMinutes")
    TimePackage mapRecordToEntity(TimePackageRecord timePackageRecord);

    TimePackageResponse mapEntityToResponse(TimePackage timePackage);
}
