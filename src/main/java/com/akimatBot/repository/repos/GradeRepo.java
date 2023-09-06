package com.akimatBot.repository.repos;

import com.akimatBot.entity.custom.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepo extends JpaRepository<Grade, Integer> {
}
