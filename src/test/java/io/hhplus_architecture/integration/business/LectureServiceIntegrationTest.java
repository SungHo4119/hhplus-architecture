package io.hhplus_architecture.integration.business;

import static io.hhplus_architecture.domain.exception.message.ExceptionMessage.LECTURE_APPLY_ALREADY_EXISTS;
import static io.hhplus_architecture.domain.exception.message.ExceptionMessage.LECTURE_APPLY_CONFLICT;
import static io.hhplus_architecture.domain.exception.message.ExceptionMessage.LECTURE_NOT_FOUND;
import static io.hhplus_architecture.domain.exception.message.ExceptionMessage.USER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.hhplus_architecture.business.LectureService;
import io.hhplus_architecture.business.StudentService;
import io.hhplus_architecture.domain.entity.Lecture;
import io.hhplus_architecture.domain.entity.LectureHistory;
import io.hhplus_architecture.domain.entity.Student;
import io.hhplus_architecture.domain.exception.constom.AlreadyExistsException;
import io.hhplus_architecture.domain.exception.constom.ConflictException;
import io.hhplus_architecture.domain.exception.constom.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class LectureServiceIntegrationTest {

    @Autowired
    LectureService lectureService;

    @Autowired
    StudentService studentService;

    @Nested
    class ApplyLecture {

        @Test
        void applyLecture_USER_NOT_FOUND_오류() {
            // given
            Long studentId = 0L;
            Long lectureId = 0L;

            // when
            Exception e = assertThrows(ResourceNotFoundException.class,
                () -> lectureService.applyLecture(studentId, lectureId));
            // then
            assertEquals(USER_NOT_FOUND, e.getMessage());
        }

        @Test
        void applyLecture_LECTURE_NOT_FOUND_오류() {
            // given
            Student s = studentService.saveStudent("학생1");
            Long studentId = s.getId();
            Long lectureId = 0L;
            // when
            Exception e = assertThrows(ResourceNotFoundException.class,
                () -> lectureService.applyLecture(studentId, lectureId));
            // then
            assertEquals(LECTURE_NOT_FOUND, e.getMessage());
        }

        @Test
        void applyLecture_AlreadyExistsException_오류() {
            // given
            Student s = studentService.saveStudent("학생1");
            Lecture l = lectureService.saveLecture("강의1", "강사1", "2024-01-01");
            Long studentId = s.getId();
            Long lectureId = l.getId();

            lectureService.applyLecture(studentId, lectureId);
            // when
            Exception e = assertThrows(
                AlreadyExistsException.class,
                () -> lectureService.applyLecture(studentId, lectureId));
            //then
            assertEquals(LECTURE_APPLY_ALREADY_EXISTS, e.getMessage());
        }

        @Test
        void applyLecture_ConflictException_오류() {
            // given
            Student s = studentService.saveStudent("학생1");
            Lecture l = lectureService.saveLecture("강의1", "강사1", "2024-01-01");
            Long studentId = s.getId();
            Long lectureId = l.getId();

            for (int i = 2; i < 32; i++) {
                Student tempStudent = studentService.saveStudent("학생" + i);
                lectureService.applyLecture(tempStudent.getId(), lectureId);
            }

            // when
            Exception e = assertThrows(
                ConflictException.class,
                () -> lectureService.applyLecture(studentId, lectureId));
            //then
            assertEquals(LECTURE_APPLY_CONFLICT, e.getMessage());
        }

        @Test
        void applyLecture_성공() {
            // given
            Student s = studentService.saveStudent("학생1");
            Lecture l = lectureService.saveLecture("강의1", "강사1", "2024-01-01");
            Long studentId = s.getId();
            Long lectureId = l.getId();

            // when
            LectureHistory result = lectureService.applyLecture(studentId, lectureId);
            // then
            assertEquals(studentId, result.getStudent().getId());
            assertEquals(lectureId, result.getLecture().getId());
        }
    }

    @Nested
    class getLecturesApply {

        @Test
        void applyLecture_USER_NOT_FOUND_오류() {
            // given
            Long studentId = 0L;
            String date = "2024-01-01";
            // when
            Exception e = assertThrows(ResourceNotFoundException.class,
                () -> lectureService.getLecturesApply(studentId, date));
            // then
            assertEquals(USER_NOT_FOUND, e.getMessage());
        }

        @Test
        void getLecturesApply_성공_값있음() {
            // given
            List<Lecture> l = new ArrayList<>();
            String date = "2024-01-01";

            Student s = studentService.saveStudent("학생1");

            // 강의 셋팅 ( 4개 )
            for (int i = 1; i < 5; i++) {
                l.add(lectureService.saveLecture("강의" + i, "강사" + i, date));
            }

            // 정원의 꽉찬 강의 셋팅
            for (int i = 2; i < 32; i++) {
                Student tempStudent = studentService.saveStudent("학생" + i);
                lectureService.applyLecture(tempStudent.getId(), l.get(0).getId());
            }

            // 학생이 강의 신청 셋팅
            lectureService.applyLecture(s.getId(), l.get(1).getId());

            // when
            List<Lecture> result = lectureService.getLecturesApply(s.getId(), date);
            // then
            assertEquals(2, result.size());
            assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(l.get(2));
            assertThat(result.get(1)).usingRecursiveComparison().isEqualTo(l.get(3));

        }
    }

    @Nested
    public class getLectureHistory {

        @Test
        void getLectureHistory_실패_USER_NOT_FOUND() {
            // given
            Long studentId = 1L;

            // when
            Exception e = assertThrows(ResourceNotFoundException.class,
                () -> lectureService.getLectureHistory(studentId));

            // then
            assertEquals(USER_NOT_FOUND, e.getMessage());
        }

        @Test
        void getLectureHistory_성공() {
            // given
            // 학생 셋팅
            Student s = studentService.saveStudent("학생1");
            // 강의 셋팅
            Lecture l = lectureService.saveLecture("강의1", "강사1", "2024-01-01");
            // 강의 신청
            LectureHistory lh = lectureService.applyLecture(s.getId(), l.getId());

            // when
            List<LectureHistory> result = lectureService.getLectureHistory(s.getId());

            // then
            assertEquals(1, result.size());
            assertThat(lh).usingRecursiveComparison().isEqualTo(result.get(0));
        }
    }
}