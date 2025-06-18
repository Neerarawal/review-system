import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import RatingSummary from '../components/RatingSummary';
import ReviewForm from '../components/ReviewForm';
import ReviewList from '../components/ReviewList';
import { fetchProductDetails } from '../services/api';
import '../styles/components.css';

const ProductDetailPage = () => {
  const { productId } = useParams();
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [refreshKey, setRefreshKey] = useState(0);

  useEffect(() => {
    const loadProduct = async () => {
      try {
        const { data } = await fetchProductDetails(productId);
        setProduct(data);
      } catch (err) {
        setError('Failed to load product details');
      } finally {
        setLoading(false);
      }
    };
    loadProduct();
  }, [productId, refreshKey]);

  const handleReviewSubmit = () => {
    setRefreshKey(prev => prev + 1);
  };

  if (loading) return <div className="loading">Loading product details...</div>;
  if (error) return <div className="error">{error}</div>;
  if (!product) return <div>Product not found</div>;

  return (
    <div className="product-detail-page">
      <h2>{product.name}</h2>
      
      <RatingSummary 
        averageRating={product.averageRating} 
        reviewCount={product.reviewCount}
        tags={product.tags}
      />
      
      <div className="review-section">
        <ReviewForm 
          productId={productId} 
          onSubmitSuccess={handleReviewSubmit} 
        />
        <ReviewList reviews={product.reviews} />
      </div>
    </div>
  );
};

export default ProductDetailPage;