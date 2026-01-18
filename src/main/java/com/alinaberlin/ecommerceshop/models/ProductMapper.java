package com.alinaberlin.ecommerceshop.models;

import com.alinaberlin.ecommerceshop.models.dtos.ProductDTO;
import com.alinaberlin.ecommerceshop.models.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    ProductDTO fromEntity(Product product);

    Product toEntity(ProductDTO productDTO);
}
