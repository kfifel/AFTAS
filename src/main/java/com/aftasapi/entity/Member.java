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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq")
    @SequenceGenerator(name = "member_seq", initialValue = 1000, allocationSize = 1)
    private Long number;

    private String name;
    private String familyName;
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
}

