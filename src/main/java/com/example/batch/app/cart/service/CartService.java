package com.example.batch.app.cart.service;

import com.example.batch.app.cart.entity.CartItem;
import com.example.batch.app.cart.repository.CartItemRepository;
import com.example.batch.app.member.entity.Member;
import com.example.batch.app.product.entity.ProductOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;

    public void addItem(Member member, ProductOption option, int quantity) {

        CartItem cartItem = CartItem.builder()
                .member(member)
                .productOption(option)
                .quantity(quantity)
                .build();

        cartItemRepository.save(cartItem);

    }
}
