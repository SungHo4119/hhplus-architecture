package io.hhplus_architecture.datasource.interfaces;

import io.hhplus_architecture.domain.entity.Lecture;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILectureJpaRepository extends JpaRepository<Lecture, Long> {

    Optional<Lecture> findById(Long id);

    List<Lecture> findBylectureDate(String date);
}
