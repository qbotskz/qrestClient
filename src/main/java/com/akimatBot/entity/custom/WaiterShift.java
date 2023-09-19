package com.akimatBot.entity.custom;

import com.akimatBot.entity.standart.Employee;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class WaiterShift {
    @ManyToOne
    Employee waiter;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private Date openingTime;
    private Date closingTime;

}
