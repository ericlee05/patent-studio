package com.ericlee.pstudio.alpha.domain.patent.entity;

import com.ericlee.pstudio.alpha.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Getter @Builder
@Embeddable
public class PatentDetail {
    private LocalDateTime lastModified;

    @ManyToOne
    @JoinColumn
    private User lastModifier;

    private LocalDateTime createdAt;
}
