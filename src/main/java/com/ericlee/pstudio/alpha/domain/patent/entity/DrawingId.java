package com.ericlee.pstudio.alpha.domain.patent.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@AllArgsConstructor @NoArgsConstructor
@Getter
@Embeddable
public class DrawingId implements Serializable {
    @ManyToOne
    @JoinColumn
    private Patent patent;

    @Column(columnDefinition = "VARCHAR(16)", nullable = false)
    private String drawingId;

    @Override
    public int hashCode() {
        return String.format("%s.%s", patent.hashCode(), drawingId.hashCode()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof DrawingId)) return false;

        return String.format("%s.%s", patent.hashCode(), drawingId.hashCode()).equals(
                String.format("%s.%s", patent.hashCode(), drawingId.hashCode())
        );
    }
}
