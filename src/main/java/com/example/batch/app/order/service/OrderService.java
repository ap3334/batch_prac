package com.example.batch.app.order.service;

import com.example.batch.app.cart.entity.CartItem;
import com.example.batch.app.cart.service.CartService;
import com.example.batch.app.member.entity.Member;
import com.example.batch.app.member.service.MemberService;
import com.example.batch.app.order.entity.Order;
import com.example.batch.app.order.entity.OrderItem;
import com.example.batch.app.order.repository.OrderRepository;
import com.example.batch.app.product.entity.ProductOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    private final CartService cartService;

    private final MemberService memberService;


    @Transactional
    public Order createFromCart(Member member) {

        List<CartItem> cartItems = cartService.getItemsByMember(member);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            ProductOption productOption = cartItem.getProductOption();

            if (productOption.isOrderable(cartItem.getQuantity())) {
                orderItems.add(new OrderItem(productOption, cartItem.getQuantity()));
            }

            cartService.deleteItem(cartItem);
        }

        return create(member, orderItems);

    }

    @Transactional
    public Order create(Member member, List<OrderItem> orderItems) {
        Order order = Order
                .builder()
                .member(member)
                .build();

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        orderRepository.save(order);

        return order;
    }

    @Transactional
    public void payByRestCashOnly(Order order) {

        Member orderer = order.getMember();

        long restCash = orderer.getRestCash();

        int payPrice = order.calculatePayPrice();

        if (payPrice > restCash) {
            throw new RuntimeException("예치금이 부족합니다.");
        }


        memberService.addCash(orderer, payPrice * -1, "주문결제__예치금결제");

        order.setPaymentDone();

        orderRepository.save(order);
    }

    @Transactional

    public void refund(Order order) {

        int payPrice = order.getPayPrice();
        memberService.addCash(order.getMember(), payPrice, "주문환불__예치금환불");

        order.setRefundDone();
        orderRepository.save(order);

    }
}
