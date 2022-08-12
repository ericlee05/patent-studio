package com.ericlee.pstudio.alpha.domain.patent.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class MultiComponent {
    @EmbeddedId
    private MultiComponentId id;

    @Column(nullable = false)
    @Lob
    private String content;
}
