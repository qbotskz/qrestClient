package com.akimatBot.entity.custom;


import com.akimatBot.entity.standart.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;//id

    private Integer grade;//var

    @ManyToMany
    private List<GradeTextTemplate> textTemplates;//id_gradeTextTemplates

    private String clientsReview;//text

    @ManyToOne
    private User user;//user

    private Date date;//date
}
