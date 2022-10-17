package com.example.batch.app.cash.entity;

import com.example.batch.app.base.entity.BaseEntity;
import com.example.batch.app.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class CashLog extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    private Member member;

    private long price;

    private String eventType;

}
