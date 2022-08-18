package com.ericlee.pstudio.alpha.domain.patent.entity;

import com.ericlee.pstudio.alpha.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(name = "component_order", nullable = false)
    private Integer order;

    public void update(User updateUser, String identifier, String content) {
        id.getPatent().getDetail().setLastModifier(updateUser);
        id.getPatent().getDetail().setLastModified(LocalDateTime.now());

        this.id.setComponentIdentifier(identifier);
        this.content = content;
    }
}
