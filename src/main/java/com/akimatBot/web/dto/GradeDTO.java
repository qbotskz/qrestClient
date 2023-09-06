package com.akimatBot.web.dto;


import com.akimatBot.entity.custom.GradeTextTemplate;
import com.akimatBot.entity.standart.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
