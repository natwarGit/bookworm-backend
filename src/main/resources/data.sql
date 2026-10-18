-- Sample Categories
INSERT INTO categories (id, name, slug) VALUES 
('11111111-1111-1111-1111-111111111101', 'Self-help', 'self-help'),
('11111111-1111-1111-1111-111111111102', 'Mystery', 'mystery'),
('11111111-1111-1111-1111-111111111103', 'Romance', 'romance'),
('11111111-1111-1111-1111-111111111104', 'Science Fiction', 'science-fiction'),
('11111111-1111-1111-1111-111111111105', 'Fantasy', 'fantasy'),
('11111111-1111-1111-1111-111111111106', 'Children''s', 'childrens')
ON CONFLICT (id) DO NOTHING;

-- Sample Authors
INSERT INTO authors (id, name, bio) VALUES
('22222222-2222-2222-2222-222222222201', 'Arjun Patel', 'Productivity and mindfulness author'),
('22222222-2222-2222-2222-222222222202', 'Raj Patel', 'Educator and lifelong learning expert'),
('22222222-2222-2222-2222-222222222203', 'James Wright', 'Motivational speaker and author'),
('22222222-2222-2222-2222-222222222204', 'James Adams', 'Mystery and thriller novelist'),
('22222222-2222-2222-2222-222222222205', 'Jessica Martin', 'Contemporary romance writer'),
('22222222-2222-2222-2222-222222222206', 'Laura Mitchell', 'Sci-Fi author'),
('22222222-2222-2222-2222-222222222207', 'Daniel Reed', 'Minimalism and lifestyle expert'),
('22222222-2222-2222-2222-222222222208', 'Clara Nelson', 'Gothic horror and suspense writer'),
('22222222-2222-2222-2222-222222222209', 'Emily Parker', 'Children books author')
ON CONFLICT (id) DO NOTHING;

-- Books from Wireframe UI
INSERT INTO books (id, isbn, title, author_id, category_id, format, language, price, stock_quantity, cover_image_url, is_bestseller, is_new_launch, delivery_lead_days) VALUES
('33333333-3333-3333-3333-333333333301', '978-0111', 'The Art of Focus', '22222222-2222-2222-2222-222222222201', '11111111-1111-1111-1111-111111111101', 'PAPERBACK', 'English', 399.00, 50, 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c', false, false, 3),
('33333333-3333-3333-3333-333333333302', '978-0112', 'The Art of Learning', '22222222-2222-2222-2222-222222222202', '11111111-1111-1111-1111-111111111101', 'PAPERBACK', 'English', 259.00, 45, 'https://images.unsplash.com/photo-1512820790803-83ca734da794', false, false, 3),
('33333333-3333-3333-3333-333333333303', '978-0113', 'The Path to Success', '22222222-2222-2222-2222-222222222203', '11111111-1111-1111-1111-111111111101', 'PAPERBACK', 'English', 359.00, 60, 'https://images.unsplash.com/photo-1497633762265-9d179a990aa6', false, false, 3),
('33333333-3333-3333-3333-333333333304', '978-0114', 'The Midnight Hour', '22222222-2222-2222-2222-222222222204', '11111111-1111-1111-1111-111111111102', 'PAPERBACK', 'English', 299.00, 30, 'https://images.unsplash.com/photo-1516979187457-637abb4f9353', true, false, 3),
('33333333-3333-3333-3333-333333333305', '978-0115', 'Beneath the Stars', '22222222-2222-2222-2222-222222222205', '11111111-1111-1111-1111-111111111103', 'HARDCOVER', 'English', 499.00, 25, 'https://images.unsplash.com/photo-1532012164546-f432f2e37b73', true, false, 3),
('33333333-3333-3333-3333-333333333306', '978-0116', 'The Final Frontier', '22222222-2222-2222-2222-222222222206', '11111111-1111-1111-1111-111111111104', 'PAPERBACK', 'English', 359.00, 40, 'https://images.unsplash.com/photo-1451187580459-43490279c0fa', true, false, 3),
('33333333-3333-3333-3333-333333333307', '978-0117', 'Joy of Minimalism', '22222222-2222-2222-2222-222222222207', '11111111-1111-1111-1111-111111111101', 'PAPERBACK', 'English', 149.00, 100, 'https://images.unsplash.com/photo-1506744038136-46273834b3fb', false, true, 3),
('33333333-3333-3333-3333-333333333308', '978-0118', 'The Vanishing House', '22222222-2222-2222-2222-222222222208', '11111111-1111-1111-1111-111111111102', 'EBOOK', 'English', 99.00, 999, 'https://images.unsplash.com/photo-1509198397868-475647b2a1e5', false, true, 0),
('33333333-3333-3333-3333-333333333309', '978-0119', 'The Lost Kitten', '22222222-2222-2222-2222-222222222209', '11111111-1111-1111-1111-111111111106', 'HARDCOVER', 'English', 339.00, 50, 'https://images.unsplash.com/photo-1543466835-00a7907e9de1', false, true, 3)
ON CONFLICT (id) DO NOTHING;

-- Demo User with 500 Gift Points
-- password_hash = SHA-256("password123") base64-encoded
INSERT INTO users (id, email, password_hash, full_name, role, gift_points_balance) VALUES
('44444444-4444-4444-4444-444444444401', 'customer@bookworm.com', '75K3eLr+dx6JJFuJ7LwIpEpOFmwGZZkRiB84PURz6U8=', 'Demo Customer', 'CUSTOMER', 500)
ON CONFLICT (id) DO NOTHING;

-- Demo Delivery Address for the Demo User
INSERT INTO addresses (id, user_id, label, full_name, address_line1, address_line2, city, state, pincode, phone) VALUES
('55555555-5555-5555-5555-555555555501', '44444444-4444-4444-4444-444444444401', 'Home', 'Demo Customer', '42 Book Lane', 'Near City Library', 'Mumbai', 'Maharashtra', '400001', '9876543210'),
('55555555-5555-5555-5555-555555555502', '44444444-4444-4444-4444-444444444401', 'Work', 'Demo Customer', 'Tech Park, Tower B, Floor 3', NULL, 'Bangalore', 'Karnataka', '560001', '9876543210')
ON CONFLICT (id) DO NOTHING;
