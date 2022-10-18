package com.example.batch.app.order.entity;

import com.example.batch.app.base.entity.BaseEntity;
import com.example.batch.app.product.entity.ProductOption;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class RebateOrderItem extends BaseEntity {
    @OneToOne(fetch = LAZY)
    @ToString.Exclude
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private OrderItem orderItem;

    @ManyToOne(fetch = LAZY)
    @ToString.Exclude
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Order order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ProductOption productOption;

    private int quantity;

    private int price;
    private int salePrice;
    private int wholesalePrice;
    private int pgFee;
    private int payPrice;
    private int refundPrice;
    private int refundQuantity;
    private boolean isPaid;

    private LocalDateTime payDate;

    private String productName;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "color", column = @Column(name = "product_option_color")),
            @AttributeOverride(name = "size", column = @Column(name = "product_option_size")),
            @AttributeOverride(name = "displayColor", column = @Column(name = "product_option_display_color")),
            @AttributeOverride(name = "displaySize", column = @Column(name = "product_option_display_size"))
    })
    private RebateOrderItem.EmbProductOption embProductOption;

    private LocalDateTime orderItemCreateDate;

    public RebateOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
        order = orderItem.getOrder();
        productOption = orderItem.getProductOption();
        quantity = orderItem.getQuantity();
        price = orderItem.getPrice();
        salePrice = orderItem.getSalePrice();
        wholesalePrice = orderItem.getWholesalePrice();
        pgFee = orderItem.getPgFee();
        payPrice = orderItem.getPayPrice();
        refundPrice = orderItem.getRefundPrice();
        refundQuantity = orderItem.getRefundQuantity();
        isPaid = orderItem.isPaid();
        payDate = orderItem.getPayDate();

        productName = orderItem.getProductOption().getProduct().getName();

        embProductOption = new EmbProductOption(orderItem.getProductOption());

        orderItemCreateDate = orderItem.getCreateDate();
    }

    @Embeddable
    @NoArgsConstructor
    public static class EmbProductOption {
        private String color;
        private String size;
        private String displayColor;
        private String displaySize;

        public EmbProductOption(ProductOption productOption) {
            color = productOption.getColor();
            size = productOption.getSize();
            displayColor = productOption.getDisplayColor();
            displaySize = productOption.getDisplaySize();
        }
    }

}
