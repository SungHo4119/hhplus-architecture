package io.hhplus_architecture.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@Table(
    name = "lecture_history",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"lecture_id", "student_id"}
        )
    }
)
@NoArgsConstructor
@AllArgsConstructor
public class LectureHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Lecture.class)
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    @ManyToOne(targetEntity = Student.class)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @CreatedDate
    @Column(name = "apply_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyDate;

    public static LectureHistory createLectureHistory(Lecture lecture, Student student) {
        return LectureHistory.builder()
            .lecture(lecture)
            .student(student)
            .build();
    }
}


