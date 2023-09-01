package com.akimatBot.web.dto;


import lombok.Data;

import java.util.Date;

@Data
public class HistoryOfOrdersDTO {

    private long id;

    private String restorantName;

    private Date date;

    private String price;


}
