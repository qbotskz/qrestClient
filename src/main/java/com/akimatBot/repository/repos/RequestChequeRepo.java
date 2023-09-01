package com.akimatBot.repository.repos;

import com.akimatBot.entity.custom.RequestCheque;
import com.akimatBot.entity.standart.Button;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RequestChequeRepo extends JpaRepository<RequestCheque, Long> {

}
