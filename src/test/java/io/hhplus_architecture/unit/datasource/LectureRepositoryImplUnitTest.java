package io.hhplus_architecture.unit.datasource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import io.hhplus_architecture.datasource.LectureRepositoryImpl;
import io.hhplus_architecture.datasource.interfaces.ILectureJpaRepository;
import io.hhplus_architecture.domain.entity.Lecture;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LectureRepositoryImplUnitTest {
    @Mock
    private ILectureJpaRepository lectureJpaRepository;

    @InjectMocks
    private LectureRepositoryImpl lectureRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById_성공_값이_없는_경우(){
        // given
        Long lectureId = 1L;
        when(lectureJpaRepository.findById(lectureId)).thenReturn(Optional.empty());
        // when
        Optional<Lecture> result = lectureRepository.findById(lectureId);
        //then
        assertEquals(Optional.empty(), result);
    }

    @Test
    void findById_성공_값이_있는_경우(){
        // given
        Long lectureId = 1L;
        Lecture lecture = new Lecture();
        when(lectureJpaRepository.findById(lectureId)).thenReturn(Optional.of(lecture));
        // when
        Optional<Lecture> result = lectureRepository.findById(lectureId);
        //then
        assertEquals(Optional.of(lecture), result);
    }


}
