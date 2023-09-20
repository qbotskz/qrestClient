package com.akimatBot.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class PrintKitchenDTO {

    long id;
    List<OrderItemDTO> items;
    long orderId;
    Date createdDate;
    String waiterName;
    String hallName;
    long deskNumber;
}
