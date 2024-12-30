package io.hhplus_architecture.datasource;

import io.hhplus_architecture.business.interfaces.ILectureHistoryRepository;
import io.hhplus_architecture.datasource.interfaces.ILectureHistoryJpaRepository;
import io.hhplus_architecture.domain.entity.Lecture;
import io.hhplus_architecture.domain.entity.LectureHistory;
import io.hhplus_architecture.domain.entity.Student;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LectureHistoryRepositoryImpl implements ILectureHistoryRepository {

    private final ILectureHistoryJpaRepository lectureHistoryJpaRepository;

    @Autowired
    public LectureHistoryRepositoryImpl(ILectureHistoryJpaRepository lectureHistoryRepository) {
        this.lectureHistoryJpaRepository = lectureHistoryRepository;
    }

    @Override
    public List<LectureHistory> findByLectureId(Long lecture_id) {
        return lectureHistoryJpaRepository.findByLectureId(lecture_id);
    }

    @Override
    public List<LectureHistory> findByStudentId(Long student_id) {
        return lectureHistoryJpaRepository.findByStudentId(student_id);
    }

    @Override
    public LectureHistory save(Lecture lecture, Student student) {
        // lectureHistory 생성
        LectureHistory lectureHistory = LectureHistory.createLectureHistory(lecture, student);
        return lectureHistoryJpaRepository.save(lectureHistory);
    }
}