package com.akimatBot.web.websocets;

import com.akimatBot.entity.custom.FoodOrder;
import com.akimatBot.entity.custom.OrderItem;
import com.akimatBot.entity.custom.PrintPayment;
import com.akimatBot.entity.custom.PrintPrecheck;
import com.akimatBot.repository.repos.*;
import com.akimatBot.utils.DateUtil;
import com.akimatBot.web.dto.OrderItemDeleteDTO;
import com.akimatBot.web.websocets.entities.KitchenPrintEntityRepo;
import com.akimatBot.web.websocets.entities.OrderItemDeleteEntity;
import com.akimatBot.web.websocets.entities.OrderItemDeleteEntityRepo;
import com.akimatBot.web.websocets.entities.PrintKitchenEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class WebSocketService {

    @Value("${printerApiToken}")
    static String apiToken;

    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    KitchenPrintEntityRepo kitchenPrintEntityRepo;

    @Autowired
    OrderItemDeleteEntityRepo orderItemDeleteEntityRepo;

    @Autowired
    PropertiesRepo propertiesRepo;

    @Autowired
    PrintPrecheckRepo printPrecheckRepo;
    @Autowired
    PrintPaymentRepo printPaymentRepo;

    public void sendToPrinter(long orderId) {
        try {
            List<OrderItem> orderItems = orderItemRepository.getAllByOrderInCart(orderId);
            if (orderItems.size() > 0) {

                PrintKitchenEntity printKitchenEntity = new PrintKitchenEntity();
                printKitchenEntity.setItems(orderItems);
                printKitchenEntity.setOrderId(orderId);
                printKitchenEntity.setCreatedDate(orderRepository.getCreatedDateByOrderId(orderId));
                printKitchenEntity.setDeskNumber(orderRepository.getDeskNumberById(orderId));
                printKitchenEntity.setWaiterName(orderRepository.getWaiterNameById(orderId));
                printKitchenEntity.setHallName(orderRepository.getHallNameById(orderId));
                kitchenPrintEntityRepo.save(printKitchenEntity);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public void printPrecheck(long orderId) {
        try {
            FoodOrder foodOrder = orderRepository.findOrderById(orderId);

            PrintPrecheck printPrecheck = new PrintPrecheck();
            printPrecheck.setPrecheckDate(DateUtil.getDbMmYyyyHhMmSs(new Date()));
            printPrecheck.setFoodOrder(foodOrder);
            printPrecheck.setPrinterName(propertiesRepo.findFirstById(5).getValue1());
            printPrecheck.setWaiterName(foodOrder.getWaiter().getFullName());
            printPrecheck.setHallName(foodOrder.getDesk().getHall().getName());
            printPrecheck.setDeskNumber(foodOrder.getDesk().getNumber());
            printPrecheckRepo.save(printPrecheck);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void printPayment(long orderId) {
        try {
            FoodOrder foodOrder = orderRepository.findOrderById(orderId);

            PrintPayment printPayment = new PrintPayment();
            printPayment.setPrecheckDate(DateUtil.getDbMmYyyyHhMmSs(new Date()));
            printPayment.setFoodOrder(foodOrder);
            printPayment.setPrinterName(propertiesRepo.findFirstById(5).getValue1());
            printPayment.setWaiterName(foodOrder.getWaiter().getFullName());
            printPayment.setHallName(foodOrder.getDesk().getHall().getName());
            printPayment.setDeskNumber(foodOrder.getDesk().getNumber());
            printPaymentRepo.save(printPayment);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void printCancelOrderItem(OrderItemDeleteDTO item, OrderItem orderItem) {
        try {
            OrderItemDeleteEntity orderItemDeleteEntity = new OrderItemDeleteEntity();
            orderItemDeleteEntity.setReason(item.getReason());
            orderItemDeleteEntity.setOrderItem(orderItem);
            orderItemDeleteEntity.setOrderId(orderItem.getGuest().getFoodOrder().getId());
            orderItemDeleteEntity.setWaiterName(orderItem.getGuest().getFoodOrder().getWaiter().getFullName());
            orderItemDeleteEntity.setHallName(orderItem.getGuest().getFoodOrder().getDesk().getHall().getName());
            orderItemDeleteEntity.setDeskNumber(orderItem.getGuest().getFoodOrder().getDesk().getNumber());
            orderItemDeleteEntity.setPrinted(false);
            orderItemDeleteEntity.setDate(new Date());
            orderItemDeleteEntityRepo.save(orderItemDeleteEntity);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}