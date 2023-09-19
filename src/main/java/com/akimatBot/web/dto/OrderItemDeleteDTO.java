package com.akimatBot.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class OrderItemDeleteDTO implements Serializable {

    long id;
    long orderId;
    String waiterName;
//    private String printerName;
    String hallName;
    long deskNumber;
    Date date;
    private OrderItemDTO orderItem;
    private String reason;


}
