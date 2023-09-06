package com.akimatBot.web.controllers.client;

import com.akimatBot.entity.custom.Cheque;
import com.akimatBot.entity.custom.Desk;
import com.akimatBot.entity.custom.FoodOrder;
import com.akimatBot.entity.custom.OrderItem;
import com.akimatBot.repository.repos.*;
import com.akimatBot.services.*;
import com.akimatBot.web.dto.*;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client/order")

public class OrderController {

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    MessageRepository messageRepo;


    @Autowired
    CartItemService cartItemService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    PropertiesRepo propertiesRepo;

    @Autowired
    ClientOrderService clientOrderService;

    @Autowired
    ChequeRepo chequeRepo;

    @Autowired
    DeskRepo deskRepo;

    @Autowired
    OrderItemRepository orderItemRepository;





    //перед отправкой запроса на этот метод, должны убедиться что со стороны телеграм бота(в комманде) отправляется параметр isNewClient в WebApp
    //после открытия страницы с фронта если есть параметр isNewClient то отправляется запрос на этот параметр

    //нужно проверить активный ли этот стол
    //выаыва ыва
    @PostMapping("/createOrder")
    public ResponseEntity<Object> createOrderRest(@RequestHeader("chatId") long chatId,
                                             @RequestParam("deskId") long deskId
    ){
        FoodOrder foodOrder = clientOrderService.createNewOrder(chatId,deskId);
        return new ResponseEntity<>(foodOrder.getDesk().getDeskDTOFull(LanguageService.getLanguage(chatId)), HttpStatus.OK);
    }

