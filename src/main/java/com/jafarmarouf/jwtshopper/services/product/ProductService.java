package com.jafarmarouf.jwtshopper.services.product;

import com.jafarmarouf.jwtshopper.dtos.ImageDto;
import com.jafarmarouf.jwtshopper.dtos.ProductDto;
import com.jafarmarouf.jwtshopper.exceptions.AlreadyExistsException;
import com.jafarmarouf.jwtshopper.exceptions.ResourceNotFoundException;
import com.jafarmarouf.jwtshopper.models.Category;
import com.jafarmarouf.jwtshopper.models.Image;
import com.jafarmarouf.jwtshopper.models.Product;
import com.jafarmarouf.jwtshopper.repository.CategoryRepository;
import com.jafarmarouf.jwtshopper.repository.ImageRepository;
import com.jafarmarouf.jwtshopper.repository.ProductRepository;
import com.jafarmarouf.jwtshopper.requests.product.AddProductRequest;
import com.jafarmarouf.jwtshopper.requests.product.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    /**
     * @return List<Product>
     */
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * @param id Long
     * @return Product
     */
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    /**
     * @param request AddProductRequest
     * @return Product
     */
    @Override
    public Product addProduct(AddProductRequest request) {
        if (productExistsByNameAndBrand(request.getName(), request.getBrand())) {
            throw new AlreadyExistsException(request.getName() + " " + request.getBrand() + " already exists!, you can update product");
        }

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    private boolean productExistsByNameAndBrand(String name, String brand) {
        return productRepository.existsByNameAndBrand(name, brand);
    }

    /**
     * @param request  AddProductRequest
     * @param category Category
     * @return Product
     */
    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    /**
     * @param request UpdateProductRequest
     * @return Product
     */
    @Override
    public Product updateProduct(UpdateProductRequest request, Long id) {
        return productRepository.findById(id)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    /**
     * @param existingProduct Product
     * @param request         UpdateProductRequest
     * @return Product
     */
    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        if (category == null) {
            throw new ResourceNotFoundException("Category '" + request.getCategory().getName() + "' not found");
        }

        existingProduct.setCategory(category);
        return existingProduct;
    }

    /**
     * @param id Long
     */
    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete, () -> {
                    throw new ResourceNotFoundException("Product not found");
                });
    }

    /**
     * @param category Category
     * @return List<Product>
     */
    @Override
    public List<Product> getProductByCategoryName(String category) {
        return productRepository.findByCategoryName(category);
    }

    /**
     * @param brand String
     * @return List<Product>
     */
    @Override
    public List<Product> getProductByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    /**
     * @param name String
     * @return Product
     */
    @Override
    public List<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }

    /**
     * @param name  String
     * @param brand String
     * @return List<Product>
     */
    @Override
    public List<Product> getProductByNameAndBrand(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    /**
     * @param category Category
     * @param brand    String
     * @return List<Product>
     */
    @Override
    public List<Product> getProductByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }


    /**
     * @param brand String
     * @param name  String
     * @return Long
     */
    @Override
    public Long countProductByNameAndBrand(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    /**
     * @param product Product
     * @return ProductDto
     */
    @Override
    public ProductDto convertToProductDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDto = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDto);
        return productDto;
    }

    /**
     * @param products List<Product>
     * @return List<ProductDto>
     */
    @Override
    public List<ProductDto> convertToProductDtoList(List<Product> products) {
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
    }
}
