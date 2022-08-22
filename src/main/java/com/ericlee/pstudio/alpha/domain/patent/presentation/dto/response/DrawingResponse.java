package com.ericlee.pstudio.alpha.domain.patent.presentation.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor @Builder
@Getter @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DrawingResponse {
    private String identifier;
    private String path;
    private boolean isRepresentative;
}
