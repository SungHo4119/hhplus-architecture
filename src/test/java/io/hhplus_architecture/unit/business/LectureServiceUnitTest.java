package io.hhplus_architecture.unit.business;

import static io.hhplus_architecture.domain.exception.message.ExceptionMessage.LECTURE_APPLY_ALREADY_EXISTS;
import static io.hhplus_architecture.domain.exception.message.ExceptionMessage.LECTURE_APPLY_CONFLICT;
import static io.hhplus_architecture.domain.exception.message.ExceptionMessage.LECTURE_NOT_FOUND;
import static io.hhplus_architecture.domain.exception.message.ExceptionMessage.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import io.hhplus_architecture.business.LectureService;
import io.hhplus_architecture.business.interfaces.ILectureHistoryRepository;
import io.hhplus_architecture.business.interfaces.ILectureRepository;
import io.hhplus_architecture.business.interfaces.IStudentRepository;
import io.hhplus_architecture.domain.entity.Lecture;
import io.hhplus_architecture.domain.entity.LectureCount;
import io.hhplus_architecture.domain.entity.LectureHistory;
import io.hhplus_architecture.domain.entity.Student;
import io.hhplus_architecture.domain.exception.constom.AlreadyExistsException;
import io.hhplus_architecture.domain.exception.constom.ConflictException;
import io.hhplus_architecture.domain.exception.constom.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LectureServiceUnitTest {

    @Mock
    ILectureRepository lectureRepository;
    @Mock
    IStudentRepository studentRepository;
    @Mock
    ILectureHistoryRepository lectureHistoryRepository;

    @InjectMocks
    LectureService lectureService;


    @BeforeEach
    void setUp() {
        // Mock 객체를 초기화
        // @Mock 어노테이션이 붙은 객체들을 초기화하고 @InjectMocks 어노테이션이 붙은 객체에 주입
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class ApplyLecture {


        @Test
        void applyLecture_실패_학생이_존재_하지_않음() {
            // given
            Long studentId = 1L;
            Long lectureId = 1L;
            // 유저 정보 조회
            when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
            // when
            Exception e = assertThrows(
                ResourceNotFoundException.class,
                () -> lectureService.applyLecture(studentId, lectureId));
            // then
            assertEquals(USER_NOT_FOUND, e.getMessage());
        }

        @Test
        void applyLecture_실패_특강이_존재_하지_않음() {
            // given
            Long studentId = 1L;
            Long lectureId = 1L;
            Student s = Student.builder().id(1L).name("테스트").build();

            when(studentRepository.findById(studentId)).thenReturn(Optional.of(s));
            when(lectureRepository.findById(lectureId)).thenReturn(Optional.empty());
            // when
            Exception e = assertThrows(
                ResourceNotFoundException.class,
                () -> lectureService.applyLecture(studentId, lectureId));
            // then
            assertEquals(LECTURE_NOT_FOUND, e.getMessage());
        }

        @Test
        void applyLecture_실패_정원_초과() {
            // given
            Long studentId = 1L;
            Long lectureId = 1L;
            Long lectureCountId = 1L;
            Integer lectureCount = 30;

            Student s = Student.builder().id(studentId).name("테스트").build();
            Lecture l = Lecture.builder().id(lectureId).lectureName("특강명").lectureDate("2024-01-01")
                .build();
            LectureCount lc = LectureCount.builder().id(lectureCountId).lectureId(lectureId)
                .count(lectureCount).build();

            // 유저 정보 조회
            when(studentRepository.findById(studentId)).thenReturn(Optional.of(s));
            // 특강 정보 조회
            when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(l));

            // 특강 정원 조회
            when(lectureRepository.findLectureCountById(lectureId)).thenReturn(lc);

            // when
            Exception e = assertThrows(
                ConflictException.class, () -> lectureService.applyLecture(studentId, lectureId));
            // then
            assertEquals(LECTURE_APPLY_CONFLICT, e.getMessage());
        }

        @Test
        void applyLecture_실패_수강_신청_이력_있음() {
            // given
            Long studentId = 1L;
            Long lectureId = 1L;
            Long lectureCountId = 1L;
            Integer lectureCount = 0;

            Student s = Student.builder().id(1L).name("테스트").build();
            Lecture l = Lecture.builder().id(1L).lectureName("특강명").lectureDate("2024-01-01")
                .build();
            LectureCount lc = LectureCount.builder().id(lectureCountId).lectureId(lectureId)
                .count(lectureCount).build();

            LectureHistory lh = LectureHistory.builder().id(1L).lecture(l).student(s).build();
            // 유저 정보 조회
            when(studentRepository.findById(studentId)).thenReturn(Optional.of(s));
            // 특강 정보 조회
            when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(l));

            // 특강 정원 조회
            when(lectureRepository.findLectureCountById(lectureId)).thenReturn(lc);

            // 특강 신청 정보 조회
            when(lectureHistoryRepository.findByLectureId(lectureId)).thenReturn(List.of(lh));

            // when
            Exception e = assertThrows(
                AlreadyExistsException.class,
                () -> lectureService.applyLecture(studentId, lectureId));
            // then
            assertEquals(LECTURE_APPLY_ALREADY_EXISTS, e.getMessage());
        }

        @Test
        void applyLecture_성공() {
            // given
            Long studentId = 1L;
            Long lectureId = 1L;
            Long lectureCountId = 1L;
            Integer lectureCount = 0;

            Student s = Student.builder().id(1L).name("테스트").build();
            Lecture l = Lecture.builder().id(1L).lectureName("특강명").lectureDate("2024-01-01")
                .build();
            LectureCount lc = LectureCount.builder().id(lectureCountId).lectureId(lectureId)
                .count(lectureCount).build();
            LectureHistory lh = LectureHistory.builder().id(1L).lecture(l).student(s).build();
            // 유저 정보 조회
            when(studentRepository.findById(studentId)).thenReturn(Optional.of(s));
            // 특강 정보 조회
            when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(l));
            // 특강 정원 조회
            when(lectureRepository.findLectureCountById(lectureId)).thenReturn(lc);
            // 특강 신청 정보 조회
            when(lectureHistoryRepository.findByLectureId(lectureId)).thenReturn(List.of());

            when(lectureHistoryRepository.save(l, s)).thenReturn(lh);
            // when
            LectureHistory result = lectureService.applyLecture(studentId, lectureId);

            // then
            assertEquals(lh, result);
        }
    }


    @Nested
    public class getLecturesApply {

        @Test
        void getLecturesApply_실패_USER_NOT_FOUND() {
            // given
            Long studentId = 1L;
            String date = "2024-01-01";

            // when
            Exception e = assertThrows(ResourceNotFoundException.class,
                () -> lectureService.getLecturesApply(studentId, date));

            // then
            assertEquals(USER_NOT_FOUND, e.getMessage());
        }

        @Test
        void getLecturesApply_성공() {
            // given
            Long studentId = 1L;
            String date = "2024-01-01";
            List<Lecture> lectures = List.of(
                Lecture.builder().id(1L).lectureName("특강1").lectureDate(date).build(),
                Lecture.builder().id(2L).lectureName("특강2").lectureDate(date).build(),
                Lecture.builder().id(3L).lectureName("특강3").lectureDate(date).build()
            );
            // 학생 정보 조회 Mock
            when(studentRepository.findById(studentId)).thenReturn(
                Optional.of(Student.builder().id(studentId).build()));

            // 특강 목록 조회 Mock
            when(lectureRepository.findBylectureDate(date)).thenReturn(lectures);

            List<Long> lectureIds = List.of(1L, 2L, 3L);

            List<LectureCount> lc = List.of(
                LectureCount.builder().lectureId(1L).count(30).build(),
                LectureCount.builder().lectureId(2L).count(10).build(),
                LectureCount.builder().lectureId(3L).count(0).build()
            );
            // 특강목록에 있는 신청자 리스트 조회
            when(lectureRepository.findLectureCountByLectureIds(lectureIds)).thenReturn(lc);

            // 학생의 특강 신청 이력 조회
            List<LectureHistory> lectureHistories = List.of(
                LectureHistory.builder().id(1L).lecture(lectures.get(2))
                    .student(Student.builder().id(studentId).build()).build()
            );
            when(lectureHistoryRepository.findByStudentId(studentId)).thenReturn(lectureHistories);

            // when
            List<Lecture> result = lectureService.getLecturesApply(studentId, date);

            // then
            assertEquals(1, result.size());
            assertEquals(2L, result.get(0).getId());
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
            Long studentId = 1L;
            List<LectureHistory> lectureHistories = List.of(
                LectureHistory.builder().id(1L)
                    .lecture(Lecture.builder().id(1L).lectureName("특강1").build())
                    .student(Student.builder().id(studentId).build()).build(),
                LectureHistory.builder().id(2L)
                    .lecture(Lecture.builder().id(2L).lectureName("특강2").build())
                    .student(Student.builder().id(studentId).build()).build(),
                LectureHistory.builder().id(3L)
                    .lecture(Lecture.builder().id(3L).lectureName("특강3").build())
                    .student(Student.builder().id(studentId).build()).build()
            );
            // 학생 정보 조회 Mock
            when(studentRepository.findById(studentId)).thenReturn(
                Optional.of(Student.builder().id(studentId).build()));

            // 학생의 특강 신청 이력 조회
            when(lectureHistoryRepository.findByStudentId(studentId)).thenReturn(lectureHistories);

            // when
            List<LectureHistory> result = lectureService.getLectureHistory(studentId);

            // then
            assertEquals(lectureHistories, result);
        }
    }
}
