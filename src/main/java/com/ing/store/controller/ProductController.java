package com.ing.store.controller;

import com.ing.store.dto.ProductRequest;
import com.ing.store.dto.ProductResponse;
import com.ing.store.dto.UpdatePriceRequest;
import com.ing.store.dto.common.PageMapper;
import com.ing.store.dto.common.PageResponse;
import com.ing.store.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody ProductRequest productRequest) {
        log.info("Adding a new product: " + productRequest.getName());
        return ResponseEntity.status(HttpStatusCode.valueOf(201))
                .body(productService.addProduct(productRequest));
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        log.info("Getting the product with id: " + id);
        return productService.getProductById(id);
    }

    @GetMapping
    public PageResponse<ProductResponse> productResponseList(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return PageMapper.from(productService.getProductList(name, page, size));
    }

    @PatchMapping("/{id}/price")
    public ProductResponse changePrice(@PathVariable Long id, @Valid @RequestBody UpdatePriceRequest updatePriceRequest) {
        log.info("Changing the price for the product with id: " + id);
        return productService.changePrice(id, updatePriceRequest.getPrice());
    }

}
