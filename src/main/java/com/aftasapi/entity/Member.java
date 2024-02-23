package com.aftasapi.entity;

import com.aftasapi.entity.enums.IdentityDocumentType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Builder(builderMethodName = "memberBuilder")
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id")
public class Member extends AppUser implements Serializable {

    @Column(nullable = false, unique = true)
    private Long number;
    private Date accessionDate;
    private String nationality;
    @Enumerated(EnumType.STRING)
    private IdentityDocumentType identityDocumentType;
    private String identityNumber;

    @OneToMany(mappedBy = "member")
    @ToString.Exclude
    private List<Ranking> rankings;

    @OneToMany(mappedBy = "member")
    @ToString.Exclude
    private List<Hunting> huntings;

    public String getName() {
        return this.getFirstName();
    }

    public void setName(String name) {
        this.setFirstName(name);
    }

    public String getFamilyName() {
        return this.getLastName();
    }

    public void setFamilyName(String name) {
        this.setLastName(name);
    }
}

