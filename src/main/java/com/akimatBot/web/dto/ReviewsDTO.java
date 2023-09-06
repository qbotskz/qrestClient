package com.akimatBot.web.dto;

import com.akimatBot.entity.standart.User;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;
@Data
public class ReviewsDTO {
    private String reviewText;

    private Long chatId;
}
