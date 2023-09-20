package com.akimatBot.repository.repos;

import com.akimatBot.entity.custom.Guest;
import com.akimatBot.entity.enums.Language;
import com.akimatBot.entity.standart.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByChatId(long chatId);

    User findById(long id);

    User findByPhone(String phone);

    User getByChatId(long chatId);

    User findFirstByChatId(long chatId);

    Integer countUserByChatId(long chatId);

    @Query("select us.cashback from  users us where us.chatId = ?1")
    Integer getCashback(long chatId);

    @Query("select us.language from users us WHERE us.chatId =?1")
    Language getLanguageByChatId(long chatId);

    boolean existsByChatIdAndPhoneNotNull(long chatId);

    @Query("select us.currentGuest from users us where us.chatId = ?1")
    Guest getCurrentGuestOfUser(long chatId);

    @Transactional
    @Modifying
    @Query("update users us set us.currentGuest = ?1 where us.chatId = ?2")
    void setGuest(Guest guestId, long chatId);


    @Query("select case when us.currentGuest.id = ?1 then true else false end from users us where us.chatId = ?2")
    boolean isMe(long id, long chatId);
}
