package com.ericlee.pstudio.alpha.domain.patent.entity;

import com.ericlee.pstudio.alpha.domain.patent.type.MultiComponentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Embeddable
public class MultiComponentId implements Serializable {
    @ManyToOne
    @JoinColumn
    private Patent patent;

    @Column(columnDefinition = "VARCHAR(6)", nullable = false)
    private String componentIdentifier;

    @Enumerated(EnumType.STRING)
    private MultiComponentType multiComponentType;
}
