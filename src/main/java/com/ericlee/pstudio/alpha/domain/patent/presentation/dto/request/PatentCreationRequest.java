package com.ericlee.pstudio.alpha.domain.patent.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public class PatentCreationRequest {
    private String koreanName;
    private String englishName;
}
