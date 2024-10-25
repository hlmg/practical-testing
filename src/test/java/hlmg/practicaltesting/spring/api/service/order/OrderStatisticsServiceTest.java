package hlmg.practicaltesting.spring.api.service.order;

import hlmg.practicaltesting.spring.client.mail.MailSendClient;
import hlmg.practicaltesting.spring.domain.history.mail.MailSendHistory;
import hlmg.practicaltesting.spring.domain.history.mail.MailSendHistoryRepository;
import hlmg.practicaltesting.spring.domain.order.Order;
import hlmg.practicaltesting.spring.domain.order.OrderRepository;
import hlmg.practicaltesting.spring.domain.order.OrderStatus;
import hlmg.practicaltesting.spring.domain.orderproduct.OrderProductRepository;
import hlmg.practicaltesting.spring.domain.product.Product;
import hlmg.practicaltesting.spring.domain.product.ProductRepository;
import hlmg.practicaltesting.spring.domain.product.ProductType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static hlmg.practicaltesting.spring.domain.product.ProductSellingStatus.SELLING;
import static hlmg.practicaltesting.spring.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
class OrderStatisticsServiceTest {

    @Autowired
    private OrderStatisticsService orderStatisticsService;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MailSendHistoryRepository mailSendHistoryRepository;

    @MockBean
    private MailSendClient mailSendClient;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        mailSendHistoryRepository.deleteAllInBatch();
    }

    @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
    @Test
    void sendOrderStatisticsMail() {
        // given
        LocalDateTime now = LocalDateTime.of(2024, 10, 25, 0, 0);

        Product product1 = createProduct(HANDMADE, "001", 1000);
        Product product2 = createProduct(HANDMADE, "002", 2000);
        Product product3 = createProduct(HANDMADE, "003", 3000);
        List<Product> products = getProduct1(product1, product2, product3);
        productRepository.saveAll(products);

        createPaymentCompletedOrder(LocalDateTime.of(2024, 10, 24, 23, 59, 59), products);
        createPaymentCompletedOrder(now, products);
        createPaymentCompletedOrder(LocalDateTime.of(2024, 10, 25, 23, 59, 59), products);
        createPaymentCompletedOrder(LocalDateTime.of(2024, 10, 26, 0, 0), products);

        when(mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class), any(String.class)))
                .thenReturn(true);

        // when
        boolean result = orderStatisticsService.sendOrderStatisticsMail(now.toLocalDate(), "no-reply@test.com");

        // then
        assertThat(result).isTrue();

        List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
        assertThat(histories).hasSize(1)
                .extracting("content")
                .contains("총 매출 합계는 12000원입니다.");

    }

    private Order createPaymentCompletedOrder(LocalDateTime now, List<Product> products) {
        Order order = Order.builder()
                .products(products)
                .orderStatus(OrderStatus.PAYMENT_COMPLETE)
                .registeredDateTime(now)
                .build();
        return orderRepository.save(order);
    }

    private List<Product> getProduct1(Product product1, Product product2, Product product3) {
        return List.of(product1, product2, product3);
    }

    private Product createProduct(ProductType type, String productNumber, int price) {
        return Product.builder()
                .type(type)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                .build();
    }
}
