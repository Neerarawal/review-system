import { useState } from 'react';
import { submitReview } from '../services/api';
import '../styles/components.css';

const ReviewForm = ({ productId, onSubmitSuccess }) => {
  const [rating, setRating] = useState(0);
  const [reviewText, setReviewText] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await submitReview({ productId, rating, reviewText });
      onSubmitSuccess();
      setRating(0);
      setReviewText('');
    } catch (err) {
      setError(err.message || 'Failed to submit review');
    }
  };

  return (
    <form className="review-form" onSubmit={handleSubmit}>
      <h3>Add Your Review</h3>
      {error && <div className="error-message">{error}</div>}
      <div className="form-group">
        <label>Rating (1-5):</label>
        <input
          type="number"
          min="1"
          max="5"
          value={rating}
          onChange={(e) => setRating(parseInt(e.target.value))}
          required
        />
      </div>
      <div className="form-group">
        <label>Review:</label>
        <textarea
          value={reviewText}
          onChange={(e) => setReviewText(e.target.value)}
          rows="4"
          required
        />
      </div>
      <button type="submit">Submit Review</button>
    </form>
  );
};

export default ReviewForm;