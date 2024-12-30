package io.hhplus_architecture.unit.datasource;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import io.hhplus_architecture.datasource.LectureHistoryRepositoryImpl;
import io.hhplus_architecture.datasource.interfaces.ILectureHistoryJpaRepository;
import io.hhplus_architecture.domain.entity.Lecture;
import io.hhplus_architecture.domain.entity.LectureHistory;
import io.hhplus_architecture.domain.entity.Student;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LectureHistoryRepositoryImplUnitTest {

    @Mock
    ILectureHistoryJpaRepository lectureHistoryJpaRepository;

    @InjectMocks
    LectureHistoryRepositoryImpl lectureHistoryRepositoryImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByLectureId_성공_값이_없는_경우(){
        // given
        Long lectureId = 1L;
        when(lectureHistoryJpaRepository.findByLectureId(lectureId)).thenReturn(List.of());
        // when
        List<LectureHistory> result = lectureHistoryRepositoryImpl.findByLectureId(lectureId);
        //then
        assertEquals(0, result.size());
    }

    @Test
    void findByLectureId_성공_값이_있는_경우(){
        // given
        Long lectureId = 1L;
        List<LectureHistory> lh = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            lh.add(new LectureHistory());
        }
        when(lectureHistoryJpaRepository.findByLectureId(lectureId)).thenReturn(lh);
        // when
        List<LectureHistory> result = lectureHistoryRepositoryImpl.findByLectureId(lectureId);
        //then
        assertEquals(lh, result);
    }

    // 질문 사항으로 남기고 주석
    // Setp 준비.md 궁금증1 참고
    /**
    @Test
    void save_성공(){
        // given
        Lecture lecture = new Lecture();
        Student student = new Student();
        LectureHistory lectureHistory = LectureHistory.createLectureHistory(lecture, student);
        when(lectureHistoryJpaRepository.save(lectureHistory)).thenReturn(lectureHistory);
        // when
        LectureHistory result = lectureHistoryRepositoryImpl.save(lecture, student);
        //then
        assertEquals(lectureHistory, result);
    }
    */
}
