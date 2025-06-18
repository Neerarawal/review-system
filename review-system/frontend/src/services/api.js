import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

export const fetchProducts = () => axios.get(`${API_BASE_URL}/products`);

export const fetchProductDetails = (productId) => 
  axios.get(`${API_BASE_URL}/reviews/products/${productId}/summary`);

export const submitReview = (reviewData) => 
  axios.post(`${API_BASE_URL}/reviews`, reviewData);