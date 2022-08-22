package com.ericlee.pstudio.alpha.domain.patent.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
@NoArgsConstructor @Getter
public class DrawingUpdateRequest {
    private String identifier;
    private String description;
}
