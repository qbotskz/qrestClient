package com.akimatBot.web.websocets.entities;

import com.akimatBot.entity.custom.OrderItem;
import com.akimatBot.entity.enums.Language;
import com.akimatBot.web.dto.OrderItemDeleteDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrderItemDeleteEntity {

    long orderId;
    String waiterName;
    String hallName;
    long deskNumber;
    Date date;
    boolean printed;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    private OrderItem orderItem;
    private String reason;

    public OrderItemDeleteEntity(long id) {
        this.id = id;
    }

    public OrderItemDeleteDTO getDTO() {
        OrderItemDeleteDTO orderItemDeleteDTO = new OrderItemDeleteDTO();

        orderItemDeleteDTO.setId(this.id);
        orderItemDeleteDTO.setReason(this.getReason());
        orderItemDeleteDTO.setOrderItem(orderItem.getOrderItemDTO(Language.ru));
        orderItemDeleteDTO.setOrderId(orderItem.getGuest().getFoodOrder().getId());
        orderItemDeleteDTO.setWaiterName(orderItem.getGuest().getFoodOrder().getWaiter().getFullName());
        orderItemDeleteDTO.setHallName(orderItem.getGuest().getFoodOrder().getDesk().getHall().getName());
        orderItemDeleteDTO.setDeskNumber(orderItem.getGuest().getFoodOrder().getDesk().getNumber());
        orderItemDeleteDTO.setDate(this.date);

        return orderItemDeleteDTO;
    }
}
