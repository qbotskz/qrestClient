package com.akimatBot.web.controllers.client;

import com.akimatBot.entity.enums.Language;
import com.akimatBot.repository.repos.UserRepository;
import com.akimatBot.services.LanguageService;
import com.akimatBot.services.UserService;
import com.akimatBot.web.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client/profile")
public class ProfileController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

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

}
