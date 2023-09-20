package com.akimatBot.web.dto;


import com.akimatBot.entity.enums.Language;
import lombok.Data;

import java.sql.Time;


@Data
public class AboutRestDTO {

    Language language;
    private Long chatId;
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
