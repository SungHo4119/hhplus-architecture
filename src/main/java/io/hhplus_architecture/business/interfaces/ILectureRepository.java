package io.hhplus_architecture.business.interfaces;

import io.hhplus_architecture.domain.entity.Lecture;
import io.hhplus_architecture.domain.entity.LectureCount;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface ILectureRepository {

    // 특강 조회
    Optional<Lecture> findById(Long id);

    // 날짜에 해당하는 특강 List 조회
    List<Lecture> findBylectureDate(String date);

    // 특강 저장
    Lecture save(String lectureName, String lecturer, String lectureDate);

    // 특강 신청자 수 조회
    LectureCount findLectureCountById(Long lectureId);

    // 특강여러개 신청자 수 조회
    List<LectureCount> findLectureCountByLectureIds(List<Long> lectureIds);

    // 특강 신청자 수 증가
    void lectureCountAdd(Long lectureId);

}
