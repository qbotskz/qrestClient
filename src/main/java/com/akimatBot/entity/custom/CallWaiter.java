package com.akimatBot.entity.custom;

import com.akimatBot.web.dto.CallWaiterDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Setter
@Getter
public class CallWaiter {


    @ManyToOne
    FoodOrder foodOrder;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Date createdDate;

    public CallWaiterDTO getDTO() {
        CallWaiterDTO callWaiterDTO = new CallWaiterDTO();
        callWaiterDTO.setId(this.getId());
        callWaiterDTO.setCreatedDate(this.getCreatedDate());
//        callWaiterDTO.setDesk(this.getDesk().getDeskDTOFull(language));

        return callWaiterDTO;
    }
}
