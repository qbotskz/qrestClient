package com.akimatBot.services;

import com.akimatBot.entity.custom.Guest;
import com.akimatBot.entity.standart.User;
import com.akimatBot.repository.repos.UserRepository;
import com.akimatBot.web.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Transactional
    public void saveDTO(UserDTO userDTO){
        User user = userRepository.findById(userDTO.getId());
        user.setFullName(userDTO.getFullName());
        user.setPhone(userDTO.getPhone());
        userRepository.save(user);
    }

    public User getUserByChatId(long chatId){
        return userRepository.getByChatId(chatId);
    }

    public Guest getCurrentGuestOfUser(long chatId){
        return userRepository.getCurrentGuestOfUser(chatId);
    }


    public void setGuest(Guest guest, long chatId) {
        userRepository.setGuest(guest, chatId);
    }
}
