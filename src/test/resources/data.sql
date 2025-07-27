-- Clean up all relevant data
DELETE FROM purchase_product;
DELETE FROM budget_item;
DELETE FROM budget;
DELETE FROM event;
DELETE FROM event_type;
DELETE FROM solution;
DELETE FROM categories;
DELETE FROM users;

-- Insert users
INSERT INTO users (id, is_active, is_suspended, is_verified, muted, phone_number, token_creation_date, role, activation_token, address, city, company_name, description, email, first_name_admin, last_name_admin, name, password, profile_photo, profile_picture_admin, surname)
VALUES
    (1, true, false, true, false, 0, null, 'ADMIN', 'token-admin', null, 'Novi Sad', null, null, 'admin@gmail.com', 'Admin', 'Admin', null, 'admin', null, null, null),
    (2, true, false, true, false, 0, null, 'ORGANIZER', 'token-organizer', null, 'Belgrade', null, null, 'milicabosancic03@gmail.com', null, null, 'Ana', 'ana123', 'ana.png', null, 'Jovanovic'),
    (9, true, false, true, false, 0, null, 'PROVIDER', null, null, 'Novi Sad', 'VibeX', 'DJ and live band services for weddings and parties.', 'milicabosancic1612@gmail.com', null, null, null, 'securepassword', null, null, null);

-- Insert categories
INSERT INTO categories (id, status, name, description)
VALUES
    (1, 1, 'Decoration', 'Event decoration services'),
    (2, 1, 'Catering', 'Food and beverage services for events'),
    (3, 1, 'Lighting', 'Professional event lighting solutions'),
    (4, 1, 'Music', 'Live bands and DJ services');

-- Insert event types
INSERT INTO event_type (id, is_active, description, name)
VALUES
    (1, true, 'Social gathering', 'Party'),
    (2, true, 'Performing arts', 'Theatre');

-- Insert events
INSERT INTO event (id, event_type_id, organizer_id, privacy_type, start_date, end_date, max_participants, description, image_url, location, name)
VALUES
    (1, 1, 2, 0, '2025-06-02', '2025-06-02', 50, 'Entry with present', 'event1.png', 'Belgrade', 'Birthday Party'),
    (2, 1, 2, 0, '2025-07-25', '2025-07-25', 30, 'For horse lovers, free entry', 'event2.png', 'Novi Sad', 'Horse Riding'),
    (4, 1, 2, 0, '2025-07-25', '2025-07-25', 30, 'Free entry, bring popcorn and drinks!', 'event3.png', 'Novi Sad', 'Rooftop theatre'),
    (7, 1, 2, 0, '2025-02-27', '2025-02-28', 30, 'For horse lovers, free entry', 'event2.png', 'Novi Sad', 'Proba');

-- Insert budgets
INSERT INTO budget (id, event_id)
VALUES
    (1, 1),
    (2, 2);

-- Insert budget items
INSERT INTO budget_item (budget_id, category_id, amount)
VALUES
    (1, 1, 10000),
    (1, 2, 50000);

-- Insert solutions
INSERT INTO solution (id, cancellation_period, category_id, discount, duration, is_available, is_deleted, is_visible, max_duration, min_duration, price, provider_id, reservation_period, reservation_type, status, solution_type, description, image_url, location, name, specificities)
VALUES
    (2, NULL, 2, 15, NULL, TRUE, FALSE, TRUE, NULL, NULL, 15000, 9, NULL, NULL, 1, 'Product', 'For the best movie night', 'service3.png', 'Novi Sad', 'Rooftop theatre equipment', NULL),
    (4, NULL, 3, 5, NULL, TRUE, FALSE, TRUE, NULL, NULL, 20000, 9, NULL, NULL, 1, 'Product', 'Interactive photo booths with custom backdrops and props.', 'service8.png', 'Belgrade', 'Photobooth rentals', NULL),
    (5, 3600000000000, 4, 10, 7200000000000, TRUE, FALSE, TRUE, NULL, NULL, 60000, 9, 14400000000000, 0, 1, 'Service', 'Music lover', 'service6.png', 'Novi Sad', 'Singer', NULL),
    (6, 3600000000000, 4, 10, NULL, TRUE, FALSE, TRUE, 10800000000000, 3600000000000, 80000, 9, 14400000000000, 0, 1, 'Service', 'High-energy band playing modern favourites.', 'service5.png', 'Novi Sad', 'Band', NULL),
    (8, 3600000000000, 4, 10, 7200000000000, TRUE, FALSE, TRUE, NULL, NULL, 30000, 9, 14400000000000, 0, 1, 'Service', 'Burn your social medias', 'service7.png', 'Novi Sad', 'Marketing team', NULL);

-- Insert purchases
INSERT INTO purchase_product (event_id, product_id, purchase_date)
VALUES
    (1, 2, '2025-05-02');

-- RESET IDENTITY SEQUENCES
ALTER TABLE users ALTER COLUMN id RESTART WITH 10;
ALTER TABLE categories ALTER COLUMN id RESTART WITH 5;
ALTER TABLE event_type ALTER COLUMN id RESTART WITH 3;
ALTER TABLE event ALTER COLUMN id RESTART WITH 10;
ALTER TABLE solution ALTER COLUMN id RESTART WITH 10;
ALTER TABLE budget ALTER COLUMN id RESTART WITH 10;
ALTER TABLE budget_item ALTER COLUMN id RESTART WITH 10;