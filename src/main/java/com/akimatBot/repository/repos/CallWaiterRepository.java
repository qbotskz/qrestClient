package com.akimatBot.repository.repos;

import com.akimatBot.entity.custom.CallWaiter;
import com.akimatBot.entity.standart.Button;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CallWaiterRepository extends JpaRepository<CallWaiter, Long> {

    @Query("select cw from CallWaiter cw where cw.id not in " +
            "(select notif.callWaiter.id from NotificationCallWaiter notif where notif.employ.chatId = ?1)")
    List<CallWaiter> getCallWaitersByChatId(long chatId);


}
