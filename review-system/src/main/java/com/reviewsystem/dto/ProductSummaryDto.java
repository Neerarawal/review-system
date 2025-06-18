package com.reviewsystem.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProductSummaryDto {
    private Double averageRating;
    private Integer reviewCount;
    private List<ReviewDetailsDto> reviews;
    private List<String> topTags;
    
    @Data
    public static class ReviewDetailsDto {
        private Long id;
        private Integer rating;
        private String reviewText;
        private String username;
        private String createdAt;
    }
}