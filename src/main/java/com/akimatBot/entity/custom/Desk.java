package com.akimatBot.entity.custom;

import com.akimatBot.entity.enums.Language;
import com.akimatBot.entity.enums.OrderStatus;
import com.akimatBot.repository.TelegramBotRepositoryProvider;
import com.akimatBot.services.CartItemService;
import com.akimatBot.web.dto.DeskDTO;
import com.akimatBot.web.dto.FoodOrderDTO;
import com.akimatBot.web.dto.HallDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Data
@AllArgsConstructor
@Entity
@Cacheable(value = false)
public class Desk {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long     id;

    private long number;

    @ManyToOne
    private FoodOrder currentOrder;


    @ManyToOne
    private Hall hall;

    public Desk() {
    }

    private FoodOrderDTO getOrderDTO(Language language) {
        if (this.currentOrder != null){
            return currentOrder.getFoodOrderDTO(language);
        }
        return null;
    }

    public DeskDTO getDeskDTONotFull() {
        DeskDTO deskDTO = new DeskDTO();

        deskDTO.setId(this.getId());
        deskDTO.setNumber(this.getNumber());
        deskDTO.setFoodOrderStatus(getFoodOrderStatus(this));
        deskDTO.setHall(this.getHall().getDTONotFull());
        return deskDTO;
    }

    private OrderStatus getFoodOrderStatus(Desk desk) {
        if (desk != null && desk.getCurrentOrder() != null){
            return desk.getCurrentOrder().getOrderStatus();
        }
        return null;
    }

    public DeskDTO getDeskDTOFull(Language language) {
        DeskDTO deskDTO = new DeskDTO();
        deskDTO.setId(this.getId());
        deskDTO.setNumber(this.getNumber());
        deskDTO.setOrder(getOrderDTO(language));
        deskDTO.setHall(this.getHall().getDTOSimple());


        return deskDTO;
    }


}
