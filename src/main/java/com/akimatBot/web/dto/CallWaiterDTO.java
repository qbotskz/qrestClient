package com.akimatBot.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class CallWaiterDTO {


    DeskDTO desk;
    private long id;
    private Date createdDate;
}
