package io.hhplus_architecture.unit.datasource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import io.hhplus_architecture.datasource.StudentRepositoryImpl;
import io.hhplus_architecture.datasource.interfaces.IStudentJpaRepository;
import io.hhplus_architecture.domain.entity.Student;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class StudentRepositoryImplUnitTest {

    @Mock
    IStudentJpaRepository studentJpaRepository;

    @InjectMocks
    StudentRepositoryImpl studentRepositoryImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById_성공_값이_없는_경우(){
        // given
        Long studentId = 1L;
        when(studentJpaRepository.findById(studentId)).thenReturn(Optional.empty());
        // when
        Optional<Student> result = studentRepositoryImpl.findById(studentId);
        //then
        assertEquals(Optional.empty(), result);
    }

    @Test
    void findById_성공_값이_있는_경우(){
        // given
        Long studentId = 1L;
        Student student = new Student();
        when(studentJpaRepository.findById(studentId)).thenReturn(Optional.of(student));
        // when
        Optional<Student> result = studentRepositoryImpl.findById(studentId);
        //then
        assertEquals(Optional.of(student), result);
    }
}
