package io.hhplus_architecture.business.interfaces;

import io.hhplus_architecture.domain.entity.Lecture;
import io.hhplus_architecture.domain.entity.LectureHistory;
import io.hhplus_architecture.domain.entity.Student;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ILectureHistoryRepository {

    List<LectureHistory> findByLectureId(Long lectureId);

    List<LectureHistory> findByStudentId(Long studentId);

    LectureHistory save(Lecture lecture, Student student);
}
