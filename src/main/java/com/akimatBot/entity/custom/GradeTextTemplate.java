package com.akimatBot.entity.custom;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class GradeTextTemplate {//name

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;//id

    private String text;//text
}
