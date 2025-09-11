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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Operations for managing store products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Add a new product", description = "Creates a new product in the store (ADMIN only).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created"),
            @ApiResponse(responseCode = "409", description = "Product with same code already exists"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody ProductRequest productRequest) {
        log.info("Adding a new product: {}", productRequest.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.addProduct(productRequest));
    }


    @Operation(summary = "Get product by ID", description = "Retrieve product details by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        log.info("Getting the product with id: {}", id);
        return productService.getProductById(id);
    }


    @Operation(summary = "List products", description = "List all products, optionally filtered by name.")
    @ApiResponse(responseCode = "200", description = "Products retrieved")
    @GetMapping
    public PageResponse<ProductResponse> productResponseList(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return PageMapper.from(productService.getProductList(name, page, size));
    }

 
    @Operation(summary = "Change product price", description = "Update the price of an existing product (ADMIN only).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Price updated"),
            @ApiResponse(responseCode = "400", description = "Invalid price"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PatchMapping("/{id}/price")
    public ProductResponse changePrice(@PathVariable Long id, @Valid @RequestBody UpdatePriceRequest updatePriceRequest) {
        log.info("Changing the price for the product with id: {}", id);
        return productService.changePrice(id, updatePriceRequest.getPrice());
    }
}
