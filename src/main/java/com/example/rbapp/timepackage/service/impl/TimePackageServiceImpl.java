package com.example.rbapp.timepackage.service.impl;

import com.example.rbapp.api.exception.NotFoundException;
import com.example.rbapp.jooq.codegen.tables.records.TimePackageRecord;
import com.example.rbapp.student.service.StudentService;
import com.example.rbapp.timepackage.controller.api.CourseTimePackageResponse;
import com.example.rbapp.timepackage.controller.api.TimePackageResponse;
import com.example.rbapp.timepackage.controller.api.TimePackageSaveRequest;
import com.example.rbapp.timepackage.entity.TimePackage;
import com.example.rbapp.timepackage.exception.DuplicateTimePackageException;
import com.example.rbapp.timepackage.service.TimePackageMapper;
import com.example.rbapp.timepackage.service.TimePackageRepository;
import com.example.rbapp.timepackage.service.TimePackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimePackageServiceImpl implements TimePackageService {

    private final TimePackageRepository timePackageRepository;
    private final StudentService studentService;
    private final TimePackageMapper timePackageMapper;

    @Override
    public TimePackageResponse create(TimePackageSaveRequest request) {
        var studentId = studentService.getIdByPhone(request.studentPhone());
        checkForDuplication(request.type(), studentId);
        var timePackageRecord = timePackageMapper.mapSaveRequestToRecord(request, studentId);
        var timePackageId = timePackageRepository.create(timePackageRecord);
        return timePackageMapper.mapEntityToResponse(getById(timePackageId));
    }

    private void checkForDuplication(String type, Long studentId) {
        if (timePackageRepository.existsByTypeAndStudentId(type, studentId)) {
            throw new DuplicateTimePackageException();
        }
    }

    @Override
    public TimePackageResponse update(TimePackageSaveRequest request) {
        var timePackageRecord = getByStudentPhoneAndType(request.studentPhone(), request.type());
        var amount = timePackageRecord.getAmountInMinutes() + request.amount();
        var initialAmount = timePackageRecord.getInitialAmountInMinutes() + request.amount();
        timePackageRepository.updateAllTimeAmount(timePackageRecord.getId(), amount, initialAmount);
        return timePackageMapper.mapEntityToResponse(getById(timePackageRecord.getId()));
    }

    private TimePackageRecord getByStudentPhoneAndType(String studentPhone, String type) {
        return timePackageRepository.findByStudentPhoneAndType(studentPhone, type)
                .orElseThrow(() -> new NotFoundException("Time package wasn't found"));
    }

    private TimePackage getById(Long timePackageId) {
        return timePackageRepository.findById(timePackageId)
                .map(timePackageMapper::mapRecordToEntity)
                .orElseThrow(() -> new NotFoundException("Time package wasn't found"));
    }

    @Override
    public void subtractTimeForStudentsInCourse(Long courseId, int amount) {
        var timePackageRecords = timePackageRepository.findAllByCourseId(courseId);
        subtractAmountInTimePackages(timePackageRecords, amount);
        timePackageRepository.batchUpdateAmount(timePackageRecords);
    }

    @Override
    public TimePackage createStudentPackage(String email, String type, Integer amount) {
        Long studentId = studentService.getIdByEmail(email);
        TimePackageRecord timePackageRecord = new TimePackageRecord();
        timePackageRecord.setType(type);
        timePackageRecord.setAmountInMinutes(amount);
        timePackageRecord.setStudentId(studentId);
        Long id = timePackageRepository.create(timePackageRecord);

        return getById(id);
    }

    @Override
    public TimePackageResponse getUserTimePackageForCourse(Long userId, Long courseId) {
        return timePackageRepository.findByUserIdAndCourseId(userId, courseId)
                .map(timePackageMapper::mapRecordToResponse)
                .orElseThrow(() -> new NotFoundException("Time package not found"));
    }

    @Override
    public List<CourseTimePackageResponse> getStudentTimePackageList(Long studentId) {
        return timePackageRepository.findAllCourseTimePackageByStudentId(studentId);
    }

    private void subtractAmountInTimePackages(Collection<TimePackageRecord> timePackageRecords, int amount) {
        timePackageRecords.forEach(timePackageRecord ->
                timePackageRecord.setAmountInMinutes(timePackageRecord.getAmountInMinutes() - amount)
        );
    }
}
