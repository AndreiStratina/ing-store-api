package com.ing.store.service;

import com.ing.store.dto.ProductRequest;
import com.ing.store.dto.ProductResponse;
import com.ing.store.exception.AlreadyExistsException;
import com.ing.store.exception.NotFoundException;
import com.ing.store.model.Product;
import com.ing.store.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.ing.store.fixture.ProductFixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void givenProductRequest_whenAddProduct_thenProductSaved() {
        ProductRequest productRequest = buildProductRequestTv_001();
        Product product = buildProductTv_001();

        when(productRepository.findByProductCode(any())).thenReturn(Optional.empty());
        when(productRepository.save(any())).thenReturn(product);

        ProductResponse response = productService.addProduct(productRequest);

        assertEquals(response.getProductCode(), productRequest.getProductCode());
        assertEquals(response.getDescription(), productRequest.getDescription());
        assertEquals(response.getPrice(), productRequest.getPrice());
        assertEquals(response.getName(), productRequest.getName());
    }

    @Test
    void givenExistingProductRequest_whenAddProduct_thenThrowsAlreadyExistsException() {
        Product product = buildProductTv_001();
        ProductRequest productRequest = buildProductRequestTv_001();
        when(productRepository.findByProductCode(any())).thenReturn(Optional.of(product));

        assertThrows(AlreadyExistsException.class, () -> productService.addProduct(productRequest));
        verify(productRepository, never()).save(any());
    }

    @Test
    void givenNonExistingProductId_whenGetProductById_thenThrowsNotFoundException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.getProductById(99L));
    }

    @Test
    void givenExistingProductId_whenGetProductById_thenReturnProductResponse() {
        Product product = buildProductTv_001();
        ProductRequest productRequest = buildProductRequestTv_001();

        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        ProductResponse response = productService.getProductById(1L);

        assertEquals(response.getProductCode(), productRequest.getProductCode());
        assertEquals(response.getDescription(), productRequest.getDescription());
        assertEquals(response.getPrice(), productRequest.getPrice());
        assertEquals(response.getName(), productRequest.getName());
    }

    @Test
    void givenNewPrice_whenChangePrice_thenPriceChanged() {
        Product product = buildProductTv_001();
        BigDecimal newPrice = new BigDecimal(15.25);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        product.setPrice(newPrice);
        when(productRepository.save(any())).thenReturn(product);

        ProductResponse updated = productService.changePrice(product.getId(), newPrice);

        assertEquals(newPrice, updated.getPrice());
        verify(productRepository).save(product);
    }

    @Test
    void givenNegativeNewPrice_whenChangePrice_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> productService.changePrice(1L, new BigDecimal("-1.00")));
    }

    @Test
    void givenNullNewPrice_whenChangePrice_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> productService.changePrice(1L, null));
    }

    @Test
    void givenMissingProduct_whenChangePrice_thenThrowsNotFoundException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> productService.changePrice(99L, new BigDecimal("10.00")));
        verify(productRepository, never()).save(any());
    }


    @Test
    void givenName_whenGetProductList_thenFindByNameIgnoreCaseIsUsedAndMapped() {
        String name = "tv_asus_XL1412";
        PageRequest pageable = PageRequest.of(1, 10);
        List<Product> productList = buildProductTvList();

        Page<Product> repoPage = new PageImpl<>(productList, pageable, 1);
        when(productRepository.findByNameIgnoreCase(any(), any())).thenReturn(repoPage);

        Page<ProductResponse> result = productService.getProductList(name, 0, 10);

        ProductResponse response1 = result.getContent().get(0);
        assertEquals(productList.get(0).getProductCode(), response1.getProductCode());
        assertEquals(productList.get(0).getName(), response1.getName());
        assertEquals(productList.get(0).getPrice(), response1.getPrice());
        assertEquals(productList.get(0).getDescription(), response1.getDescription());

        ProductResponse response2 = result.getContent().get(1);
        assertEquals(productList.get(1).getProductCode(), response2.getProductCode());
        assertEquals(productList.get(1).getName(), response2.getName());
        assertEquals(productList.get(1).getPrice(), response2.getPrice());
        assertEquals(productList.get(1).getDescription(), response2.getDescription());
    }

    @Test
    void givenNameWithNoMatches_whenGetProductLis_thenEmptyPageAndFindByNameCalled() {
        String name = "tv_asus_XL1412";
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Product> emptyRepoPage = Page.empty(pageable);

        when(productRepository.findByNameIgnoreCase(eq(name), eq(pageable)))
                .thenReturn(emptyRepoPage);

        Page<ProductResponse> result = productService.getProductList(name, 0, 10);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void givenNullName_whenGetProductList_thenEmptyPageAndFindAllCalled() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Product> emptyRepoPage = Page.empty(pageable);

        when(productRepository.findAll(eq(pageable))).thenReturn(emptyRepoPage);

        Page<ProductResponse> result = productService.getProductList(null, 0, 10);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void givenBlankName_whenGetProductList_thenEmptyPageAndFindAllCalled() {
        String blankName = "";
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Product> emptyRepoPage = Page.empty(pageable);

        when(productRepository.findAll(eq(pageable))).thenReturn(emptyRepoPage);

        Page<ProductResponse> result = productService.getProductList(blankName, 0, 10);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
    }
}
