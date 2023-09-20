package com.akimatBot.web.dto;

import lombok.Data;

import java.util.List;

@Data
public class HallDTO {

    List<DeskDTO> desks;
    private long id;
    private String name;

}
