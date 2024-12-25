package io.hhplus_architecture.datasource.interfaces;

import io.hhplus_architecture.domain.entity.LectureCount;
import jakarta.persistence.LockModeType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ILectureCountJpaRepository extends JpaRepository<LectureCount, Long> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    LectureCount findBylectureId(Long lectureId);

    @Query("SELECT lc FROM LectureCount lc WHERE lc.lectureId IN :lectureIds")
    List<LectureCount> findByLectureIds(@Param("lectureIds") List<Long> lectureIds);


    LectureCount save(LectureCount lectureCount);

}
