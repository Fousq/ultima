package com.example.rbapp.head.teacher.service;

import com.example.rbapp.api.exception.NotFoundException;
import com.example.rbapp.head.teacher.entity.HeadTeacher;
import com.example.rbapp.jooq.codegen.tables.records.HeadTeacherRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeadTeacherService {

    private final HeadTeacherMapper headTeacherMapper;
    private final HeadTeacherRepository headTeacherRepository;

    public HeadTeacher create(HeadTeacher headTeacher) {
        HeadTeacherRecord headTeacherRecord = headTeacherMapper.mapEntityToRecord(headTeacher);
        Long id = headTeacherRepository.create(headTeacherRecord);
        return getById(id);
    }

    private HeadTeacher getById(Long id) {
        return headTeacherRepository.findById(id)
                .map(headTeacherMapper::mapRecordToEntity)
                .orElseThrow(() -> new NotFoundException("Head teacher not found"));
    }

    public void updateBitrix(HeadTeacher headTeacher, Long bitrixId) {
        headTeacherRepository.updateBitrixIdById(bitrixId, headTeacher.getId());
    }

    public boolean existsByUserId(Long userId) {
        return headTeacherRepository.existsByUserId(userId);
    }

    public void deleteByUserId(Long userId) {
        headTeacherRepository.deleteByUserId(userId);
    }
}
