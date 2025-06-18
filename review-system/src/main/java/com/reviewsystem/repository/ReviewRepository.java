package com.reviewsystem.repository;

import com.reviewsystem.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Check for existing review (prevent duplicate reviews)
    Optional<Review> findByProductIdAndUserId(Long productId, Long userId);
    
    // Calculate average rating
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double findAverageRatingByProductId(@Param("productId") Long productId);
    
    // Count reviews for a product
    @Query("SELECT COUNT(r) FROM Review r WHERE r.product.id = :productId")
    Integer countByProductId(@Param("productId") Long productId);
    
    // Get all reviews for a product with user info (for display)
    @Query("""
        SELECT r, u.username 
        FROM Review r 
        JOIN User u ON r.user.id = u.id 
        WHERE r.product.id = :productId 
        ORDER BY r.createdAt DESC
        """)
    List<Object[]> findReviewsWithUserInfoByProductId(@Param("productId") Long productId);
    
    // Bonus: Get all review texts for tag extraction
    @Query("SELECT r.reviewText FROM Review r WHERE r.product.id = :productId")
    List<String> findAllReviewTextsByProductId(@Param("productId") Long productId);
    
    // Bonus: Get reviews with photos (if you implement photo feature later)
    @Query("SELECT r FROM Review r WHERE r.product.id = :productId AND r.photoUrl IS NOT NULL")
    List<Review> findReviewsWithPhotosByProductId(@Param("productId") Long productId);
    
    // Get top-rated reviews (4-5 stars)
    @Query("""
        SELECT r FROM Review r 
        WHERE r.product.id = :productId 
        AND r.rating >= 4 
        ORDER BY r.rating DESC, r.createdAt DESC
        """)
    List<Review> findTopRatedReviewsByProductId(@Param("productId") Long productId);
    
    // Native SQL example for complex statistics
    @Query(value = """
        SELECT 
            DATE_TRUNC('week', r.created_at) AS week_start,
            COUNT(r.id) AS review_count,
            AVG(r.rating) AS avg_rating
        FROM reviews r
        WHERE r.product_id = :productId
        GROUP BY week_start
        ORDER BY week_start DESC
        LIMIT 8
        """, nativeQuery = true)
    List<Object[]> findWeeklyReviewStats(@Param("productId") Long productId);
}