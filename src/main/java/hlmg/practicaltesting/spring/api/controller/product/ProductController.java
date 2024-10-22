package hlmg.practicaltesting.spring.api.controller.product;

import hlmg.practicaltesting.spring.api.controller.product.dto.request.ProductCreateRequest;
import hlmg.practicaltesting.spring.api.service.product.ProductService;
import hlmg.practicaltesting.spring.api.service.product.response.ProductResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProductController {
    private final ProductService productService;

    @PostMapping("/api/v1/products/new")
    public void createProduct(@RequestBody ProductCreateRequest request) {
        productService.createProduct(request);
    }

    @GetMapping("/api/v1/products/celling")
    public List<ProductResponse> getSellingProducts() {
        return productService.getSellingProducts();
    }

}
