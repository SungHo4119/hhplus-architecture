package io.hhplus_architecture.business.interfaces;

import io.hhplus_architecture.domain.entity.Student;
import java.util.Optional;
import org.springframework.stereotype.Repository;


@Repository
public interface IStudentRepository {
    // 학생 저장
    Student save(String name);

    // 학생 조회
    Optional<Student> findById(Long id);
}
