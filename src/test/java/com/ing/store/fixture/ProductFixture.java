package com.ing.store.fixture;

import com.ing.store.dto.ProductRequest;
import com.ing.store.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductFixture {

    public static Product buildProductTv_001() {
        Product product = new Product();

        product.setId(1L);
        product.setProductCode("tv_001");
        product.setPrice(new BigDecimal(12323.10));
        product.setName("tv_asus_XL1412");
        product.setDescription("the newest TV on the market");

        return product;
    }

    public static Product buildProductTv_002() {
        Product product = new Product();

        product.setId(2L);
        product.setProductCode("tv_002");
        product.setPrice(new BigDecimal(12323.10));
        product.setName("tv_asus_XL1412");
        product.setDescription("the newest TV on the market");

        return product;
    }

    public static List<Product> buildProductTvList() {
        List<Product> productList = new ArrayList<>();

        productList.add(buildProductTv_001());
        productList.add(buildProductTv_002());

        return productList;
    }

    public static ProductRequest buildProductRequestTv_001() {
        ProductRequest productRequest = new ProductRequest();

        productRequest.setProductCode("tv_001");
        productRequest.setPrice(new BigDecimal(12323.10));
        productRequest.setName("tv_asus_XL1412");
        productRequest.setDescription("the newest TV on the market");

        return productRequest;
    }
}
