package com.ericlee.pstudio.alpha.domain.patent.entity;

import com.ericlee.pstudio.alpha.domain.patent.type.SingleComponentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Embeddable
public class SingleComponentId implements Serializable {
    @ManyToOne
    @JoinColumn
    private Patent patent;

    @Enumerated(EnumType.STRING)
    private SingleComponentType singleComponentType;
}
