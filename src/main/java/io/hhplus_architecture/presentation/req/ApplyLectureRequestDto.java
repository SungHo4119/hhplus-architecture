package io.hhplus_architecture.presentation.req;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplyLectureRequestDto {

    private Long studentId;
    private Long lectureId;
}
