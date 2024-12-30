package io.hhplus_architecture.presentation;


import io.hhplus_architecture.business.LectureService;
import io.hhplus_architecture.domain.entity.Lecture;
import io.hhplus_architecture.domain.entity.LectureHistory;
import io.hhplus_architecture.presentation.req.ApplyLectureRequestDto;
import io.hhplus_architecture.utils.Validation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/lectures")
public class LectureController {

    private final LectureService lectureService;

    @Autowired
    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @PostMapping
    // 강의 신청
    public ResponseEntity<LectureHistory> applyLecture(
        @RequestBody ApplyLectureRequestDto req
    ) {
        // input validation
        Validation.inputValidationId(req.getStudentId(), "studentId");
        Validation.inputValidationId(req.getLectureId(), "lectureId");
        return ResponseEntity.ok(
            lectureService.applyLecture(req.getStudentId(), req.getLectureId()));
    }

    @GetMapping("/apply")
    public ResponseEntity<List<Lecture>> getLecturesApply(
        @Param("date") String date,
        @Param("studentId") Long studentId
    ) {
        Validation.inputValidationId(studentId, "studentId");

        Validation.isValidDateFormat(date);
        return ResponseEntity.ok(lectureService.getLecturesApply(studentId, date));
    }

    @GetMapping("/history")
    public ResponseEntity<List<LectureHistory>> getLectureHistory(
        @Param("studentId") Long studentId
    ) {
        Validation.inputValidationId(studentId, "studentId");

        return ResponseEntity.ok(lectureService.getLectureHistory(studentId));
    }
}
