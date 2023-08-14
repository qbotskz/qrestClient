package com.akimatBot.repository.repos;

import com.akimatBot.entity.custom.FoodOrder;
import com.akimatBot.entity.custom.Guest;
import com.akimatBot.entity.custom.PaymentTypeReport;
import com.akimatBot.entity.enums.OrderStatus;
import com.akimatBot.entity.enums.OrderType;
import com.akimatBot.web.dto.PaymentTypeReportDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Repository
@Transactional
public interface OrderRepository extends JpaRepository<FoodOrder, Integer> {

    FoodOrder findOrderById(long id);

    @Query(value = "select fo.order_status from food_order fo where fo.order_status != 2 and fo.desk_id = ?1  ORDER BY ID DESC LIMIT 1",nativeQuery = true)
    OrderStatus getStatusOfOrder(long deskId);

    @Query(value = "select fo.orderStatus from FoodOrder fo where fo.id = ?1")
    OrderStatus getStatusOfOrderByOrderId(long orderId);

//    List<Order> findOrdersByCourierIsNullAndFinishedFalse();
//    List<Order> findOrdersByOperatorIsNotNullAndFinishedIsFalse();

//    FoodOrder findByIdAndDoneIsFalse(long id);
    FoodOrder findById(long id);
//    List<Order> findAllByUser_ChatId(String chatId);

//    @Query("select o from Orders o where (o.createdDate > ?2 or o.paid = true) and o.client.chatId = ?1")
//    List<Orders> getActuallyOrders(long chatId, Date date);

//    default List<Orders> getActuallyOrders(long chatId, int minute){
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.MINUTE, -minute);
//        return getActuallyOrders(chatId, calendar.getTime());
//    }


//    @Query("select o from OrderOfBooks o where DATEDIFF(minute,0,current_timestamp)+15 ")
//    @Query("select current_timestamp + '10 minutes'::interval")

    List<FoodOrder> findAllByCreatedDateBetweenAndOrderStatusOrderById(Date start, Date end, OrderStatus orderStatus);
//    List<FoodOrder> findAllByDoneIsFalseOrderByCreatedDateDesc();
    //    List<OrderOfBooks> findAllByDoneFalseAndTicketReceivedTrueOrderByCreatedDateDesc();
//    List<FoodOrder> findAllByDoneFalseOrderByCreatedDateDesc();
//    List<FoodOrder> findAllByDoneFalseAndPaidTrueOrderByCreatedDateDesc();
//    List<FoodOrder> findAllByDoneFalseAndPaidTrueOrderByCreatedDate();
//    List<FoodOrder> findAllByDoneFalseAndWaiterIsNullOrderByCreatedDate();
    List<FoodOrder> findAllByOrderStatusAndWaiterIsNullOrderByCreatedDate(OrderStatus orderStatus);


//    List<FoodOrder> findAllByPaidIsNullOrderByCreatedDateDesc();
    @Query(value = "select g.food_order_id from Guest g where g.client_id = (select u.id from users u where u.chat_id  = ?1)" +
            "            and (select fo.order_status = 2 from food_order fo where fo.id = g.food_order_id and fo.order_type = ?2 )" +
            "            ORDER BY ID DESC LIMIT 1", nativeQuery = true)
    FoodOrder getLastClientOrder(long chatId, OrderType in_the_restaurant);


    @Query("select guest.foodOrder from Guest guest where guest.client.chatId = ?1 and guest.foodOrder.orderStatus = 4")
    FoodOrder getLastClientOrder2(long chatId, OrderType in_the_restaurant);


//    FoodOrder findLastByClientChatIdAndOrderTypeAndDoneIsFalse(long chatId, OrderType in_the_restaurant);

    //todo asdasdhuasiufhsdeoighawgjhaerkh;
//    default FoodOrder findLastByClientChatIdAndOrderTypeAndDoneIsFalse(long chatId, OrderType in_the_restaurant){
//        return findLimited( chatId, in_the_restaurant, PageRequest.of(0, 10));
//    }

