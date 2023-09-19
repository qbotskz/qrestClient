package com.akimatBot.repository.repos;

import com.akimatBot.entity.custom.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
}
