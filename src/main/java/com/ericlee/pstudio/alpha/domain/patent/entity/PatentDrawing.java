package com.ericlee.pstudio.alpha.domain.patent.entity;

import com.ericlee.pstudio.alpha.domain.user.entity.User;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class PatentDrawing {
    @EmbeddedId
    private DrawingId id;

    @Lob
    private byte[] image;

    @Setter
    @Column(columnDefinition = "VARCHAR(16)", nullable = false)
    private String identifier;

    @Column(nullable = false)
    @Lob
    private String description;

    @Setter
    @Column(nullable = false)
    private boolean isRepresentative;

    public void update(User updateUser, String identifier, String description) {
        this.setIdentifier(identifier);
        this.description = description;

        PatentDetail detail = id.getPatent().getDetail();
        detail.setLastModifier(updateUser);
        detail.setLastModified(LocalDateTime.now());
    }
}
