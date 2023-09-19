package com.akimatBot.services;


import com.akimatBot.entity.custom.OrderItem;
import com.akimatBot.repository.repos.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;


    public OrderItem getOne(long id) {
        return orderItemRepository.findById(id);
    }


//    public OrderItemDTO getOrderItemData(OrderItem o){
//        OrderItemDTO orderItemDTO = new OrderItemDTO();
//        orderItemDTO.setCreateDate(o.getCreatedDate());
//        orderItemDTO.set
//
//
//
//        return orderItemDTO;
//    }


}
