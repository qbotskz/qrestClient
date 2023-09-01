package com.akimatBot.repository.repos;

import com.akimatBot.entity.custom.AboutRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AboutRestaurantRepo extends JpaRepository<AboutRestaurant, Integer> {

    AboutRestaurant findById(int id);
}