    @GetMapping("/getOrder/active")
    public ResponseEntity<Object> getActiveOrder(@RequestHeader("chatId") long chatId){
        FoodOrder order = clientOrderService.getActiveOrderInRestaurant(chatId);
        if(order != null) {
            return new ResponseEntity<>(order.getDesk().getDeskDTOFull(LanguageService.getLanguage(chatId)),
                    HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new AnswerDTO("order is empty").getJson(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getOrder/button")
    public ResponseEntity<?> getButtonForPay(@RequestParam("chatId")long chatId){
        FoodOrder order = clientOrderService.getActiveOrderInRestaurant(chatId);
        if(order != null) {
            if (order.getWaiter()!=null){
                return new ResponseEntity<>("status:\"true\"",
                        HttpStatus.OK);
            }else{
                return new ResponseEntity<>("status:\"false\"",
                        HttpStatus.OK);
            }
        }else{
            return new ResponseEntity<>(new AnswerDTO("order is empty").getJson(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addToCart")
    public ResponseEntity<Object> addToCart(@RequestHeader("chatId") long chatId,
                                             @RequestParam("foodId") long foodId
    ){
        try {
            return new ResponseEntity<>(clientOrderService.addToCart(chatId, foodId), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }

    }


    @PostMapping("/orderItem/delete")
    public ResponseEntity<Object> deleteOrderItem(
            @RequestBody OrderItemDeleteDTO item,
            @RequestHeader(value="chatId") long chatId
    ){

        try {

            OrderItem orderItem1 = orderItemRepository.getOne(item.getOrderItem().getId());
            Cheque cheque = orderItem1.getGuest().getFoodOrder().getCheque();
            if (clientOrderService.cancelOrderItem(item)){
                return new ResponseEntity<>(cheque.getChequeDTO(), HttpStatus.OK);
            }
            return new ResponseEntity<>("Problemka",HttpStatus.FORBIDDEN);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }

    }
    @PostMapping("/orderItem/decrease")
    public ResponseEntity<Object> editCountOfOrderItem(
            @RequestParam ("orderItemId") long orderItemId,
            @RequestHeader(value="chatId") long chatId
    ){

        try {
            return new ResponseEntity<>(clientOrderService.decreaseOrderItem(orderItemId,chatId), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/place")
    public ResponseEntity<Object> place(@RequestHeader("chatId") long chatId){
        try {
            clientOrderService.place(chatId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("/getOrder/done")
    public ResponseEntity<Object> getDoneOrders(@RequestHeader("chatId") long chatId){
        List<FoodOrderDTO> order = clientOrderService.getDoneOrdersOfClient(chatId);
        if(order != null && order.size() != 0) {
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new AnswerDTO("order is empty").getJson(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getOrder/{id}")
    public ResponseEntity<Object> getOneOrders(@PathVariable("id") long id,
                                               @RequestHeader("chatId") long chatId){
        FoodOrder order =  orderRepo.findOrderById(id);
            return new ResponseEntity<>(order.getClientFoodOrderDTO(LanguageService.getLanguage(chatId)),
                    HttpStatus.OK);
    }

    @PostMapping("/callWaiter")
    public ResponseEntity<Object> callWaiter(@RequestBody FoodOrderDTO foodOrderDTO ){
        CallWaiterDTO callWaiterDTO = clientOrderService.callWaiter(foodOrderDTO);
        if (callWaiterDTO != null){
            return new ResponseEntity<>(callWaiterDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/requestCheque")
    public ResponseEntity<Object> requestCheque(@RequestBody FoodOrderDTO foodOrderDTO ){
        Cheque cheque = clientOrderService.requestCheque(foodOrderDTO);
        if (cheque != null){
            return new ResponseEntity<>(cheque.getChequeDTO(), HttpStatus.OK);
        }
        return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/isAccept")
    public ResponseEntity<Object> isAccept(@RequestParam("orderId") long orderId){

        return new ResponseEntity<>(clientOrderService.isAccept(orderId), HttpStatus.OK);

    }



//    @GetMapping("/activeOrders")
//    public List<FoodOrderDTO> getActiveOrders(){
//        List<Map<Object, Object>>  orders = new ArrayList<>();
//        for (FoodOrder order:orderService.findActiveOrdersOrderByDate()){
//            orders.add(order.getFoodOrderDTO(userService.getLanguageByChatId(createOrder())));
//        }
//
//        return orders;
//    }


    @PostMapping("/createOrder/inRestaurant/reOrder")
    public FoodOrderDTO reOrderRest(@RequestParam("chatId") long chatId){

//        User user = userService.findByChatId(chatId);
//        userService.save(user);
//
//        FoodOrder foodOrder = orderService.getActiveOrderInRestaurant(chatId);
//
//        foodOrder = orderService.fillOrderWithCardItemsAndSave(foodOrder, chatId);
//        cartItemService.clearUserCart(chatId);
//
//        return foodOrder.getFoodOrderDTO(user.getLanguage(), null);
        return null;
    }




    //method for client // todo overwrite
    @PostMapping("/createOrder/takeout")
    public FoodOrderDTO createOrder(@RequestParam("chatId") long chatId,
                                           @RequestParam("useCashback") boolean useCashback,
                                           @RequestParam("deliverNeed") boolean deliverNeed,
                                           @RequestParam("address") String address,
                                           @RequestParam("fullName") String fullName,
                                           @RequestParam("phoneNumber") String phoneNumber){

//        User user = userService.findByChatId(chatId);
//        user.setPhone(phoneNumber);
//        user.setFullName(fullName);
//        userService.save(user);
//
//        FoodOrder foodOrder = new FoodOrder();
////        foodOrder.setClient(user);
////        foodOrder.setUseCashback(useCashback);
//        foodOrder.setDeliverNeed(deliverNeed);
//        foodOrder.setOrderType(deliverNeed?OrderType.courier:OrderType.takeout);
////        order.setDeliveryPrice(deliverNeed?Integer.parseInt(propertiesRepo.findFirstById(3).getValue1()):0);
////        foodOrder.setDeliveryPrice(deliverNeed?1500:0);
//
//        foodOrder.setAddress(address);
//
//        Cheque cheque = new Cheque();
//        cheque.setUseCashback(useCashback);
//        cheque.setDeliveryPrice(deliverNeed?Integer.parseInt(propertiesRepo.findFirstById(3).getValue1()):0);
//
//        foodOrder = orderService.fillOrderWithCardItemsAndSave(foodOrder, chatId);
//        sendPayPage(foodOrder);
//        return foodOrder.getFoodOrderDTO(user.getLanguage(), null);

        return null;
    }


//    @PostMapping("/closeOrder")
//    public Map<Object, Object> closeOrder(@RequestParam("chatId") long chatId){
//        FoodOrder foodOrder = orderRepo.getLastClientOrder(chatId, OrderType.in_the_restaurant);
//        if(foodOrder != null) {
//            return orderService.closeOrderOfTable(foodOrder).getJson();
//        }
//        else {
//            return new AnswerDTO("order is not found").getJson();
//        }
//    }

    protected void sendPayPage(FoodOrder foodOrder) {
//        PaymTech paymTech = new PaymTech();
//        Long pOrderId;
//        if (order.getPaymTechOrderId() == null)
//            pOrderId = paymTech.createOrder(order.getTotal(), order.getId(), order.getUser().getChatId());
//        else pOrderId = order.getPaymTechOrderId();
//
//        if (pOrderId != null){
//            order.setPaymTechOrderId(pOrderId);
//            orderService.save(order);
//        }
    }
}
