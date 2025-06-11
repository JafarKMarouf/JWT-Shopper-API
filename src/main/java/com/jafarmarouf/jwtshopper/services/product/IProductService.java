package com.jafarmarouf.jwtshopper.services.product;

import com.jafarmarouf.jwtshopper.dtos.ProductDto;
import com.jafarmarouf.jwtshopper.models.Product;
import com.jafarmarouf.jwtshopper.requests.product.AddProductRequest;
import com.jafarmarouf.jwtshopper.requests.product.UpdateProductRequest;

import java.util.List;

public interface IProductService {

    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product addProduct(AddProductRequest request);

    Product updateProduct(UpdateProductRequest request, Long id);

    void deleteProductById(Long id);

    List<Product> getProductByCategoryName(String category);

    List<Product> getProductByBrand(String brand);

    List<Product> getProductByName(String name);

    List<Product> getProductByNameAndBrand(String brand, String name);

    List<Product> getProductByCategoryAndBrand(String category, String brand);

    Long countProductByNameAndBrand(String brand, String name);

    ProductDto convertToProductDto(Product product);

    List<ProductDto> convertToProductDtoList(List<Product> products);
}
