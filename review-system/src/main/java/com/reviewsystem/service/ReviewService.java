package com.reviewsystem.service;

import com.reviewsystem.dto.ProductSummaryDto;
import com.reviewsystem.dto.ReviewDto;
import com.reviewsystem.exception.DuplicateReviewException;
import com.reviewsystem.exception.ResourceNotFoundException;
import com.reviewsystem.model.Product;
import com.reviewsystem.model.Review;
import com.reviewsystem.model.User;
import com.reviewsystem.repository.ProductRepository;
import com.reviewsystem.repository.ReviewRepository;
import com.reviewsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    
    private static final List<String> COMMON_TAGS = Arrays.asList(
        "great", "good", "bad", "excellent", "poor", "quality", "fast", "slow"
    );
    
    @Transactional
    public Review createReview(ReviewDto reviewDto) {
        Product product = productRepository.findById(reviewDto.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        
        User user = userRepository.findById(reviewDto.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Check for existing review
        if (reviewRepository.findByProductIdAndUserId(product.getId(), user.getId()).isPresent()) {
            throw new DuplicateReviewException("User has already reviewed this product");
        }
        
        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(reviewDto.getRating());
        review.setReviewText(reviewDto.getReviewText());
        
        return reviewRepository.save(review);
    }
    
    public ProductSummaryDto getProductSummary(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        
        Double averageRating = reviewRepository.findAverageRatingByProductId(productId);
        Integer reviewCount = reviewRepository.countByProductId(productId);
        
        List<ProductSummaryDto.ReviewDetailsDto> reviews = reviewRepository
            .findByProductId(productId)
            .stream()
            .map(review -> {
                ProductSummaryDto.ReviewDetailsDto dto = new ProductSummaryDto.ReviewDetailsDto();
                dto.setId(review.getId());
                dto.setRating(review.getRating());
                dto.setReviewText(review.getReviewText());
                dto.setUsername(review.getUser().getUsername());
                dto.setCreatedAt(review.getCreatedAt().toString());
                return dto;
            })
            .collect(Collectors.toList());
        
        List<String> topTags = extractTopTags(reviews);
        
        ProductSummaryDto summary = new ProductSummaryDto();
        summary.setAverageRating(averageRating != null ? averageRating : 0.0);
        summary.setReviewCount(reviewCount);
        summary.setReviews(reviews);
        summary.setTopTags(topTags);
        
        return summary;
    }
    
    private List<String> extractTopTags(List<ProductSummaryDto.ReviewDetailsDto> reviews) {
        return reviews.stream()
            .flatMap(review -> Arrays.stream(review.getReviewText().toLowerCase().split("\\s+")))
            .filter(COMMON_TAGS::contains)
            .collect(Collectors.groupingBy(word -> word, Collectors.counting()))
            .entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .limit(5)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }
}