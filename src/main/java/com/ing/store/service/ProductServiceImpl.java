package com.ing.store.service;

import com.ing.store.dto.ProductRequest;
import com.ing.store.dto.ProductResponse;
import com.ing.store.exception.AlreadyExistsException;
import com.ing.store.exception.NotFoundException;
import com.ing.store.model.Product;
import com.ing.store.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.ing.store.converter.ProductConverter.convertProductToProductResponse;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse addProduct(ProductRequest productRequest) {
        log.info("Adding product productCode={}, name={}", productRequest.getProductCode(), productRequest.getName());

        productRepository.findByProductCode(productRequest.getProductCode()).ifPresent(p -> {
            throw new AlreadyExistsException("Product with this productCode already exists: " + productRequest.getProductCode());
        });

        Product product = new Product(productRequest.getProductCode(), productRequest.getName(), productRequest.getDescription(), productRequest.getPrice());

        return convertProductToProductResponse(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        log.info("Getting the product with id = " + id);

        return convertProductToProductResponse(productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found: " + id)));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductList(String name, int page, int size) {
        if (name == null || name.isBlank()) {
            log.info("Getting all the products");
            return convertProductToProductResponse(productRepository.findAll(PageRequest.of(page, size)));
        }
        log.info("Getting all the products with name: " + name);
        return convertProductToProductResponse(productRepository.findByNameIgnoreCase(name, PageRequest.of(page, size)));
    }

    @Transactional
    public ProductResponse changePrice(Long id, BigDecimal newPrice) {
        log.info("Changing price for product id = {} to {}", id, newPrice);

        if (newPrice == null || newPrice.signum() <= 0) {
            throw new IllegalArgumentException("Illegal Price");
        }

        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found: " + id));
        product.setPrice(newPrice);

        return convertProductToProductResponse(productRepository.save(product));
    }
}
