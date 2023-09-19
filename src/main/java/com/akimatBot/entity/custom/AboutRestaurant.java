package com.akimatBot.entity.custom;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Time;

@Data
@Entity
public class AboutRestaurant {

    @Id
    private int id;

    private String addresskz;
    private String addressru;

    private Time monSt;
    private Time monEnd;

    private Time tueSt;
    private Time tueEnd;

    private Time wedSt;
    private Time wedEnd;

    private Time thurSt;
    private Time thurEnd;

    private Time friSt;
    private Time friEnd;

    private Time satSt;
    private Time satEnd;

    private Time sunSt;
    private Time sunEnd;

    private String contact;

    private String urlInst;
    private String urlWhats;

}
