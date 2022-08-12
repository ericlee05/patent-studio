package com.ericlee.pstudio.alpha.domain.organization.entity;

import com.ericlee.pstudio.alpha.domain.patent.entity.Patent;
import com.ericlee.pstudio.alpha.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Builder @Getter
@Entity
public class Organization {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(columnDefinition = "VARCHAR(64)", nullable = false)
    private String name;

    @Embedded
    private OrganizationDetail detail;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users;
    public void addUser(User user) {
        users.add(user);
    }
    public void removeUser(User user) {
        users.remove(user);
    }

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Patent> patents;
    public void addPatent(Patent patent) {
        patents.add(patent);
    }
    public void deletePatent(Patent patent) {
        patents.remove(patent);
    }
}
