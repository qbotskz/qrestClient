package com.akimatBot.services;

import com.akimatBot.RestoranApplication;
import com.akimatBot.entity.custom.*;
import com.akimatBot.entity.enums.OrderItemStatus;
import com.akimatBot.entity.enums.OrderStatus;
import com.akimatBot.entity.enums.OrderType;
import com.akimatBot.entity.standart.Employee;
import com.akimatBot.repository.repos.*;
import com.akimatBot.web.dto.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.lang.reflect.Array;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Autowired
    CallWaiterRepository callWaiterRepository;

    @Autowired
    EmployeeService employeeService;
    @Autowired
    OrderService orderService;

    @Autowired
    RequestChequeRepo requestChequeRepo;


    @Transactional
    public FoodOrder createNewOrder(long chatId, long deskId){

        FoodOrder foodOrder;
        Guest guest = userService.getCurrentGuestOfUser(chatId);

        if (guest == null) {
            Desk desk = deskRepo.findById(deskId);
            if (desk.getCurrentOrder() != null){
                foodOrder = desk.getCurrentOrder();
            }
            else {
                foodOrder = new FoodOrder();
                foodOrder.setCreatedDate(new Date());
                foodOrder.setDesk(desk);
                foodOrder.setOrderType(OrderType.in_the_restaurant);
                foodOrder.setOrderStatus(OrderStatus.NEW);

                Cheque cheque = new Cheque();
                cheque.setService(Double.parseDouble(propertiesRepo.findFirstById(4).getValue1()));
                cheque = chequeRepo.save(cheque);

                foodOrder.setCheque(cheque);
                foodOrder = orderRepository.save(foodOrder);
                desk.setCurrentOrder(foodOrder);
            }
            guest = new Guest();
            guest.setClient(userService.getUserByChatId(chatId));
            guest.setFoodOrder(foodOrder);
            guest.setCreatedDate(new Date());
            guest = guestRepo.save(guest);
            userService.setGuest(guest , chatId);

        }
        else {
            return guest.getFoodOrder();
        }


        return foodOrder;

    }

    public FoodOrder getActiveOrderInRestaurant(long chatId) {
        return userService.getCurrentGuestOfUser(chatId).getFoodOrder();
//        return orderRepository.getLastClientOrder2(chatId, OrderType.in_the_restaurant);
    }

    public List<FoodOrderDTO> getDoneOrdersOfClient(long chatId) {
        List<FoodOrderDTO> foodOrderDTOS = new ArrayList<>();
        for (FoodOrder foodOrder : orderRepository.getDoneOrdersOfClient(chatId)){
            foodOrderDTOS.add(foodOrder.getClientFoodOrderDTO(LanguageService.getLanguage(chatId)));
        }
        return foodOrderDTOS;
    }


    public AnswerAddToCartDTO addToCart(long chatId, long foodId) {
//        Guest guest = guestRepo.findByClientChatIdAndFoodOrderId(chatId, foodOrderId);
        Guest guest = userService.getCurrentGuestOfUser(chatId);
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


        AnswerAddToCartDTO answerAddToCartDTO = new AnswerAddToCartDTO();
        answerAddToCartDTO.setOrderItem(orderItem.getOrderItemDTO(LanguageService.getLanguage(chatId)));
        Cheque cheque = chequeRepo.getByGuest(guest.getId());
        answerAddToCartDTO.setCheque(cheque.getChequeDTO());

        return answerAddToCartDTO;
    }

    public void place(long chatId) {
        orderItemRepository.orderingByClient(chatId);
        orderRepository.placeOrder(chatId);
//        sendMessageToWaiters(chatId); //Отправка сообщение официанту

    }

    private void sendMessageToWaiters(long chatId) {
        FoodOrder foodOrder = orderRepository.getById(chatId);
        if (foodOrder.getWaiter()!=null){
            if (foodOrder.getWaiter().getChatId()>0){
                sendMessage(foodOrder.getWaiter().getChatId(), foodOrder.getDesk());
            }
        }else{
            List<Employee> waiters = employeeService.getAllActiveWaiters();
            for (Employee w: waiters){
                if (w.getChatId()>0){
                    sendMessage(w.getChatId(), foodOrder.getDesk());
                }
            }
        }
    }

    private void sendMessage(long chatId, Desk desk){
        String text = "Номер стола: " + desk.getNumber() + "\n"
        + desk.getHall().getName() + "\n"
                + "Заказывает что-то";

        String encodedMessageText = URLEncoder.encode(text, StandardCharsets.UTF_8);

        String url = "https://api.telegram.org/bot6147742087:AAEwAQfIIs04vNSK6sGX5wfwK68gUhmkHd8/sendMessage?chat_id=" + chatId + "&text="+encodedMessageText;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                System.out.println("Response status: " + response.getStatusLine());
                // Дополнительная обработка ответа, если необходимо
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sendMessageToWaiters2(FoodOrder foodOrder) {
        if (foodOrder.getWaiter()!=null){
            if (foodOrder.getWaiter().getChatId()>0){
                sendMessage2(foodOrder.getWaiter().getChatId(), foodOrder);
            }
        }else{
            List<Employee> waiters = employeeService.getAllActiveWaiters();
            for (Employee w: waiters){
                if (w.getChatId()>0){
                    sendMessage2(w.getChatId(), foodOrder);
                }
            }
        }
    }

    private void sendMessage2(long chatId, FoodOrder foodOrder){
        String callMessage = "Стол с номером %s вызывает официанта!";

        String encodedMessageText = URLEncoder.encode(String.format(callMessage, foodOrder.getDesk().getNumber()), StandardCharsets.UTF_8);

        String url = "https://api.telegram.org/bot" + propertiesRepo.findById(2).getValue1() + "/sendMessage?chat_id=" + chatId + "&text="+encodedMessageText;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                System.out.println("Response status: " + response.getStatusLine());
                // Дополнительная обработка ответа, если необходимо
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
//        Cheque cheque = foodOrder.getCheque();
//        cheque.addTotal(-(orderItem.getTotal()));
//        chequeRepo.save(cheque);


        orderItemRepository.setDelete(orderItem);
    }

    public AnswerAddToCartDTO decreaseOrderItem(long orderItemId, long chatId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId);

        if (orderItem != null && orderItem.getOrderItemStatus().equals(OrderItemStatus.IN_CART_CLIENT)){

            if (orderItem.getQuantity() == 1){
                orderItemRepository.setDelete(orderItem);
            }
            else {
                orderItem.minusQuantity();
                orderItemRepository.save(orderItem);
            }


//            Cheque cheque1e = orderItem.getGuest().getFoodOrder().getCheque();
//            cheque1e.addTotal(-orderItem.getPrice());
//            chequeRepo.save(cheque1e);

            AnswerAddToCartDTO answerAddToCartDTO = new AnswerAddToCartDTO();
            answerAddToCartDTO.setOrderItem(orderItem.getOrderItemDTO(LanguageService.getLanguage(chatId)));
            Cheque cheque = chequeRepo.getByGuest(orderItem.getGuest().getId());
            answerAddToCartDTO.setCheque(cheque.getChequeDTO());

            return answerAddToCartDTO;
        }

        return null;

    }

    public CallWaiterDTO callWaiter(FoodOrderDTO foodOrderDTO) {

        FoodOrder foodOrder = orderRepository.findById(foodOrderDTO.getId());

//        Desk desk = deskRepo.findById(deskDTO.getId());
        if (foodOrder != null){
            CallWaiter callWaiter = new CallWaiter();
            callWaiter.setCreatedDate(new Date());
            callWaiter.setFoodOrder(foodOrder);
            callWaiterRepository.save(callWaiter);
            sendMessageToWaiters2(foodOrder);
//            sendCalls(foodOrder);
            return callWaiter.getDTO();
        }
        return null;
    }

    private void sendCalls(FoodOrder foodOrder){
        String callMessage = "Стол с номером %s вызывает официанта!";
        if (foodOrder != null){
            if (foodOrder.getWaiter() != null){
                sendMessage(String.format(callMessage, foodOrder.getDesk().getNumber()),foodOrder.getDesk().getCurrentOrder().getWaiter().getChatId() );
            }
            else{
                List<Employee> employees = employeeService.getAllActiveWaiters();
                for (Employee employee : employees){
                    sendMessage(String.format(callMessage, foodOrder.getDesk().getNumber()),employee.getChatId() );
                }
            }
        }
    }

    private int sendMessage(String text, Long chatId){
        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(text);
            sendMessage.setParseMode("html");
            return RestoranApplication.bot.execute(sendMessage).getMessageId();

        }catch (Exception e){
//            e.printStackTrace();
        }
        return 0;
    }

    @Transactional
    public Cheque requestCheque(FoodOrderDTO foodOrderDTO) {
        String text = "Стол с номером %s запрашивает счет!";
        FoodOrder foodOrder = orderService.findById(foodOrderDTO.getId());
        if (foodOrder != null){
            RequestCheque requestCheque = new RequestCheque();
            requestCheque.setCreatedDate(new Date());
            requestCheque.setFoodOrder(foodOrder);
            requestChequeRepo.save(requestCheque);
            if (foodOrder.getWaiter() != null){
                sendMessage(String.format(text, foodOrder.getDesk().getNumber()),foodOrder.getWaiter().getChatId() );
            }
            return foodOrder.getCheque();
        }
        return null;
    }

    public boolean isAccept(long orderId) {

        return orderRepository.isAccept(orderId) != null && orderRepository.isAccept(orderId);
    }
}
