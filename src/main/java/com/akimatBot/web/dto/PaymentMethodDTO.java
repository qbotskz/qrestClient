package com.akimatBot.web.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaymentMethodDTO {

    List<PaymentTypeDTO> paymentTypes;
    private long id;
//    private boolean active;
    private String name;

}
