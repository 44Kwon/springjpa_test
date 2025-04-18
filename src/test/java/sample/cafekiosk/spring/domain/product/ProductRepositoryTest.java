package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.IntegrationTestSupport;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

/**
 * Repository 테스트
 * Persistence Layer - 데이터베이스 엑세스하는 계층
 * 단위테스트의 성격을 갖고 있다
 */
//@DataJpaTest    //자동으로 rollback이 됨(@Transactinal이 달려있음)
@Transactional
class ProductRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
    @Test
    void findAllBySellingStatusIn() {
        //given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1,product2,product3));

        //when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

        //then
        //갯수 비교뿐만 아니라 추출해서 실제 값 비교까지
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );


    }


    @DisplayName("상품번호 리스트로 상품들을 조회한다.")
    @Test
    void findAllByProductNumberIn() {
        //given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1,product2,product3));

        //when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

        //then
        //갯수 비교뿐만 아니라 추출해서 실제 값 비교까지
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );


    }

    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어온다")
    @Test
    void findLatestProduct() {
        //given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1,product2,product3));

        //when
        String latestProductNumber = productRepository.findLatestProductNumber();

        //then
        //갯수 비교뿐만 아니라 추출해서 실제 값 비교까지
        assertThat(latestProductNumber).isEqualTo("003");
    }

    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어올 떄 상품이 하나도 없는 경우 null을 반환한다.")
    @Test
    void findLatestProductNumberWhenProductIsEmpty() {
        //when
        String latestProductNumber = productRepository.findLatestProductNumber();

        //then
        //갯수 비교뿐만 아니라 추출해서 실제 값 비교까지
        assertThat(latestProductNumber).isNull();
    }

    private Product createProduct(String number, ProductType productType, ProductSellingStatus productSellingStatus, String coffee, int price) {
        Product product = Product.builder()
                .productNumber(number)
                .type(productType)
                .sellingStatus(productSellingStatus)
                .name(coffee)
                .price(price)
                .build();
        return product;
    }
}