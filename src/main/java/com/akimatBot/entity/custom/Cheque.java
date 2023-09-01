package com.akimatBot.entity.custom;

import com.akimatBot.repository.TelegramBotRepositoryProvider;
import com.akimatBot.utils.DateUtil;
import com.akimatBot.web.dto.ChequeDTO;
import com.akimatBot.web.dto.PaymentDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
public class Cheque {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Getter
    @Setter
    @OneToOne(mappedBy = "cheque")
    FoodOrder order;

//    @Setter
//    private double total;

//    @Setter
//    @Getter
//    private double prepayment;

    @Getter
    @Setter
    @ManyToOne
    Payment prepayment;

    @Setter
    private double discount;

    @Setter
    private double service;

    @Setter
    private int deliveryPrice;

    @Setter
    @Getter
    private Boolean useCashback;
    @Setter
    @Getter
    private double usedCashback;

    @Setter
    private double cashbackPercentage;
    @Setter
    private double addedCashback;

//    @Setter
//    private double calculatedTotal;

//    @Setter
//    @Getter
//    private Double forPayment;
//
//    @Getter
//    private Double change;




    //    @Getter
    @OneToMany(mappedBy = "cheque", fetch = FetchType.LAZY)
    private List<Payment> payments;

//    public double getCalculatedTotal() {
//        return calculatedTotal;
//    }


    public double getTotal() {
        Double ct = TelegramBotRepositoryProvider.getChequeRepo().getTotal(this.getId());
        if (ct != null){
            return ct;
        }
        return 0.0;
    }

    public void setPrepayment(Payment prepayment) {
        this.prepayment = prepayment;
        if (this.id != 0) {
            calculate();
        }
    }

    public void setDiscount(double discount) {
        this.discount = discount;
        if (this.id != 0) {
            calculate();
        }
    }

    public void setService(double service) {
        this.service = service;
        if (this.id != 0) {
            calculate();
        }
    }

    public List<Payment> getPayments() {
        return TelegramBotRepositoryProvider.getPaymentRepo().findAllByChequeAndPrepaymentFalse(this);
    }


//    public void addTotal(double total){
//        this.total += total;
//    }

    public Cheque() {}

    public ChequeDTO getChequeDTO() {
        this.calculate();

        ChequeDTO chequeDTO = new ChequeDTO();

        chequeDTO.setId(this.id);
        chequeDTO.setTotal(this.getTotal());
        chequeDTO.setDiscount(this.discount);
        chequeDTO.setService(this.service);
        chequeDTO.setDeliveryPrice(this.deliveryPrice);
        chequeDTO.setUsedCashback(this.usedCashback);
        chequeDTO.setCashbackPercentage(this.cashbackPercentage);
        chequeDTO.setAddedCashback(this.addedCashback);
        chequeDTO.setPrepayment(this.getPrepaymentDTO());
        chequeDTO.setCalculatedTotal(this.getCalculatedTotal());
        chequeDTO.setPayments(getPaymentsDTO());
        chequeDTO.setChange(this.getChange());
        chequeDTO.setForPayment(getForPayment());

        return chequeDTO;

    }

    public double getChange() {
        double change = 0.0;

        if (prepayment!= null && this.getCalculatedTotal() < prepayment.getAmount()){
            prepayment.setChange(prepayment.getAmount() - this.getCalculatedTotal());
            TelegramBotRepositoryProvider.getPaymentRepo().save(prepayment);
        }

        if (prepayment != null) {
            change += prepayment.getChange();
        }
        for (Payment payment : getPayments()){
            change += payment.getChange();
        }
        return change;
    }

    private PaymentDTO getPrepaymentDTO(){
        if (this.getPrepayment() != null){
            return this.getPrepayment().getPaymentDTO();
        }
        return null;
    }


    private double getPaymentsTotal() {
        double total = 0.0;
        for (Payment payment : getPayments()) {
            total += payment.getAmount();
        }
        return total;
    }

    private List<PaymentDTO> getPaymentsDTO() {
        List<PaymentDTO> dtos = new ArrayList<>();
        for (Payment payment : getPayments()) {
            dtos.add(payment.getPaymentDTO());
        }
        return dtos;
    }

    public void calculate() {
//        calculateTotal();
//        calculateForPayment();
//        calculateChange();
    }

//    private void calculateChange() {
//        if (this.getPaymentsTotal() + getPrepayment().getAmount() > calculatedTotal){
//            change =  getPaymentsTotal() + getPrepayment().getAmount()  - calculatedTotal;
//        }
//        else change = 0.0;
//    }

    //    private void calculateForPayment() {
//        if (getPaymentsTotal() + getPrepaymentAmount() >  getCalculatedTotal()){
//            forPayment = 0.0;
//        }
//        else forPayment =  this.getCalculatedTotal() - getPaymentsTotal() - getPrepaymentAmount();
//    }
    public double getForPayment() {
        if (getPaymentsTotal() + getPrepaymentAmount() >  getCalculatedTotal()){
            return 0.0;
        }
        else return this.getCalculatedTotal() - getPaymentsTotal() - getPrepaymentAmount();
    }

    private double getPrepaymentAmount() {
        if (this.getPrepayment() != null){
            return getPrepayment().getAmount();
        }
        return 0.0;
    }

    public double getCalculatedTotal() {
        double total = getTotal();

        double calculatedTotal  = total - total*discount/100;
        calculatedTotal += total * service/100;
        calculatedTotal += deliveryPrice;
        calculatedTotal -= usedCashback;

        addedCashback = calculatedTotal * cashbackPercentage/100;

        return calculatedTotal;
    }
//    private void calculateTotal() {
//        calculatedTotal  = total - total*discount/100;
//        calculatedTotal += total * service/100;
//        calculatedTotal += deliveryPrice;
//        calculatedTotal -= usedCashback;
//
//        addedCashback = calculatedTotal * cashbackPercentage/100;
//
//    }

//    public void minusTotal(double value) {
//        total -= value;
//    }

    public void deletePrepayment() {
        TelegramBotRepositoryProvider.getPaymentRepo().delete(this.prepayment);
        this.prepayment = null;
    }
}
