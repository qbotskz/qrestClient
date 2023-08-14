package com.akimatBot.services;

import com.akimatBot.entity.custom.*;
import com.akimatBot.entity.enums.OrderItemStatus;
import com.akimatBot.entity.enums.OrderStatus;
import com.akimatBot.entity.enums.OrderType;
import com.akimatBot.repository.repos.*;
import com.akimatBot.web.dto.AnswerAddToCartDTO;
import com.akimatBot.web.dto.OrderItemDeleteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ClientOrderService {

    @Autowired
    DeskRepo deskRepo;

    @Autowired
    UserService userService;

    @Autowired
    GuestRepo guestRepo;

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ChequeRepo chequeRepo;

    @Autowired
    PropertiesRepo propertiesRepo;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    FoodService foodService;


    @Transactional
    public void createNewOrder(long chatId, long deskId){

        Desk desk = deskRepo.findById(deskId);
        FoodOrder foodOrder = getActiveOrderInRestaurant(chatId);
        if (foodOrder == null) {
            if (desk.getCurrentOrder() == null) {
                foodOrder = new FoodOrder();
                foodOrder.setCreatedDate(new Date());
                foodOrder.setDesk(desk);
                foodOrder.setOrderType(OrderType.in_the_restaurant);
                foodOrder.setOrderStatus(OrderStatus.IN_CART_CLIENT);

                Cheque cheque = new Cheque();
                cheque.setService(Double.parseDouble(propertiesRepo.findFirstById(4).getValue1()));
                cheque = chequeRepo.save(cheque);

                foodOrder.setCheque(cheque);
                foodOrder = orderRepository.save(foodOrder);
                desk.setCurrentOrder(foodOrder);
            }
            foodOrder = desk.getCurrentOrder();

//            Guest guest = orderRepository.getGuestOfOrder(foodOrder.getId(), chatId);
            Guest guest = userService.getCurrentGuestOfUser(chatId);
            if (guest == null) {
                guest = new Guest();
                guest.setClient(userService.getUserByChatId(chatId));
                guest.setFoodOrder(foodOrder);
                guest.setCreatedDate(new Date());
                guest = guestRepo.save(guest);
                userService.setGuest(guest , chatId);
            }
        }

    }

    public FoodOrder getActiveOrderInRestaurant(long chatId) {
        return orderRepository.getLastClientOrder2(chatId, OrderType.in_the_restaurant);
    }


    public AnswerAddToCartDTO addToCart(long chatId, long foodId) {
        Guest guest = guestRepo.findByClientChatId(chatId);
        OrderItem orderItem = orderItemRepository.findByFoodIdAndGuestIdAndOrderItemStatus(foodId, guest.getId() , OrderItemStatus.IN_CART_CLIENT);

        if (orderItem != null){
            orderItem.addQuantity();
            orderItemRepository.save(orderItem);
        }
        else {
            Food food = foodService.findById(foodId);

            orderItem = new OrderItem();
            orderItem.setOrderItemStatus(OrderItemStatus.IN_CART_CLIENT);
            orderItem.setCreatedDate(new Date());
            orderItem.setQuantity(1);
            orderItem.setPrice(food.getPrice());
            orderItem.setGuest(guest);
            orderItem.setFood(food);
            orderItem = orderItemRepository.save(orderItem);
        }

//        chequeRepo.addTotal(orderItem.getId());
        Cheque cheque1e = orderItem.getGuest().getFoodOrder().getCheque();
        cheque1e.addTotal(orderItem.getPrice());
        chequeRepo.save(cheque1e);

        AnswerAddToCartDTO answerAddToCartDTO = new AnswerAddToCartDTO();
        answerAddToCartDTO.setOrderItem(orderItem.getOrderItemDTO(LanguageService.getLanguage(chatId)));
        Cheque cheque = chequeRepo.getByGuest(guest.getId());
        answerAddToCartDTO.setCheque(cheque.getChequeDTO());

        return answerAddToCartDTO;
    }

    public void place(long chatId) {
        orderItemRepository.orderingByClient(chatId);
    }

    public boolean cancelOrderItem(OrderItemDeleteDTO item) {
        OrderItem orderItem1 = orderItemRepository.getOne(item.getOrderItem().getId());
        if(!orderItem1.getOrderItemStatus().equals(OrderItemStatus.DELETED)) {
            if (orderItem1.getOrderItemStatus().equals(OrderItemStatus.IN_CART_CLIENT)) {
                this.deleteOrderItem(orderItem1);
                return true;
            }
        }
        return false;

    }

    @Transactional
    public void deleteOrderItem(OrderItem orderItem) {

        FoodOrder foodOrder = orderItem.getGuest().getFoodOrder();
        Cheque cheque = foodOrder.getCheque();
        cheque.addTotal(-(orderItem.getTotal()));
        chequeRepo.save(cheque);


        orderItemRepository.setDelete(orderItem);
    }

    public AnswerAddToCartDTO decreaseOrderItem(long orderItemId, long chatId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId);

        if (orderItem != null){

            if (orderItem.getQuantity() == 1){
                orderItemRepository.setDelete(orderItem);
            }
            else {
                orderItem.minusQuantity();
                orderItemRepository.save(orderItem);
            }


            Cheque cheque1e = orderItem.getGuest().getFoodOrder().getCheque();
            cheque1e.addTotal(-orderItem.getPrice());
            chequeRepo.save(cheque1e);

            AnswerAddToCartDTO answerAddToCartDTO = new AnswerAddToCartDTO();
            answerAddToCartDTO.setOrderItem(orderItem.getOrderItemDTO(LanguageService.getLanguage(chatId)));
            Cheque cheque = chequeRepo.getByGuest(orderItem.getGuest().getId());
            answerAddToCartDTO.setCheque(cheque.getChequeDTO());

            return answerAddToCartDTO;
        }

        return null;

    }

}
