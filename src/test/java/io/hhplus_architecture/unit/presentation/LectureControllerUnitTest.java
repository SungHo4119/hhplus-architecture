package io.hhplus_architecture.unit.presentation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import io.hhplus_architecture.business.LectureService;
import io.hhplus_architecture.domain.entity.Lecture;
import io.hhplus_architecture.domain.entity.LectureHistory;
import io.hhplus_architecture.domain.entity.Student;
import io.hhplus_architecture.presentation.LectureController;
import io.hhplus_architecture.presentation.req.ApplyLectureRequestDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class LectureControllerUnitTest {


    @Mock
    LectureService lectureService;


    @InjectMocks
    LectureController lectureController;

    @BeforeEach
    void setUp() {
        // Mock 객체를 초기화
        // @Mock 어노테이션이 붙은 객체들을 초기화하고 @InjectMocks 어노테이션이 붙은 객체에 주입
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void applyLecture_성공() {
        Long studentId = 1L;
        Long lectureId = 1L;

        // given
        ApplyLectureRequestDto req = ApplyLectureRequestDto.builder()
            .studentId(1L)
            .lectureId(1L)
            .build();

        LectureHistory lectureHistory = LectureHistory.builder()
            .id(1L)
            .lecture(Lecture.builder().id(1L).lectureName("특강명").lectureDate("2024-01-01").build())
            .student(Student.builder().id(1L).name("테스트").build())
            .build();

        when(lectureService.applyLecture(studentId, lectureId)).thenReturn(lectureHistory);
        // when
        ResponseEntity<LectureHistory> res = lectureController.applyLecture(req);
        // then
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(lectureHistory, res.getBody());
    }

    @Test
    void getLecturesApply_성공() {
        String date = "2024-01-01";
        Long studentId = 1L;

        List<Lecture> l = List.of(
            Lecture.builder().id(1L).lectureName("특강명").lecturer("강사명").lectureDate("2024-01-01")
                .build()
        );

        // given
        when(lectureService.getLecturesApply(studentId, date)).thenReturn(l);
        // when
        ResponseEntity<List<Lecture>> res = lectureController.getLecturesApply(date, studentId);
        // then
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(l, res.getBody());
    }

    @Test
    void getLectureHistory_성공() {
        Long studentId = 1L;

        List<LectureHistory> l = List.of(
            LectureHistory.builder().id(1L)
                .lecture(
                    Lecture.builder().id(1L).lectureName("특강명").lectureDate("2024-01-01").build())
                .student(Student.builder().id(1L).name("테스트").build())
                .build()
        );

        // given
        when(lectureService.getLectureHistory(studentId)).thenReturn(l);
        // when
        ResponseEntity<List<LectureHistory>> res = lectureController.getLectureHistory(studentId);
        // then
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(l, res.getBody());
    }
}
