package com.ing.store.converter;

import com.ing.store.dto.ProductResponse;
import com.ing.store.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static com.ing.store.converter.ProductConverter.convertProductToProductResponse;
import static com.ing.store.fixture.ProductFixture.buildProductTv_001;
import static com.ing.store.fixture.ProductFixture.buildProductTv_002;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ProductConverterTest {


    @Test
    void givenProduct_whenConvertProductToProductResponse_thenReturnProductResponse() {
        Product product = buildProductTv_001();

        ProductResponse productResponse = convertProductToProductResponse(product);

        assertEquals(productResponse.getProductCode(), product.getProductCode());
        assertEquals(productResponse.getDescription(), product.getDescription());
        assertEquals(productResponse.getPrice(), product.getPrice());
        assertEquals(productResponse.getName(), product.getName());
        assertEquals(productResponse.getUpdatedAt(), product.getUpdatedAt());
        assertEquals(productResponse.getCreatedAt(), product.getCreatedAt());
    }

    @Test
    void givenProductPage_whenConvertToProductResponsePage_thenContentMappedAndPaginationPreserved() {
        Product p1 = buildProductTv_001();
        Product p2 = buildProductTv_002();

        PageRequest pageable = PageRequest.of(1, 10);
        Page<Product> products = new PageImpl<>(List.of(p1, p2), pageable, 25);

        Page<ProductResponse> responsePage = convertProductToProductResponse(products);

        assertEquals(1, responsePage.getNumber());
        assertEquals(10, responsePage.getSize());
        assertEquals(25, responsePage.getTotalElements());
        assertEquals(2, responsePage.getContent().size());

        ProductResponse r1 = responsePage.getContent().get(0);
        ProductResponse r2 = responsePage.getContent().get(1);

        assertEquals(p1.getProductCode(), r1.getProductCode());
        assertEquals(p1.getName(), r1.getName());
        assertEquals(p1.getDescription(), r1.getDescription());
        assertEquals(p1.getPrice(), r1.getPrice());
        assertEquals(p1.getCreatedAt(), r1.getCreatedAt());
        assertEquals(p1.getUpdatedAt(), r1.getUpdatedAt());

        assertEquals(p2.getProductCode(), r2.getProductCode());
        assertEquals(p2.getName(), r2.getName());
        assertEquals(p2.getDescription(), r2.getDescription());
        assertEquals(p2.getPrice(), r2.getPrice());
        assertEquals(p2.getCreatedAt(), r2.getCreatedAt());
        assertEquals(p2.getUpdatedAt(), r2.getUpdatedAt());
    }
}
