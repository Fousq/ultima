package com.example.rbapp.api.timepackage.method;

import com.example.rbapp.api.AbstractComponentTest;
import com.example.rbapp.timepackage.job.SubtractStudentTimeJob;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.rbapp.jooq.codegen.Tables.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubtractStudentTimeTest extends AbstractComponentTest {

    @Autowired
    private DSLContext dslContext;

    @Autowired
    private SubtractStudentTimeJob subtractStudentTimeJob;

    @Test
    void shouldSubtractStudentTime() {
        String type = "individual";
        Long studentId = createStudentInDB();
        Long timePackageId = createTimePackageInDB(type, studentId);
        createCourseSubjectInDB(type, studentId);

        subtractStudentTimeJob.execute();

        checkIfTimePackageIsSubtracted(timePackageId);
    }

    private void checkIfTimePackageIsSubtracted(Long timePackageId) {
        Long amount = dslContext.select(TIME_PACKAGE.AMOUNT_IN_MINUTES).from(TIME_PACKAGE)
                .where(TIME_PACKAGE.ID.eq(timePackageId)).fetchSingleInto(Long.class);
        assertEquals(20, amount);
    }

}
