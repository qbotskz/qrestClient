package com.akimatBot.entity.custom;

import com.akimatBot.entity.enums.CacheTypes;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Setter
@Getter
public class RequestCheque {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    FoodOrder foodOrder;

    private Date createdDate;

    private boolean notified;
}
