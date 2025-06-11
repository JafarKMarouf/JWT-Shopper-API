package com.jafarmarouf.jwtshopper.controller;

import com.jafarmarouf.jwtshopper.dtos.ProductDto;
import com.jafarmarouf.jwtshopper.exceptions.AlreadyExistsException;
import com.jafarmarouf.jwtshopper.exceptions.ResourceNotFoundException;
import com.jafarmarouf.jwtshopper.models.Product;
import com.jafarmarouf.jwtshopper.requests.product.AddProductRequest;
import com.jafarmarouf.jwtshopper.requests.product.UpdateProductRequest;
import com.jafarmarouf.jwtshopper.response.ApiResponse;
import com.jafarmarouf.jwtshopper.services.product.IProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@AllArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;

    @GetMapping("/all-products")
    public ResponseEntity<ApiResponse> findAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> convertedProducts = productService.convertToProductDtoList(products);
        return ResponseEntity.ok(new ApiResponse("get all products success", convertedProducts));
    }

    @GetMapping("product/{productId}/product")
    public ResponseEntity<ApiResponse> findProductById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            ProductDto convertedProduct = productService.convertToProductDto(product);
            return ResponseEntity.ok(new ApiResponse("get product success", convertedProduct));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> storeProduct(@RequestBody AddProductRequest product) {
        try {
            Product theProduct = productService.addProduct(product);
            ProductDto convertedProduct = productService.convertToProductDto(theProduct);
            return ResponseEntity.ok(new ApiResponse("Add Product success", convertedProduct));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long productId, @RequestBody UpdateProductRequest product) {
        try {
            Product theProduct = productService.updateProduct(product, productId);
            ProductDto convertedProduct = productService.convertToProductDto(theProduct);
            return ResponseEntity.ok(new ApiResponse("Update Product success", convertedProduct));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Delete Product success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/product/{category}/all/products")
    public ResponseEntity<ApiResponse> findProductByCategoryName(@PathVariable(name = "category") String categoryName) {
        try {
            List<Product> products = productService.getProductByCategoryName(categoryName);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found by name of category: " + categoryName, null));
            }
            List<ProductDto> convertedProducts = productService.convertToProductDtoList(products);

            return ResponseEntity.ok(new ApiResponse("get all products success", convertedProducts));

        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/product/by-brand")
    public ResponseEntity<ApiResponse> findProductByBrand(@RequestParam String brand) {
        try {
            List<Product> products = productService.getProductByBrand(brand);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found by brand: " + brand, null));
            }
            List<ProductDto> convertedProducts = productService.convertToProductDtoList(products);

            return ResponseEntity.ok(new ApiResponse("get all products success", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/product/{name}/by-name")
    public ResponseEntity<ApiResponse> findProductByName(@PathVariable String name) {
        try {
            List<Product> products = productService.getProductByName(name);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found by name: " + name, null));
            }
            List<ProductDto> convertedProducts = productService.convertToProductDtoList(products);

            return ResponseEntity.ok(new ApiResponse("get all products success", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/product/by/name-and-brand")
    public ResponseEntity<ApiResponse> findProductByNameAndBrand(@RequestParam String brand, @RequestParam String name) {
        try {
            List<Product> products = productService.getProductByNameAndBrand(brand, name);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found ", null));
            }
            List<ProductDto> convertedProducts = productService.convertToProductDtoList(products);

            return ResponseEntity.ok(new ApiResponse("get all products success", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/product/by/category-and-brand")
    public ResponseEntity<ApiResponse> findProductByCategoryAndBrand(@RequestParam String category, @RequestParam String brand) {
        try {
            List<Product> products = productService.getProductByCategoryAndBrand(category, brand);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found ", null));
            }
            List<ProductDto> convertedProducts = productService.convertToProductDtoList(products);

            return ResponseEntity.ok(new ApiResponse("get all products success", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/count-by/name-and-brand")
    public ResponseEntity<ApiResponse> countProductsByNameAndBrand(@RequestParam String name, @RequestParam String brand) {
        try {
            Long countProduct = productService.countProductByNameAndBrand(name, brand);
            return ResponseEntity.ok(new ApiResponse("Product Count!", countProduct));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

}
