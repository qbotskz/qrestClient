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
        sendMessageToWaiters(chatId); //Отправка сообщение официанту
    }

    private void sendMessageToWaiters(long chatId) {
        FoodOrder foodOrder = orderRepository.getById(chatId);

        String text = "<b>Гость оформил заказ</b>" + "\n" + "\n" + "<b>Номер стола: </b>" + foodOrder.getDesk().getNumber() + "\n"
                +"<b>Зал: </b>" +  foodOrder.getDesk().getHall().getName() + "\n" + "\n"
                + "Для принятия заказа, зайдите в раздел <b>\"Новые\"</b>";

        String encodedMessageText = URLEncoder.encode(text, StandardCharsets.UTF_8);

        if (foodOrder.getWaiter()!=null){
            if (foodOrder.getWaiter().getChatId()>0){
                sendMessage(foodOrder.getWaiter().getChatId(), encodedMessageText);
            }
        }else{
            List<Employee> waiters = employeeService.getAllActiveWaiters();
            for (Employee w: waiters){
                if (w.getChatId()>0){
                    sendMessage(w.getChatId(), encodedMessageText);
                }
            }
        }
    }

    private void sendMessage(long chatId, String encodedMessageText){
        String url = "https://api.telegram.org/bot"  + propertiesRepo.findById(2).getValue1() + "/sendMessage?chat_id=" + chatId + "&text="+encodedMessageText + "&parse_mode=HTML";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                System.out.println("Response status: " + response.getStatusLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sendMessageToWaiters2(FoodOrder foodOrder) {

        String callMessage = "\uD83D\uDED1\uD83D\uDED1\uD83D\uDED1\uD83D\uDED1\uD83D\uDED1\uD83D\uDED1\uD83D\uDED1" + "\n\n" +  "<b>Стол №%s вызвал Вас!</b>";

        String encodedMessageText = URLEncoder.encode(String.format(callMessage, foodOrder.getDesk().getNumber()), StandardCharsets.UTF_8);


        if (foodOrder.getWaiter()!=null){
            if (foodOrder.getWaiter().getChatId()>0){
                sendMessage(foodOrder.getWaiter().getChatId(), encodedMessageText);
            }
        }else{
            List<Employee> waiters = employeeService.getAllActiveWaiters();
            for (Employee w: waiters){
                if (w.getChatId()>0){
                    sendMessage(w.getChatId(), encodedMessageText);
                }
            }
        }
    }
    private void sendMessageToWaiters3(FoodOrder foodOrder) {

        String callMessage = "\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35" + "\n\n" +  "<b>Стол с №%s запрашивает Счет\uD83D\uDCB5</b>";

        String encodedMessageText = URLEncoder.encode(String.format(callMessage, foodOrder.getDesk().getNumber()), StandardCharsets.UTF_8);


        if (foodOrder.getWaiter()!=null){
            if (foodOrder.getWaiter().getChatId()>0){
                sendMessage(foodOrder.getWaiter().getChatId(), encodedMessageText);
            }
        }else{
            List<Employee> waiters = employeeService.getAllActiveWaiters();
            for (Employee w: waiters){
                if (w.getChatId()>0){
                    sendMessage(w.getChatId(), encodedMessageText);
                }
            }
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
            sendMessageToWaiters3(foodOrder);
            return foodOrder.getCheque();
        }
        return null;
    }






    public boolean isAccept(long orderId) {

        return orderRepository.isAccept(orderId) != null && orderRepository.isAccept(orderId);
    }
}
