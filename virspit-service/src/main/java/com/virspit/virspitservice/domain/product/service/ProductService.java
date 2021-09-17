package com.virspit.virspitservice.domain.product.service;

import com.virspit.virspitservice.domain.product.dto.ProductDto;
import com.virspit.virspitservice.domain.product.entity.ProductDoc;
import com.virspit.virspitservice.domain.product.repository.ProductDocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductDocRepository productRepository;

    @Transactional
    public Mono<ProductDto> insert(ProductDto productDto) {
//        return productDto.map(ProductDoc::dtoToEntity)
//                .flatMap(productRepository::insert)
//                .map(ProductDto::entityToDto);
        return productRepository.save(ProductDoc.dtoToEntity(productDto))
                .map(ProductDto::entityToDto);
    }

    public Flux<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .map(ProductDto::entityToDto);
    }

    public Mono<ProductDto> getProduct(final String id) {
        return productRepository.findById(id)
                .map(ProductDto::entityToDto);
    }

    public Flux<ProductDto> getProductsInPriceRange(final int minPrice, final int maxPrice) {
        return productRepository.findByPriceBetween(Range.closed(minPrice, maxPrice))
                .map(ProductDto::entityToDto);
    }

    public Flux<ProductDto> getProductsBy(String search) {
        return productRepository.findByNameLikeOrderByCreatedDateDesc(search)
                .map(ProductDto::entityToDto);
    }

    @Transactional
    public Mono<ProductDto> updateProduct(Mono<ProductDto> productDto, String id) {
        return productRepository.findById(id)
                .flatMap(p -> productDto.map(ProductDoc::dtoToEntity))
                .doOnNext(e -> e.setId(id))
                .flatMap(productRepository::save)
                .map(ProductDto::entityToDto);
    }

    @Transactional
    public Mono<Void> deleteProduct(final String id) {
        return productRepository.deleteById(id);
    }
}
