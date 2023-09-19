package com.akimatBot.web.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentDTO {

    PaymentTypeDTO paymentType;
    private long id;
    private double amount;


}
