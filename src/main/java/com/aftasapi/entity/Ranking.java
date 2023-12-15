package com.aftasapi.entity;

import com.aftasapi.entity.embedded.RankingId;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ranking implements java.io.Serializable{

    @EmbeddedId
    private RankingId id;

    private Integer rank;
    private Integer score;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @MapsId("memberId")
    @JsonBackReference
    private Member member;

    @ManyToOne
    @JoinColumn(name = "competition_id")
    @MapsId("competitionCode")
    @JsonBackReference
    private Competition competition;
}
