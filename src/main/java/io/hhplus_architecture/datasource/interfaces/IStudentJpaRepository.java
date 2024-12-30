package io.hhplus_architecture.datasource.interfaces;

import io.hhplus_architecture.domain.entity.Student;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStudentJpaRepository extends JpaRepository<Student, Long> {
    // 학생 조회
    Optional<Student> findById(Long id);

}
