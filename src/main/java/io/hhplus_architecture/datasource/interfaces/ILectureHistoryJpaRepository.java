package io.hhplus_architecture.datasource.interfaces;

import io.hhplus_architecture.domain.entity.LectureHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILectureHistoryJpaRepository extends JpaRepository<LectureHistory, Long> {
    
    List<LectureHistory> findByLectureId(Long lecture_id);

    List<LectureHistory> findByStudentId(Long student_id);
}

