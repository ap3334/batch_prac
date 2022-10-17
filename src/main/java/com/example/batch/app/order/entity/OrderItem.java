package com.example.batch.app.order.entity;

import com.example.batch.app.base.entity.BaseEntity;
import com.example.batch.app.product.entity.ProductOption;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class OrderItem extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    @ToString.Exclude
    private Order order;

    @ManyToOne(fetch = LAZY)
    private ProductOption productOption;

    private int quantity;

    private int price;

    private int salePrice;

    private int wholesalePrice;

    private int payPrice;

    private int refundPrice;

    private int refundQuantity;

    private int pgFee;

    private boolean isPaid;

    public OrderItem(ProductOption productOption, int quantity) {
        this.productOption = productOption;
        this.quantity = quantity;
        this.price = productOption.getPrice();
        this.salePrice = productOption.getSalePrice();
        this.wholesalePrice = productOption.getWholesalePrice();
    }

    public int calculatePayPrice() {

        return salePrice * quantity;
    }

    public void setPaymentDone() {

        this.pgFee = 0;
        this.payPrice = calculatePayPrice();
        this.isPaid = true;

    }

    public void setRefundDone() {

        if (refundQuantity == quantity) return;

        this.refundQuantity = quantity;
        this.refundPrice = payPrice;
    }
}