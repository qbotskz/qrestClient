package com.akimatBot.repository.repos;

import com.akimatBot.entity.custom.PrintPrecheck;
import com.akimatBot.entity.standart.Button;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface PrintPrecheckRepo extends JpaRepository<PrintPrecheck, Long> {

    List<PrintPrecheck> findAllByOrderById();

    @Transactional
    @Modifying
    @Query("update PrintPrecheck pr set pr.printed = true where pr.id = ?1")
    void setPrinted(long id);

    @Transactional
    @Modifying
    @Query("update PrintPrecheck pp set pp.cancelled = true, pp.cancelPrecheckDate = ?2 where pp.foodOrder.id = ?1")
    void setCancelled(long orderId, Date cancelPrecheck);

    List<PrintPrecheck> findAllByCancelPrecheckDateBetween(Date start, Date end);

}
