package com.jafarmarouf.jwtshopper.services.product;

import com.jafarmarouf.jwtshopper.models.Product;
import com.jafarmarouf.jwtshopper.requests.product.AddProductRequest;
import com.jafarmarouf.jwtshopper.requests.product.UpdateProductRequest;

import java.util.List;

public interface IProductService {

    Product getProductById(Long id);

    Product addProduct(AddProductRequest request);

    Product updateProduct(UpdateProductRequest request, Long id);

    void deleteProductById(Long id);

    List<Product> getAllProducts();

    List<Product> getProductByCategoryName(String category);

    List<Product> getProductByBrand(String brand);

    List<Product> getProductByCategoryAndBrand(String category, String brand);

    List<Product> getProductByName(String name);

    List<Product> getProductByBrandAndName(String brand, String name);

    Long countProductByBrandAndName(String brand, String name);
}
