-- Sample products
INSERT INTO products (name, description) VALUES 
('Wireless Earbuds Pro', 'Noise-cancelling with 30hr battery'),
('Smart Fitness Watch', 'Heart rate monitor and GPS tracking'),
('Bluetooth Speaker X2', '360° sound with waterproof design');

-- Sample users
INSERT INTO users (username, email) VALUES
('reviewer1', 'user1@example.com'),
('reviewer2', 'user2@example.com'),
('reviewer3', 'user3@example.com');

-- Sample reviews
INSERT INTO reviews (product_id, user_id, rating, review_text) VALUES
(1, 1, 5, 'Excellent sound quality!'),
(1, 2, 4, 'Great but battery could last longer'),
(2, 3, 3, 'Accurate tracking but bulky design');