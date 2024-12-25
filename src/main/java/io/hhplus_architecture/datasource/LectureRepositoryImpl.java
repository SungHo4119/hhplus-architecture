package io.hhplus_architecture.datasource;

import io.hhplus_architecture.business.interfaces.ILectureRepository;
import io.hhplus_architecture.datasource.interfaces.ILectureCountJpaRepository;
import io.hhplus_architecture.datasource.interfaces.ILectureJpaRepository;
import io.hhplus_architecture.domain.entity.Lecture;
import io.hhplus_architecture.domain.entity.LectureCount;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LectureRepositoryImpl implements ILectureRepository {

    private final ILectureJpaRepository lectureJpaRepository;

    private final ILectureCountJpaRepository lectureCountJpaRepository;

    @Autowired
    public LectureRepositoryImpl(ILectureJpaRepository lectureRepository,
        ILectureCountJpaRepository lectureCountJpaRepository) {
        this.lectureJpaRepository = lectureRepository;
        this.lectureCountJpaRepository = lectureCountJpaRepository;
    }

    // 특강 조회
    @Override
    public Optional<Lecture> findById(Long id) {
        return lectureJpaRepository.findById(id);
    }

    // 날짜에 해당하는 특강 List 조회
    @Override
    public List<Lecture> findBylectureDate(String date) {
        return lectureJpaRepository.findBylectureDate(date);
    }

    @Override
    public Lecture save(String lectureName, String lecturer, String lectureDate) {
        Lecture lecture = lectureJpaRepository.save(
            Lecture.builder().lectureName(lectureName).lecturer(lecturer).lectureDate(lectureDate)
                .build());

        lectureCountJpaRepository.save(
            LectureCount.builder().lectureId(lecture.getId()).count(0).build());
        return lecture;
    }

    @Override
    public LectureCount findLectureCountById(Long lectureId) {
        return lectureCountJpaRepository.findBylectureId(lectureId);
    }

    @Override
    public List<LectureCount> findLectureCountByLectureIds(List<Long> lectureIds) {
        return lectureCountJpaRepository.findByLectureIds(lectureIds);
    }

    @Override
    public void lectureCountAdd(Long lectureId) {
        LectureCount l = lectureCountJpaRepository.findBylectureId(lectureId);
        l.setCount(l.getCount() + 1);
        lectureCountJpaRepository.save(l);
    }
}
