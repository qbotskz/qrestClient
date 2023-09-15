package com.akimatBot.services;

import com.akimatBot.entity.custom.CartItem;
import com.akimatBot.entity.custom.Food;
import com.akimatBot.entity.enums.OrderStatus;
import com.akimatBot.repository.repos.CartItemRepo;
import com.akimatBot.repository.repos.DeskRepo;
import com.akimatBot.repository.repos.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class CartItemService {
    private final CartItemRepo cartItemRepo;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    FoodService foodService;

    @Autowired
    DeskRepo deskRepo;

    @Autowired
    public CartItemService(CartItemRepo cartItemRepo) {
        this.cartItemRepo = cartItemRepo;
    }


    public List<CartItem> findAllCartItemsOfUser(long chatId) {
        return cartItemRepo.findAllByClientChatIdOrderById(chatId);
    }
    public List<CartItem> findAllCartItemsOfTable(long tableId) {
        return cartItemRepo.findAllByDeskIdOrderById(tableId);
    }

    public CartItem findByBookAndTable(long foodId, long tableId){

        return cartItemRepo.findByFoodIdAndDeskId(foodId, tableId);
    }
    public CartItem findByBookAndChatId(Food food, long chatId){

        return cartItemRepo.findByFoodAndClientChatId(food, chatId);
    }

    public CartItem findByIdAndChatId(long id, long chatId){
        return cartItemRepo.findByIdAndClientChatId(id, chatId);
    }


    @Transactional
    public CartItem save(CartItem newCartItem) {
        return cartItemRepo.save(newCartItem);
    }

    private boolean addToCart(CartItem cartItem, Food food, Long chatId) {
        //System.out.println(food.getRemains());
        if (food.getRemains() <= 0) {
            return false;
        } else if (!food.getActivated()) {
            return false;
        }
        cartItem.quantityPlusOne();
        this.save(cartItem);
        food.setRemains(food.getRemains() - 1);
        foodService.save(food);

        if (chatId != null) {
            System.out.println(chatId);
            orderRepository.setStatus(chatId, OrderStatus.NEW);
        }
        return true;
    }

    @Transactional
    public boolean addToCart(Long foodId, long chatId){
        Food food = foodService.findById(foodId);
        CartItem cartItem = this.findByBookAndChatId(food, chatId);

        if (cartItem == null) {
            cartItem = new CartItem(chatId, food, 0);
        }

        return addToCart(cartItem, food, chatId);
    }
    @Transactional
    public boolean addToCartFromWaiter(long foodId, long tableId){
        Food food = foodService.findById(foodId);
        CartItem cartItem = this.findByBookAndTable(foodId, tableId);

        if (cartItem == null){
            cartItem = new CartItem();
            cartItem.setDesk(deskRepo.getOne(tableId));
            cartItem.setQuantity(1);
            cartItem.setFood(foodService.findById(foodId));
        }
        return addToCart(cartItem, food, null);
    }

    @Transactional
    public boolean decreaseCartItemQuantity(long bookId, long chatId){

        try {
            CartItem cartItem;
            cartItem = this.findByBookAndChatId(foodService.findById(bookId), chatId);

            int quantity = cartItem.getQuantity();

            quantity--;
            cartItem.setQuantity(quantity);

            if (quantity <= 0) {
                this.delete(cartItem);
            } else {
                this.save(cartItem);
            }

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }



    @Transactional
    public void delete(CartItem newCartItem) {
        cartItemRepo.delete(newCartItem);
    }

    @Transactional
    public void clearUserCart(long chatId) {
        cartItemRepo.deleteCartItemsByClientChatId(chatId);
    }
    @Transactional
    public void clearDeskCart(long deskId) {
        cartItemRepo.deleteCartItemsByDeskId(deskId);
    }
}
