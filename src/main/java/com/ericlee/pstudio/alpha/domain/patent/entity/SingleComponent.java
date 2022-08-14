package com.ericlee.pstudio.alpha.domain.patent.entity;

import com.ericlee.pstudio.alpha.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
public class SingleComponent {
    @EmbeddedId
    private SingleComponentId id;

    @Column(nullable = false)
    @Lob
    private String content;
    public void update(User updateUser, String content) {
        id.getPatent().getDetail().setLastModifier(updateUser);
        id.getPatent().getDetail().setLastModified(LocalDateTime.now());
        this.content = content;
    }
}
