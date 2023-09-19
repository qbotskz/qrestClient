package com.akimatBot.web.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeDTO {

    private Long chatId;

    private Integer grade;

    private List<Integer> textTemplates;

    private String clientsReview;
}
