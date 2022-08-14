package com.ericlee.pstudio.alpha.domain.patent.entity;

import com.ericlee.pstudio.alpha.domain.user.entity.User;
import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Getter @Builder
@Embeddable
public class PatentDetail {
    @Setter
    private LocalDateTime lastModified;

    @Setter
    @ManyToOne
    @JoinColumn
    private User lastModifier;

    private LocalDateTime createdAt;
}