//    List<FoodOrder> findAllByWaiterChatIdAndDoneIsFalse(long chatId);
    List<FoodOrder> findAllByWaiterChatIdAndOrderStatusNot(long chatId, OrderStatus orderStatus);
    List<FoodOrder> findAllByWaiterCodeAndOrderStatusNot(long code, OrderStatus orderStatus);

    ///////////////////////////////////////////////////
    @Query("select fo from FoodOrder fo where fo.waiter.chatId = ?1 and fo.orderStatus = 2 and fo.createdDate >= fo.waiter.currentShift.openingTime")
    List<FoodOrder> getDoneOrdersOfWaiterByChatId(long chatId);
    ///////////////////////////////////////////////////
    List<FoodOrder> findAllByWaiterChatIdAndOrderStatus(long chatId, OrderStatus orderStatus);

    List<FoodOrder> findAllByDeskIdOrderById(long deskId);
//    List<FoodOrder> findAllByDeskIdAndDoneIsFalse(long deskId);
    List<FoodOrder> findAllByDeskIdAndOrderStatus(long deskId, OrderStatus orderStatus);
    FoodOrder findFirstByDeskIdAndOrderStatusNotOrderByIdDesc(long deskId, OrderStatus orderStatus);

    FoodOrder findLastByDeskIdAndOrderTypeAndOrderStatusNot(long deskId, OrderType in_the_restaurant, OrderStatus orderStatus);

//    boolean existsByWaiterChatIdAndDoneIsFalse(long chatId);


    @Query("select count (ord) FROM FoodOrder ord where ord.waiter.code = ?1 and ord.orderStatus <> 3 and ord.orderStatus <> 2")
    long countActiveOrdersByCode(long code);

    @Query("select count (ord) FROM FoodOrder ord where ord.orderStatus <> 3 and ord.orderStatus <> 2")
    long countAllActiveOrders();
//    boolean existsByWaiterCodeAndOrderStatusNot(long code, OrderStatus orderStatus);
    boolean existsByOrderStatusNot(OrderStatus orderStatus);

    @Query("select sum(fo.cheque.calculatedTotal) from FoodOrder fo where fo.waiter.code = ?1 and fo.createdDate > fo.waiter.currentShift.openingTime and fo.orderStatus = 2")
    Double getClosedOrdersCash(Long code);

    @Query("select count (fo.desk.id) from FoodOrder fo where fo.waiter.id = ?1 and fo.orderStatus <> 2 and fo.orderStatus <> 3")
    long getDesksSize(long userId);

    @Query(value = "update food_order set desk_id = ?2 where id = ?1", nativeQuery = true)
    @Modifying
    void changeDesk(long orderId, long toDeskId);

    @Query("select fo.desk.id from FoodOrder fo where fo.id = ?1")
    long getDeskIdOfOrder(long orderId);

    @Query(value = "update food_order set waiter_id = ?2 where id = ?1", nativeQuery = true)
    @Modifying
    void changeWaiter(long orderId, long toWaiterId);

    @Query("select fo.createdDate from FoodOrder fo where fo.id = ?1")
    Date getCreatedDateByOrderId(long orderId);
    @Query("select fo.desk.id from FoodOrder fo where fo.id = ?1")
    long getDeskNumberById(long orderId);

    @Query("select fo.waiter.fullName from FoodOrder fo where fo.id = ?1")
    String getWaiterNameById(long orderId);
    @Query("select fo.desk.hall.name from FoodOrder fo where fo.id = ?1")
    String getHallNameById(long orderId);

    @Query("select count (ord) FROM FoodOrder ord where ord.waiter.chatId = ?1 and ord.orderStatus <> 3 and ord.orderStatus <> 2")
    long countActiveOrdersByChatId(long chatId);

    boolean existsByWaiterChatIdAndOrderStatusNot(long chatId, OrderStatus done);


    @Query("select case when  fo.waiter.code = ?1 then true else false end from  FoodOrder fo where fo.id = ?2")
    boolean hisOrder(Long code, long orderId);
    @Query("select case when  fo.waiter.chatId = ?1 then true else false end from  FoodOrder fo where fo.id = ?2")
    boolean hisOrderByChatId(Long chatId, long orderId);
    @Query("select case when  fo.waiter.code = ?1 then true else false end from  FoodOrder fo where fo.cheque.id = ?2")
    boolean hisOrderByChequeId(Long code, long chequeId);


    @Query("select case when  fo.waiter.chatId = ?1 then true else false end from  FoodOrder fo where fo.cheque.id = ?2")
    boolean hisOrderByChequeIdAndChatId(Long chatId, long chequeId);


    @Query("select case when  guest.foodOrder.waiter.code = ?1 then true else false end from  Guest guest where guest.id = ?2")
    boolean hisOrderByGuestId(Long code, long guestId);


    @Query("select case when  guest.foodOrder.waiter.chatId = ?1 then true else false end from  Guest guest where guest.id = ?2")
    boolean hisOrderByGuestIdAndChatId(Long chatId, long guestId);
    @Query("select case when  item.guest.foodOrder.waiter.code = ?1 then true else false end from  OrderItem item where item.id = ?2")
    boolean hisOrderByOrderItemId(Long code, long orderItemId);


    @Query("select case when  item.guest.foodOrder.waiter.chatId = ?1 then true else false end from  OrderItem item where item.id = ?2")
    boolean hisOrderByOrderItemIdAndChatId(Long chatId, long orderItemId);

    @Query("select case when  count(item.id) = 0 then true else false end from  OrderItem item where item.guest.foodOrder.id = ?1 and item.orderItemStatus <> 3 and item.orderItemStatus <> 0")
    boolean isEmpty(long orderId);


    @Query("select sum(fo.cheque.calculatedTotal) from FoodOrder fo where fo.createdDate between ?1 and ?2 and fo.orderStatus = 2")
    Double getTotalBetween(Date openingTime, Date closingTime);


