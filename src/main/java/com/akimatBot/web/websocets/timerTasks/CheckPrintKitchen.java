package com.akimatBot.web.websocets.timerTasks;

import com.akimatBot.entity.custom.OrderItem;
import com.akimatBot.entity.enums.Language;
import com.akimatBot.web.dto.OrderItemDTO;
import com.akimatBot.web.websocets.entities.KitchenPrintEntityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

@Service
public class CheckPrintKitchen extends TimerTask {

    @Autowired
    KitchenPrintEntityRepo kitchenPrintEntityRepo;

    public static List<OrderItemDTO> getItemsStr(List<OrderItem> orderItems) {
        List<OrderItemDTO> dtos = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            dtos.add(orderItem.getOrderItemDTO(Language.ru));
        }
        return dtos;
    }

    @Override
    public void run() {
//        List<PrintKitchenEntity> printKitchenEntities = kitchenPrintEntityRepo.findAllByOrderById();
//        for (PrintKitchenEntity printKitchenEntity : printKitchenEntities){
//
//
//            WebSocketSession socketSession = WebSocketSessionManager.getSession(KitchenPrint.handlerId);
//            if (socketSession != null && socketSession.isOpen()) {
//                try {
//                    socketSession.sendMessage(new TextMessage(new Gson().toJson(printKitchenEntity.getDTO())));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }
}
