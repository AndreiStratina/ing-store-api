package com.ing.store.converter;

import com.ing.store.dto.ProductResponse;
import com.ing.store.model.Product;
import org.springframework.data.domain.Page;

public class ProductConverter {

    public static ProductResponse convertProductToProductResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();

        productResponse.setProductCode(product.getProductCode());
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setPrice(product.getPrice());
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());

        return productResponse;
    }

    public static Page<ProductResponse> convertProductToProductResponse(Page<Product> products) {
        return products.map(ProductConverter::convertProductToProductResponse);
    }
}
