package io.hhplus_architecture.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Getter
@Setter
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@Table(
    name = "lecture_status",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"lecture_id"}
        )
    }
)
@NoArgsConstructor
@AllArgsConstructor
public class LectureCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "lecture_id", nullable = false)
    private Long lectureId;

    @Column(name = "count", columnDefinition = "TINYINT DEFAULT 0", nullable = false)
    private Integer count;
}
