package com.akimatBot.entity.custom;

import com.akimatBot.entity.enums.Language;
import com.akimatBot.entity.enums.OrderStatus;
import com.akimatBot.entity.enums.OrderType;
import com.akimatBot.entity.standart.Employee;
import com.akimatBot.entity.standart.User;
import com.akimatBot.repository.TelegramBotRepositoryProvider;
import com.akimatBot.services.WaiterService;
import com.akimatBot.utils.DateUtil;
import com.akimatBot.web.dto.FoodOrderDTO;
import com.akimatBot.web.dto.GuestDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Entity
public class FoodOrder {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Date createdDate;
    @ManyToOne
    private Employee waiter;

    @ManyToOne
    Desk desk;

    @OneToMany(mappedBy="foodOrder")
    private List<Guest> guests;

    private Boolean deliverNeed = false;

    private String address;


    private Date completionDate;

    private OrderType orderType;

    @OneToOne
    Cheque cheque;

    @Enumerated
    OrderStatus orderStatus;


    public FoodOrder() {

    }
    public List<Guest> getGuests() {
        guests = TelegramBotRepositoryProvider.getGuestRepo()
                .findAllByFoodOrderAndDeletedFalseOrderByIdAsc(this);
        return guests;
    }
    public long getGuestsSize() {
        return  TelegramBotRepositoryProvider.getGuestRepo()
                .getGuestsSizeOfOrder(this.id);
    }



    public void addGuest(Guest guest){
        if (guests == null)
            guests = new ArrayList<>();

        guests.add(guest);
    }


    public User getClientByChatId(long chatId) {
        if (guests!= null){
            for (Guest guest : guests){
                if (guest.getClient().getChatId() == chatId){
                    return guest.getClient();
                }
            }
        }
        return null;
    }

    public void minusItem(OrderItem orderItem, int quantity) {

        if (orderItem.getQuantity() == quantity){
            TelegramBotRepositoryProvider.getOrderItemRepository().delete(orderItem);
        }
        else {
            orderItem.setQuantity(orderItem.getQuantity() - quantity);
            TelegramBotRepositoryProvider.getOrderItemRepository().save(orderItem);
        }
    }

    public FoodOrderDTO getFoodOrderDTO(Language language) {

        FoodOrderDTO foodOrderDTO = new FoodOrderDTO();
        foodOrderDTO.setId(this.id);
        foodOrderDTO.setOrderStatus(this.orderStatus);
        foodOrderDTO.setDeliverNeed(this.deliverNeed);
        foodOrderDTO.setAddress(this.address);
        foodOrderDTO.setOrderType(this.orderType);
        foodOrderDTO.setCreatedDate(DateUtil.getOnlyOClock(this.createdDate));
//        foodOrderDTO.setCompletionDate(this.completionDate);
        foodOrderDTO.setCheque(this.cheque.getChequeDTO());
        foodOrderDTO.setGuests(getGuestsDTO(language));
        foodOrderDTO.setWaiter(WaiterService.getWaiterByUserSimple(this.getWaiter()));

        return foodOrderDTO;
    }
    public FoodOrderDTO getClientFoodOrderDTO(Language language) {

        FoodOrderDTO foodOrderDTO = new FoodOrderDTO();
        foodOrderDTO.setId(this.id);
        foodOrderDTO.setOrderStatus(this.orderStatus);
        foodOrderDTO.setDeliverNeed(this.deliverNeed);
        foodOrderDTO.setAddress(this.address);
        foodOrderDTO.setOrderType(this.orderType);
        foodOrderDTO.setCreatedDate(DateUtil.getOnlyOClock(this.createdDate));
//        foodOrderDTO.setCompletionDate(this.completionDate);
        foodOrderDTO.setCheque(this.cheque.getChequeDTO());
        foodOrderDTO.setGuests(getGuestsDTO(language));
        foodOrderDTO.setWaiter(WaiterService.getWaiterByUserSimple(this.getWaiter()));
        foodOrderDTO.setDesk(this.getDesk().getDeskDTONotFull());

        return foodOrderDTO;
    }


    private List<GuestDTO> getGuestsDTO(Language language) {

        List<GuestDTO> items = new ArrayList<>();

        for (Guest guest :  getGuests()) {
            items.add(guest.getGuestDTO(language));
        }

        return items;
    }



}
