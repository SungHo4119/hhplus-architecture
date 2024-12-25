package io.hhplus_architecture.datasource.interfaces;

import io.hhplus_architecture.domain.entity.LectureHistory;
import jakarta.persistence.LockModeType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface ILectureHistoryJpaRepository extends JpaRepository<LectureHistory, Long> {

    /**
     * LockModeType
     * <p>
     * - READ : 읽기 작업을 수행할 때 사용
     * - WRITE : 쓰기 작업을 수행할 때 사용
     * - OPTIMISTIC : 낙관적 락을 사용할 때 사용
     * - OPTIMISTIC_FORCE_INCREMENT : 낙관적 락을 사용할 때 사용하며, 엔티티의 버전을 강제로 증가시킬 때 사용
     * - PESSIMISTIC_READ : 비관적 락을 사용할 때 사용하며, 읽기 작업을 수행할 때 사용
     * - PESSIMISTIC_WRITE : 비관적 락을 사용할 때 사용하며, 쓰기 작업을 수행할 때 사용
     * - PESSIMISTIC_FORCE_INCREMENT : 비관적 락을 사용할 때 사용하며, 엔티티의 버전을 강제로 증가시킬 때 사용
     * - NONE : 락을 사용하지 않을 때 사용 ( default )
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<LectureHistory> findByLectureId(Long lecture_id);

    List<LectureHistory> findByStudentId(Long student_id);
}

