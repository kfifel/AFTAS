package com.aftasapi.dto;

import com.aftasapi.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RankingDTO {

    private Integer rank;
    private Integer score;
    private Member member;
}
