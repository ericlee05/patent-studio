package com.ericlee.pstudio.alpha.domain.patent.entity;

import com.ericlee.pstudio.alpha.domain.patent.type.MultiComponentType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor @Builder
@NoArgsConstructor
@Getter
@Embeddable
public class MultiComponentId implements Serializable {
    @ManyToOne
    @JoinColumn
    private Patent patent;

    @Setter
    @Column(columnDefinition = "VARCHAR(8)", nullable = false)
    private String componentIdentifier;

    @Enumerated(EnumType.STRING)
    private MultiComponentType multiComponentType;
}
