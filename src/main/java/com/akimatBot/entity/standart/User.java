package com.akimatBot.entity.standart;

import com.akimatBot.entity.custom.*;
import com.akimatBot.entity.enums.Gender;
import com.akimatBot.entity.enums.Language;
import com.akimatBot.web.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

//@Data
@Entity(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long     id;

    private long        chatId;

    private String      phone;

    private String      fullName;

    private String      userName;

    private Language language;

    private Gender gender;


    private double cashback;

    @ManyToOne
    private Guest currentGuest;

    private Long linkMessageId;

    public UserDTO getDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(this.id);
        userDTO.setFullName(fullName);
        userDTO.setPhone(this.getPhone());
        userDTO.setChatId(this.getChatId());
        return userDTO;
    }
}
