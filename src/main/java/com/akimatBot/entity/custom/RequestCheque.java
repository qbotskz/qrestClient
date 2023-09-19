package com.akimatBot.entity.custom;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Setter
@Getter
public class RequestCheque {


    @ManyToOne
    FoodOrder foodOrder;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Date createdDate;

    private boolean notified;
}
