package com.akimatBot.repository.repos;

import com.akimatBot.entity.custom.GradeTextTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeTextTemplateRepo extends JpaRepository<GradeTextTemplate, Integer> {
    GradeTextTemplate findById(int id);
}
