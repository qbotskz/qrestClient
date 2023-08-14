package com.akimatBot.web.controllers.client;

import com.akimatBot.entity.custom.*;
import com.akimatBot.entity.enums.Language;
import com.akimatBot.entity.enums.OrderItemStatus;
import com.akimatBot.repository.repos.*;
import com.akimatBot.services.*;
import com.akimatBot.utils.pdfDocument.PDFGenerator;
import com.akimatBot.utils.reports.OrderReportDaily;
import com.akimatBot.web.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController()
@RequestMapping("/api/client")
public class FoodController {

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
    OrderService orderService;

    @Autowired
    DeskRepo deskRepo;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ChequeRepo chequeRepo;

    @Autowired
    GuestRepo guestRepo;

    @Autowired
    WaiterShiftService waiterShiftService;

    @Autowired
    WaiterService waiterService;

    @Autowired
    PaymentMethodRepo paymentMethodRepo;

    @Autowired
    FoodService foodService;

    @Autowired
    PDFGenerator pdfGenerator;

    @Autowired
    OrderReportDaily orderReportDaily;

    @Autowired
    FoodCategoryRepo foodCategoryRepo;

    @Value("${photosPath}")
    String photosPath;
    Language language = Language.ru;


//    @PreAuthorize("@permissionEvaluator.isOpenShift()")
    @GetMapping("/searchFood")
    public  List<FoodDTO> searchBook(@RequestParam("foodName") String foodName,
                                     @RequestParam("page") int page,
                                     @RequestHeader(name = "chatId") Long chatId){

        List<FoodDTO> maps = new ArrayList<>();
        Language language = LanguageService.getLanguage(chatId);
        List<Food> foods = foodService.searchFood(foodName, page,language);
        for (Food food : foods){
            maps.add(food.getFoodDTO(language));
        }
        return maps ;
    }

    @GetMapping("/getCategories")
    public List<FoodCategoryDTO> getCategoriesWithFoods(@RequestHeader Long chatId){
        Language language = LanguageService.getLanguage(chatId);

        List<FoodCategoryDTO> foodCategoryDTOS = new ArrayList<>();

        for (FoodCategory sub : foodCategoryRepo.findAllByOrderById()){
            foodCategoryDTOS.add((sub.getDTO(language)));
        }

        return foodCategoryDTOS;
    }

    @GetMapping("/getLanguage")
    public Language getLanguage(){
        return Language.ru;
    }

    @GetMapping("/photos/{fileName}")
    public ResponseEntity<Resource> getPhoto(@PathVariable(name = "fileName") String fileName) throws IOException {
        Path photoPath = Paths.get(photosPath).resolve(fileName);
        Resource resource = new UrlResource(photoPath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok().body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }






}