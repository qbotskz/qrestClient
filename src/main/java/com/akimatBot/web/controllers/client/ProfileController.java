package com.akimatBot.web.controllers.client;

import com.akimatBot.entity.custom.FoodOrder;
import com.akimatBot.entity.custom.Guest;
import com.akimatBot.entity.enums.Language;
import com.akimatBot.entity.standart.User;
import com.akimatBot.repository.repos.AboutRestaurantRepo;
import com.akimatBot.repository.repos.GradeTextTemplateRepo;
import com.akimatBot.repository.repos.UserRepository;
import com.akimatBot.services.LanguageService;
import com.akimatBot.services.UserService;
import com.akimatBot.web.dto.GradeDTO;
import com.akimatBot.web.dto.ReviewsDTO;
import com.akimatBot.web.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client/profile")
public class ProfileController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    AboutRestaurantRepo aboutRestaurantRepo;

    @Autowired
    GradeTextTemplateRepo gradeTextTemplateRepo;
    @PostMapping("/setLanguage")
    public void setLanguage(@RequestBody Language language,
                            @RequestHeader Long chatId){

        LanguageService.setLanguage(chatId, language);

    }

    @GetMapping("/getUser")
    public UserDTO getUser(@RequestHeader Long chatId){

        return userRepository.getByChatId(chatId).getDTO();

    }

    @GetMapping("/getLanguage")
    public Language getLanguage(@RequestHeader Long chatId){

        return LanguageService.getLanguage(chatId);

    }

    @PostMapping("/saveUser")
    public void saveUser(
                            @RequestBody UserDTO userDTO){

        userService.saveDTO(userDTO);

    }

    @GetMapping("/aboutRest")
    private ResponseEntity<Object> getAboutRest(@RequestHeader Long chatId){
        return new ResponseEntity<>(LanguageService.getAboutRest(chatId), HttpStatus.OK);
    }

    @PostMapping("/reviews")
    private ResponseEntity<Object> setReviews(@RequestBody ReviewsDTO reviewsDTO){
        userService.setReview(reviewsDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/gradeText")
    private ResponseEntity<?> getGradeText(@RequestHeader Long chatId){
        return new ResponseEntity<>(userService.getTexts(), HttpStatus.OK);
    }

    @PostMapping("/setGrade")
    private ResponseEntity<Object> setGrade(@RequestBody GradeDTO gradeDTO){
        User user = userRepository.findByChatId(gradeDTO.getChatId());
        if (user!=null){
            Guest guest = user.getCurrentGuest();
            if (guest!=null){
                FoodOrder foodOrder = guest.getFoodOrder();
                if (foodOrder!=null){
                    if (foodOrder.getWaiter()!=null){
                        userService.setGrade(gradeDTO);
                        return new ResponseEntity<>(HttpStatus.OK);
                    }else {
                        return new ResponseEntity<>("status:\"waiter null\"",HttpStatus.NOT_FOUND);
                    }
                }else{
                    return new ResponseEntity<>("status:\"foodOrder null\"",HttpStatus.NOT_FOUND);
                }
            }else{
                return new ResponseEntity<>("status:\"guest null\"",HttpStatus.NOT_FOUND);
            }
        }else{
            return new ResponseEntity<>("status:\"user null\"",HttpStatus.NOT_FOUND);
        }
    }
}
