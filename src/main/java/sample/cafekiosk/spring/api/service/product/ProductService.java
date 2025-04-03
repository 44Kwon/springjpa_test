package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * readOnly = true : 읽기전용
 * CRUD 에서 CUD 동작 X / only Read
 * JPA : CUD 스냅샷 저장, 변경감지 X (성능 향상)
 *
 * CQRS - Command / Read 를 분리하자
 *
 *
 */
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 동시성 이슈
    // 필드에다 unique 제약조건을 걸고, unique에서 튕겼으면
    // 누가 먼저 선점을 했다고 생각해서 3회이상 재시도해서 통과하게 해서 처리
    // 빈도수가 낮은 경우(크리티컬 하지 않은 경우) 시스템적으로 처리

    // 크리티컬한 경우(빈도수가 많은 경우) => 정책을 다르게 가져간다 UUID 활용(동시성 문제 해결)
    public ProductResponse createProduct(ProductCreateServiceRequest request) {
        // productNumber
        // DB에서 마지막 저장된 Product 상품 번호를 읽어와서 +1

        //nextProductNumber
        String nextProductNumber = createNextProductNumber();

        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }

    private String createNextProductNumber() {
        String latestProductNumber = productRepository.findLatestProductNumber();
        if(latestProductNumber == null) {
            return "001";
        }

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt + 1;

        // 9 -> 009 10 -> 010
        return String.format("%03d", nextProductNumberInt);
    }

    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}
