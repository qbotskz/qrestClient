package com.akimatBot.services;

import com.akimatBot.entity.custom.*;
import com.akimatBot.entity.standart.User;
import com.akimatBot.repository.repos.GradeRepo;
import com.akimatBot.repository.repos.GradeTextTemplateRepo;
import com.akimatBot.repository.repos.ReviewRepository;
import com.akimatBot.repository.repos.UserRepository;
import com.akimatBot.web.dto.GradeDTO;
import com.akimatBot.web.dto.GradeTextDTO;
import com.akimatBot.web.dto.ReviewsDTO;
import com.akimatBot.web.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ReviewRepository repository;

    @Autowired
    GradeTextTemplateRepo gradeTextTemplateRepo;

    @Autowired
    GradeRepo gradeRepo;

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

    public GradeTextTemplate gradeTextTemplateGetOne(int id){
        return gradeTextTemplateRepo.findById(id);
    }

    public void setGuest(Guest guest, long chatId) {
        userRepository.setGuest(guest, chatId);
    }

    public void setReview(ReviewsDTO reviewsDTO){
        Review review = new Review();
        review.setUser(userRepository.findByChatId(reviewsDTO.getChatId()));
        review.setReviewDate(new Date());
        review.setReviewText(reviewsDTO.getReviewText());
        repository.save(review);
    }

    public List<GradeTextDTO> getTexts(){
        List<GradeTextTemplate> gradeTextTemplates = gradeTextTemplateRepo.findAll();

        List<GradeTextDTO> gradeTextDTOS = gradeTextTemplates.stream().map(g -> new GradeTextDTO(g.getId(),g.getText())).collect(Collectors.toList());

        return gradeTextDTOS;
    }


    public void setGrade(GradeDTO gradeDTO){
        User user = userRepository.findByChatId(gradeDTO.getChatId());
        if (user!=null){
            Guest guest = user.getCurrentGuest();
            if (guest!=null){
                FoodOrder foodOrder = guest.getFoodOrder();
                if (foodOrder!=null){
                    if (foodOrder.getWaiter()!=null){
                        Grade grade = new Grade();
                        grade.setGrade(gradeDTO.getGrade());
                        grade.setDate(new Date());
                        grade.setClientsReview(gradeDTO.getClientsReview());
                        grade.setUser(user);
                        List<GradeTextTemplate> gradeTextTemplates = new ArrayList<>();
                        for (Integer i: gradeDTO.getTextTemplates()){
                            GradeTextTemplate gradeTextTemplate = gradeTextTemplateGetOne(i);
                            gradeTextTemplates.add(gradeTextTemplate);
                        }
                        grade.setTextTemplates(gradeTextTemplates);
                        gradeRepo.save(grade);
                    }
                }
            }
        }
    }
}
