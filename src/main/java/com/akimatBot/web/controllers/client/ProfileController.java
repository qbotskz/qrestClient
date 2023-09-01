package com.akimatBot.web.controllers.client;

import com.akimatBot.entity.enums.Language;
import com.akimatBot.repository.repos.AboutRestaurantRepo;
import com.akimatBot.repository.repos.UserRepository;
import com.akimatBot.services.LanguageService;
import com.akimatBot.services.UserService;
import com.akimatBot.web.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client/profile")
public class ProfileController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    AboutRestaurantRepo aboutRestaurantRepo;

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
}
