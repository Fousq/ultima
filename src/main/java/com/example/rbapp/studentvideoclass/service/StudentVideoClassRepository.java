package com.example.rbapp.studentvideoclass.service;

import com.example.rbapp.jooq.codegen.tables.records.StudentVideoClassRecord;
import com.example.rbapp.studentvideoclass.entity.StudentVideoClass;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.InsertSetMoreStep;
import org.jooq.UpdateConditionStep;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.rbapp.jooq.codegen.Tables.STUDENT_VIDEO_CLASS;

@Repository
@RequiredArgsConstructor
public class StudentVideoClassRepository {

    private final DSLContext dslContext;

    public List<StudentVideoClassRecord> findAllByVideoClassId(Long videoClassId) {
        return dslContext.selectFrom(STUDENT_VIDEO_CLASS)
                .where(STUDENT_VIDEO_CLASS.VIDEO_CLASS_ID.eq(videoClassId))
                .fetchInto(StudentVideoClassRecord.class);
    }

    public void batchUpdate(List<StudentVideoClassRecord> studentVideoClassList) {
        var update = studentVideoClassList.stream()
                .map(studentVideoClassRecord ->
                        dslContext.update(STUDENT_VIDEO_CLASS)
                                .set(STUDENT_VIDEO_CLASS.VISITED, studentVideoClassRecord.getVisited())
                                .where(STUDENT_VIDEO_CLASS.STUDENT_ID.eq(studentVideoClassRecord.getStudentId()))
                                .and(STUDENT_VIDEO_CLASS.VIDEO_CLASS_ID.eq(studentVideoClassRecord.getVideoClassId()))
                ).toList();
        dslContext.batch(update).execute();
    }

    public void deleteAll(List<Long> studentIds, Long videoClassId) {
        dslContext.deleteFrom(STUDENT_VIDEO_CLASS)
                .where(STUDENT_VIDEO_CLASS.VIDEO_CLASS_ID.eq(videoClassId))
                .and(STUDENT_VIDEO_CLASS.STUDENT_ID.in(studentIds))
                .execute();
    }

    public void batchCreate(List<StudentVideoClassRecord> studentVideoClassRecords, Long videoClassId) {
        var insert = studentVideoClassRecords.stream()
                .map(studentVideoClassRecord -> dslContext.insertInto(STUDENT_VIDEO_CLASS)
                        .set(STUDENT_VIDEO_CLASS.VIDEO_CLASS_ID, videoClassId)
                        .set(STUDENT_VIDEO_CLASS.STUDENT_ID, studentVideoClassRecord.getStudentId())
                        .set(STUDENT_VIDEO_CLASS.VISITED, studentVideoClassRecord.getVisited())
                )
                .toList();
        dslContext.batch(insert).execute();
    }
}
