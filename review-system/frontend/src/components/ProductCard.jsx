import { useNavigate } from 'react-router-dom';
import '../styles/components.css';

const ProductCard = ({ product }) => {
  const navigate = useNavigate();

  return (
    <div className="product-card" onClick={() => navigate(`/products/${product.id}`)}>
      <div className="product-image">
        <img 
          src={product.imageUrl || '/placeholder-product.png'} 
          alt={product.name} 
          onError={(e) => e.target.src = '/placeholder-product.png'}
        />
      </div>
      <div className="product-info">
        <h3>{product.name}</h3>
        <p className="description">{product.description}</p>
      </div>
    </div>
  );
};

export default ProductCard;