package com.example.batch.app.base;

import com.example.batch.app.cart.service.CartService;
import com.example.batch.app.member.entity.Member;
import com.example.batch.app.member.service.MemberService;
import com.example.batch.app.order.entity.Order;
import com.example.batch.app.order.service.OrderService;
import com.example.batch.app.product.entity.Product;
import com.example.batch.app.product.entity.ProductOption;
import com.example.batch.app.product.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Configuration
@Profile("dev")
public class DevInitData {

    @Bean
    public CommandLineRunner initData(MemberService memberService, ProductService productService, CartService cartService, OrderService orderService) {
        return args -> {

            String password = "{noop}1234";
            Member member1 = memberService.join("user1", password, "user1@test.com");
            Member member2 = memberService.join("user2", password, "user2@test.com");
            Member member3 = memberService.join("user3", password, "user3@test.com");
            Member member4 = memberService.join("user4", password, "user4@test.com");

            memberService.addCash(member1, 10_000, "충전__무통장입금");
            memberService.addCash(member1, 20_000, "충전__무통장입금");

            memberService.addCash(member1, -5_000, "출금__일반");

            memberService.addCash(member1, 300_000, "충전__무통장입금");

            long restCash = memberService.getRestCash(member1);

            Product product1 = productService.create("단가라 OPS", 68000, 45000, "청평화 A-1-15", Arrays.asList(new ProductOption("RED", "44"), new ProductOption("RED", "55"), new ProductOption("BLUE", "44"), new ProductOption("BLUE", "55")));
            Product product2 = productService.create("쉬폰 OPS", 72000, 55000, "청평화 A-1-15", Arrays.asList(new ProductOption("WHITE", "44"), new ProductOption("WHITE", "55"), new ProductOption("BLACK", "44"), new ProductOption("BLACK", "55")));

            ProductOption product1Option__RED_44 = product1.getProductOptions().get(0);
            ProductOption product1Option__BLUE_44 = product1.getProductOptions().get(2);

            cartService.addItem(member1, product1Option__RED_44, 1);
            cartService.addItem(member1, product1Option__RED_44, 2);
            cartService.addItem(member1, product1Option__BLUE_44, 1);

            Order order1 = orderService.createFromCart(member1);

            int order1PayPrice = order1.calculatePayPrice();

            orderService.payByRestCashOnly(order1);

            ProductOption product2Option__BLACK_44 = product1.getProductOptions().get(0);
            ProductOption product2Option__WHITE_44 = product1.getProductOptions().get(2);

            cartService.addItem(member2, product1Option__RED_44, 1);
            cartService.addItem(member2, product2Option__BLACK_44, 2);
            cartService.addItem(member2, product2Option__WHITE_44, 2);

            Order order2 = orderService.createFromCart(member2);

            memberService.addCash(member2, 370_000, "충전__무통장입금");

            orderService.payByRestCashOnly(order2);


        };
    }

}
