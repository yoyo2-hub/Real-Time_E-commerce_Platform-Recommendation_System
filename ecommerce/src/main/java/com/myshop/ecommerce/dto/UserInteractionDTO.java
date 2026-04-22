package com.myshop.ecommerce.dto;

import com.myshop.ecommerce.entity.Event;
import com.myshop.ecommerce.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInteractionDTO {
    private Event event;
    private Product product;
}