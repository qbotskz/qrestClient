package com.akimatBot.web.dto;

import com.akimatBot.entity.enums.OrderItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class OrderItemDTO implements Serializable {

    OrderItemStatus orderItemStatus;
    private long id;
    private int quantity;

//    private Date createdDate;
    private int price;
    private FoodDTO food;

    private String comment;


}
