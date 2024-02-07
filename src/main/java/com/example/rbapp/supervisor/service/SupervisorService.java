package com.example.rbapp.supervisor.service;

import com.example.rbapp.api.exception.NotFoundException;
import com.example.rbapp.jooq.codegen.tables.records.SupervisorRecord;
import com.example.rbapp.supervisor.contoller.api.SupervisorResponse;
import com.example.rbapp.supervisor.entity.Supervisor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupervisorService {

    private final SupervisorRepository supervisorRepository;
    private final SupervisorMapper supervisorMapper;

    public Long create(Supervisor supervisor) {
        SupervisorRecord supervisorRecord = supervisorMapper.mapEntityToRecord(supervisor);
        return supervisorRepository.create(supervisorRecord);
    }

    public void updateBitrixContact(Long bitrixId, Long supervisorId) {
        supervisorRepository.updateBitrixIdBySupervisorId(bitrixId, supervisorId);
    }

    public List<SupervisorResponse> getAll() {
        List<SupervisorRecord> supervisors = supervisorRepository.findAll();
        return supervisorMapper.mapRecordToResponse(supervisors);
    }

    public SupervisorResponse getById(Long id) {
        return supervisorRepository.findById(id)
                .map(supervisorMapper::mapRecordToResponse)
                .orElseThrow(() -> new NotFoundException("Supervisor not found"));
    }

    public boolean existsByUserId(Long userId) {
        return supervisorRepository.existsByUserId(userId);
    }

    public void deleteByUserId(Long userId) {
        supervisorRepository.deleteByUserId(userId);
    }
}
