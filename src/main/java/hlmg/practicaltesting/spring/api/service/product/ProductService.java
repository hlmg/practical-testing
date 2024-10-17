package hlmg.practicaltesting.spring.api.service.product;

import hlmg.practicaltesting.spring.api.service.product.response.ProductResponse;
import hlmg.practicaltesting.spring.domain.product.Product;
import hlmg.practicaltesting.spring.domain.product.ProductRepository;
import hlmg.practicaltesting.spring.domain.product.ProductSellingStatus;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}
