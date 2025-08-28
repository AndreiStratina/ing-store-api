package com.ing.store.service;

import com.ing.store.dto.ProductRequest;
import com.ing.store.dto.ProductResponse;
import com.ing.store.exception.AlreadyExistsException;
import com.ing.store.exception.NotFoundException;
import com.ing.store.model.Product;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

/**
 * Service contract for managing {@link Product} entities.
 * <p>
 * Provides operations to create, read, list and update products in the store.
 */
public interface ProductService {

    /**
     * Adds a new product to the store.
     * <p>
     * Validates that the {@code productCode} is unique.
     *
     * @param req DTO containing product data (code, name, description, price)
     * @return the persisted {@link ProductResponse} entity with generated ID
     * @throws AlreadyExistsException
     *         if a product with the same {@code productCode} already exists
     */
    ProductResponse addProduct(ProductRequest req);

    /**
     * Retrieves a product by its ID.
     *
     * @param id the product's database ID
     * @return the {@link ProductResponse} if found
     * @throws NotFoundException
     *         if no product exists with the given ID
     */
    ProductResponse getProductById(Long id);

    /**
     * Returns a paginated list of products.
     * <p>
     * If {@code q} is not null/blank, performs a case-insensitive search
     * by product name.
     *
     * @param name    optional search term (product name, partial match)
     * @param page page number (0-based)
     * @param size page size (number of items per page)
     * @return a {@link Page} of {@link ProductResponse} objects
     */
    Page<ProductResponse> getProductList(String name, int page, int size);

    /**
     * Updates the price of an existing product.
     *
     * @param id       the ID of the product to update
     * @param newPrice the new price (must be positive)
     * @return the updated {@link ProductResponse}
     * @throws NotFoundException
     *         if the product does not exist
     * @throws IllegalArgumentException
     *         if {@code newPrice} is null or not positive
     */
    ProductResponse changePrice(Long id, BigDecimal newPrice);
}