package com.akimatBot.entity.custom;

import com.akimatBot.web.dto.PaymentTypeReportDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTypeReport {
    @ManyToOne
    PaymentType paymentType;
    Double total;
    Long quantity;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public PaymentTypeReport(PaymentType paymentType, Double total, Long quantity) {
        this.paymentType = paymentType;
        this.total = total;
        this.quantity = quantity;
    }

    public PaymentTypeReport(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public PaymentTypeReportDTO getDTO() {
        PaymentTypeReportDTO paymentTypeReportDTO = new PaymentTypeReportDTO();
        paymentTypeReportDTO.setPaymentType(this.paymentType.getPaymentTypeDTO());
        paymentTypeReportDTO.setTotal(total);
        paymentTypeReportDTO.setQuantity(quantity);
        return paymentTypeReportDTO;
    }
}
