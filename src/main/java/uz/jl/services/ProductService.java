package uz.jl.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.jl.domains.Product;
import uz.jl.domains.Uploads;
import uz.jl.dto.ProductCreateDto;
import uz.jl.mapper.ProductMapper;
import uz.jl.repository.ProductRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper mapper;
    private final FileStorageService fileStorageService;
    private final ProductRepository productRepository;

    public void create(ProductCreateDto dto, MultipartFile file) {
        Uploads cover = fileStorageService.upload(file);
        Product product = mapper.fromCreateDto(dto);
        product.setCover(cover);
        productRepository.save(product);
    }

    public Page<Product> findAllByPage(Optional<Integer> pageOptional, Optional<Integer> limitOptional, String searchQuery) {
        int page = pageOptional.orElse(0);
        int size = limitOptional.orElse(6);
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return productRepository.findAll(searchQuery,pageable);
    }

    public void delete(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new RuntimeException("Product not found!");
        }

        productRepository.delete(product.get());
    }

    public void update(Long id, String color, String price) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            throw new RuntimeException("Product not found");
        }

        Product product1 = product.get();
        product1.setColor(color);
        product1.setPrice(Double.valueOf(price));

        productRepository.save(product1);
    }
}
