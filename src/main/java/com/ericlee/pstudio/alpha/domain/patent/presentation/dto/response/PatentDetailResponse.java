package com.ericlee.pstudio.alpha.domain.patent.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor @Builder @Getter
public class PatentDetailResponse {
    private String patentSummary;
}
