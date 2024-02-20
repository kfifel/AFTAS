package com.aftasapi.web.dto;

import com.aftasapi.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RankingDTO implements Serializable {

    private Integer rank;
    private Integer score;
    private Member member;
}