//    @Query(value = "select new PaymentTypeReport() (sum(p.amount)) AS id, pt as paymentType, (sum(p.amount)) as total from payment_type pt" +
//    @Query(value = "select NEW PaymentTypeReport(pt, (sum(p.amount))) from payment_type pt" +
//            " left join payment p on pt.id = p.payment_type_id" +
//            "    where p.cheque_id in (select fo.cheque_id from food_order fo" +
//            "           where fo.created_date between ?1 and ?2 and fo.order_status = 2)" +
//            "                                        group by pt.id,pt.name", nativeQuery = true)
//    List<PaymentTypeReport> getTotalForPaymentTypes(Date openingTime, Date closingTime);

//    asdasd// осы екы методтын быреуын ыстету ту ду

//    @Query("select new PaymentTypeReport(pt, (sum(p.amount)) ) from PaymentType pt left join Payment p on pt.id = p.paymentType.id " +
//            "where p.cheque.id in " +
//            "(select fo.cheque.id from FoodOrder fo where fo.createdDate between ?1 and ?2 and fo.orderStatus = 2) ")
//    List<PaymentTypeReport> getTotalForPaymentTypesJPQL(Date openingTime, Date closingTime);

    @Query("select new PaymentTypeReport(pt, sum(p.amount), count(p) ) from PaymentType pt left join Payment p on pt.id = p.paymentType.id " +
               " where p.cheque.id in " +
                   "(select fo.cheque.id from FoodOrder fo where fo.createdDate between ?1 and ?2 and fo.orderStatus = 2) group by pt")

    List<PaymentTypeReport> getTotalForPaymentTypesJPQL(Date openingTime, Date closingTime);


    @Query("select count(fo) from FoodOrder fo where fo.createdDate between ?1 and ?2 and fo.orderStatus = 2")
    long getQuantityBetween(Date openingTime, Date closingTime);

    @Query("update FoodOrder g set g.orderStatus = 3 where g.id = ?1")
    @Modifying
    void delete(long id);

    @Query("select guest from Guest guest where guest.client.chatId = ?2 and guest.foodOrder.id = ?1")
    Guest getGuestOfOrder(long id, long chatId);
}
