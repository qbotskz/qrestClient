//package com.akimatBot.web.controllers.printer;
//
//
//
//import com.akimatBot.entity.custom.PrintPayment;
//import com.akimatBot.entity.custom.PrintPrecheck;
//import com.akimatBot.repository.repos.PrintPaymentRepo;
//import com.akimatBot.repository.repos.PrintPrecheckRepo;
//import com.akimatBot.web.dto.*;
//import com.akimatBot.web.websocets.entities.KitchenPrintEntityRepo;
//import com.akimatBot.web.websocets.entities.OrderItemDeleteEntity;
//import com.akimatBot.web.websocets.entities.OrderItemDeleteEntityRepo;
//import com.akimatBot.web.websocets.entities.PrintKitchenEntity;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/printer")
//public class PrinterController {
//
//
//    @Autowired
//    KitchenPrintEntityRepo kitchenPrintEntityRepo;
//
//    @Autowired
//    OrderItemDeleteEntityRepo orderItemDeleteEntityRepo;
//    @Autowired
//    PrintPrecheckRepo printPrecheckRepo;
//
//    @Autowired
//    PrintPaymentRepo printPaymentRepo;
//
//
//    @GetMapping("/getAvailablePrintKitchens")
//    public ResponseEntity<Object> getAllTable(){
//        List<PrintKitchenEntity> printKitchenEntities = kitchenPrintEntityRepo.findAllByOrderById();
//        List<PrintKitchenDTO> dtos = new ArrayList<>();
//
//        for (PrintKitchenEntity printKitchenEntity : printKitchenEntities){
//            dtos.add(printKitchenEntity.getDTO());
//        }
//        return new ResponseEntity<>(dtos, HttpStatus.OK);
//    }
//
//
//
//    @GetMapping("/getAllCancelPrint")
//    public ResponseEntity<Object> getAllCancelPrint(){
//        List<OrderItemDeleteEntity> deleteEntities = orderItemDeleteEntityRepo.findAllByPrintedFalseOrderById();
//        List<OrderItemDeleteDTO> dtos = new ArrayList<>();
//
//        for (OrderItemDeleteEntity orderItemDeleteEntity : deleteEntities){
//            dtos.add(orderItemDeleteEntity.getDTO());
//        }
//        return new ResponseEntity<>(dtos, HttpStatus.OK);
//    }
//
//    @PostMapping("/updatePrintKitchen")
//    public void updatePrintKitchen(@RequestBody PrintKitchenDTO printKitchenDTO){
//        System.out.println(printKitchenDTO.toString());
//        kitchenPrintEntityRepo.delete(new PrintKitchenEntity(printKitchenDTO.getId()));
//    }
//
//
//    @PostMapping("/updateCancelOrderItem")
//    public void updateCancelOrderItem(@RequestBody OrderItemDeleteDTO orderItemDeleteDTO){
//        System.out.println(orderItemDeleteDTO.toString());
//        orderItemDeleteEntityRepo.setPrinted(orderItemDeleteDTO.getId());
//    }
//
//    //todo precheck
//
//    @GetMapping("/getAllPrecheck")
//    public ResponseEntity<Object> getAllPrecheck(){
//
//        List<PrintPrecheck> printPrechecks = printPrecheckRepo.findAllByPrintedFalseOrderById();
//        List<PrintPrecheckDTO> dtos = new ArrayList<>();
//        for (PrintPrecheck printPrecheck : printPrechecks){
//            dtos.add(printPrecheck.getDTO());
//        }
//        return new ResponseEntity<>(dtos, HttpStatus.OK);
//    }
//
//    @PostMapping("/updatePrintPrecheck")
//    public void updatePrintPrecheck(@RequestBody PrintPrecheckDTO printPrecheckDTO){
//        System.out.println(printPrecheckDTO.toString());
//        printPrecheckRepo.setPrinted(printPrecheckDTO.getId());
//    }
//
//    //todo payment
//
//    @GetMapping("/getPayments")
//    public ResponseEntity<Object> getPayments(){
//
//        List<PrintPayment> printPayments = printPaymentRepo.findAllByOrderById();
//        List<PrintPaymentDTO> dtos = new ArrayList<>();
//        for (PrintPayment printPayment : printPayments){
//            dtos.add(printPayment.getDTO());
//        }
//        return new ResponseEntity<>(dtos, HttpStatus.OK);
//    }
//
//    @PostMapping("/updatePayment")
//    public void updatePayment(@RequestBody PrintPaymentDTO printPaymentDTO){
//        System.out.println(printPaymentDTO.toString());
//        printPaymentRepo.delete(new PrintPayment(printPaymentDTO.getId()));
//    }
//
//}
