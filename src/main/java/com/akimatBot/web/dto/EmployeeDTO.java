package com.akimatBot.web.dto;


import com.akimatBot.entity.enums.Language;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmployeeDTO {

    Language language;
    List<RoleDTO> roles;
    private long id;
    private long chatId;
    private String phone;
    private String fullName;
    private String position;
    private Long code;
    private WaiterShiftDTO currentShift;
    private RestaurantBranchDTO restaurantBranch;

}
