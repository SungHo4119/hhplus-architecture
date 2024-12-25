package io.hhplus_architecture.domain.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Entity
@Builder
@Table(name = "lecture")
@NoArgsConstructor
@AllArgsConstructor
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lecture_name", length = 10, nullable = false)
    private String lectureName;

    @Column(length = 10, nullable = false)
    private String lecturer;

    @Column(name = "lecture_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String lectureDate;
}
