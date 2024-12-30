package io.hhplus_architecture.business;

import static io.hhplus_architecture.domain.exception.message.ExceptionMessage.LECTURE_APPLY_ALREADY_EXISTS;
import static io.hhplus_architecture.domain.exception.message.ExceptionMessage.LECTURE_APPLY_CONFLICT;
import static io.hhplus_architecture.domain.exception.message.ExceptionMessage.LECTURE_NOT_FOUND;
import static io.hhplus_architecture.domain.exception.message.ExceptionMessage.USER_NOT_FOUND;

import io.hhplus_architecture.business.interfaces.ILectureHistoryRepository;
import io.hhplus_architecture.business.interfaces.ILectureRepository;
import io.hhplus_architecture.business.interfaces.IStudentRepository;
import io.hhplus_architecture.domain.entity.Lecture;
import io.hhplus_architecture.domain.entity.LectureCount;
import io.hhplus_architecture.domain.entity.LectureHistory;
import io.hhplus_architecture.domain.entity.Student;
import io.hhplus_architecture.domain.exception.constom.AlreadyExistsException;
import io.hhplus_architecture.domain.exception.constom.ConflictException;
import io.hhplus_architecture.domain.exception.constom.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LectureService {

    private final ILectureRepository lectureRepository;
    private final IStudentRepository studentRepository;
    private final ILectureHistoryRepository lectureHistoryRepository;

    @Autowired
    public LectureService(
        ILectureRepository lectureRepository,
        IStudentRepository studentRepository,
        ILectureHistoryRepository lectureHistoryRepository
    ) {
        this.lectureRepository = lectureRepository;
        this.studentRepository = studentRepository;
        this.lectureHistoryRepository = lectureHistoryRepository;
    }


    // private methods
    private Student findStudentById(Long studentId) {
        return studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
    }

    private Lecture findLectureById(Long lectureId) {
        return lectureRepository.findById(lectureId)
            .orElseThrow(() -> new ResourceNotFoundException(LECTURE_NOT_FOUND));
    }

    private void validationLectureCount(Long lectureId) {
        LectureCount lectureCount = lectureRepository.findLectureCountById(lectureId);
        if (lectureCount.getCount() >= 30) {
            throw new ConflictException(LECTURE_APPLY_CONFLICT);
        }
    }

    private void validatelectureHistory(Long studentId, Long lectureId) {
        List<LectureHistory> lectureHistory = lectureHistoryRepository.findByLectureId(lectureId);

        if (lectureHistory.stream().anyMatch(lh -> lh.getStudent().getId().equals(studentId))) {
            throw new AlreadyExistsException(LECTURE_APPLY_ALREADY_EXISTS);
        }
    }

    // public methods
    @Transactional
    public Lecture saveLecture(String lectureName, String lecturer, String lectureDate) {
        return lectureRepository.save(lectureName, lecturer, lectureDate);
    }

    @Transactional
    public LectureHistory applyLecture(Long studentId, Long lectureId) {
        // 학생 조회 및 체크
        Student student = findStudentById(studentId);
        // 특강 조회 및 체크
        Lecture lecture = findLectureById(lectureId);
        // 특강 정원 체크
        validationLectureCount(lectureId);
        // 특강 신청 이력
        validatelectureHistory(studentId, lectureId);

        LectureHistory lh = lectureHistoryRepository.save(lecture, student);
        lectureRepository.lectureCountAdd(lectureId);
        return lh;
    }

    @Transactional
    public List<Lecture> getLecturesApply(Long studentId, String date) {
        findStudentById(studentId);
        // 날짜에 존재하는 특강 목록 조회
        List<Lecture> lectures = lectureRepository.findBylectureDate(date);

        // 특강 목록 ID를 이용해서 특강 신청자 조회
        List<LectureCount> lectureCounts = lectureRepository.findLectureCountByLectureIds(
            lectures.stream().map(Lecture::getId).toList()
        );

        List<Lecture> result = new ArrayList<>(lectures);

        // 특강 목록에서 정원이 다 찬 특강 제외
        result.removeIf(lecture -> lectureCounts.stream()
            .anyMatch(lc -> lc.getLectureId().equals(lecture.getId()) && lc.getCount() >= 30));

        // 특강 신청 이력 조회
        List<LectureHistory> lectureHistories = lectureHistoryRepository.findByStudentId(studentId);

        // 특강 신청 이력이 있는 특강 목록 제외
        result.removeIf(lecture -> lectureHistories.stream()
            .anyMatch(lh -> lh.getLecture().getId().equals(lecture.getId())));

        return result;
    }

    @Transactional
    public List<LectureHistory> getLectureHistory(Long studentId) {
        findStudentById(studentId);
        return lectureHistoryRepository.findByStudentId(studentId);
    }

    @Transactional
    public LectureCount getLectureCount(Long lectureId) {
        return lectureRepository.findLectureCountById(lectureId);
    }

    @Transactional
    public List<LectureHistory> getLectureHistoryByLectureId(Long lectureId) {
        return lectureHistoryRepository.findByLectureId(lectureId);
    }
}
