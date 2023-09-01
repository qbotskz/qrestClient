package com.akimatBot.entity.custom;

import com.akimatBot.entity.enums.CacheTypes;
import com.akimatBot.entity.standart.Employee;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Setter
@Getter
public class NotificationCallWaiter {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    CallWaiter callWaiter;

    @ManyToOne
    Employee employ;

    Date createdDate;
}
