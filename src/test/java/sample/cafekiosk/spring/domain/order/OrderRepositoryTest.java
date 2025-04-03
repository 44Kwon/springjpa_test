package sample.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@ActiveProfiles("test")
@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("해당일자에 결제완료된 주문들을 가져온다.")
    @Test
    void bringCompleteOrder() {
        //given
        Product product1 = createProduct("001", 4000);
        Product product2 = createProduct("002", 5000);

        productRepository.saveAll(List.of(product1,product2));

        Order order1 = Order.create(List.of(product1, product2), LocalDateTime.of(2025, 2, 13,3,4));
        Order order2 = Order.create(List.of(product1, product2), LocalDateTime.of(2025, 2, 13,7,4));

        order1.changeOrderStatus();
        order2.changeOrderStatus();

        orderRepository.saveAll(List.of(order1,order2));

        //when
        List<Order> completeOrders = orderRepository.findOrdersBy(LocalDateTime.of(2025, 2, 13, 0, 0), LocalDateTime.of(2025, 2, 14, 0, 0), OrderStatus.PAYMENT_COMPLETED);


        //then
        assertThat(completeOrders).hasSize(2);

    }

    private Product createProduct(String productNumber, int price) {
        return Product.builder()
                .type(HANDMADE)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                .build();
    }
}