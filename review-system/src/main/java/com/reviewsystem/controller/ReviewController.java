package com.reviewsystem.controller;

import com.reviewsystem.dto.ProductSummaryDto;
import com.reviewsystem.dto.ReviewDto;
import com.reviewsystem.model.Review;
import com.reviewsystem.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    
    @PostMapping
    public ResponseEntity<Review> createReview(@Valid @RequestBody ReviewDto reviewDto) {
        Review review = reviewService.createReview(reviewDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }
    
    @GetMapping("/products/{productId}/summary")
    public ResponseEntity<ProductSummaryDto> getProductSummary(@PathVariable Long productId) {
        ProductSummaryDto summary = reviewService.getProductSummary(productId);
        return ResponseEntity.ok(summary);
    }
}