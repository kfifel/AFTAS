package com.aftasapi.entity.embedded;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class RankingId implements Serializable {

    @Serial
    private static final long serialVersionUID = -2150687288148842337L;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "competition_id")
    private String competitionCode;
}