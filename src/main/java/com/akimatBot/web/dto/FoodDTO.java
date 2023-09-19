package com.akimatBot.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

//@Data
@Getter
@Setter
public class FoodDTO {

    private long id;
    //    private String nameRu;
    private String name;
    //    private String descriptionRu;
//    private String nameKz;
//    private String descriptionKz;
    private String description;
    private Integer price;
    private Long remains;
    private Long countOrders;
    private String lastChanged;
    private RestaurantBranchDTO branch;


    private Boolean activated;
    private Integer specialOfferSum;
    private Integer cashBackPercentage;
    private List<KitchenDTO> kitchens;

    private String photoUrl;
}
