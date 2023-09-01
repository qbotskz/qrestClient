//package com.akimatBot.web.controllers.client;
//
//
//import com.akimatBot.entity.custom.FoodOrder;
//import com.akimatBot.entity.custom.Guest;
//import com.akimatBot.entity.custom.OrderItem;
//import com.akimatBot.entity.enums.Language;
//import com.akimatBot.entity.standart.User;
//import com.akimatBot.repository.repos.GuestRepo;
//import com.akimatBot.repository.repos.OrderItemRepository;
//import com.akimatBot.services.FoodService;
//import com.akimatBot.services.OrderItemService;
//import com.akimatBot.services.OrderService;
//import com.akimatBot.services.UserService;
//import com.akimatBot.web.dto.FoodOrderDTO;
//import com.akimatBot.web.dto.OrderItemDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.jaxb.SpringDataJaxb;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.RequestEntity;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/client/orderHistory")
//public class HistoryController {
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private OrderService orderService;
//    @Autowired
//    private OrderItemService orderItemService;
//
//    @Autowired
//    private GuestRepo guestRepo;
//
//    @GetMapping("/getHistory")
//    public ResponseEntity<?> getHistory(@RequestParam(name = "chatId")String chatId){
//        User user = userService.getUserByChatId(Long.parseLong(chatId));
//        if (user!=null){
//            List<FoodOrderDTO> foodOrders = new ArrayList<>();
//            List<Guest> guests = guestRepo.findAllByClientChatId(Long.parseLong(chatId));
//            for (Guest g: guests){
//                FoodOrder foodOrder = g.getFoodOrder();
//                foodOrders.add(foodOrder.getFoodOrderDTO(Language.ru,Long.parseLong(chatId)));
//            }
//            return new ResponseEntity<>(foodOrders,HttpStatus.OK);
//        }else{
//            return new ResponseEntity<>("{\"status\":\"user is null\"}", HttpStatus.FORBIDDEN);
//        }
//    }
//
//
//
//}
