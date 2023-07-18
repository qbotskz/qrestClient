package com.akimatBot.web.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReportDailyDTO {
    GeneralShiftDTO generalShift;

    long orderQuantity;

    Double orderTotal;

    EmployeeDTO currentEmployee;
}
