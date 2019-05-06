INSERT INTO category (id, name, url) VALUES (1, 'Tablet', '/tablet');
INSERT INTO category (id, name, url) VALUES (2, 'Smartphone', '/smartphone');
INSERT INTO category (id, name, url) VALUES (3, 'TV', '/tv');

INSERT INTO producer (id, name) VALUES (1, 'Apple');
INSERT INTO producer (id, name) VALUES (2, 'Samsung');
INSERT INTO producer (id, name) VALUES (3, 'Sony');
INSERT INTO producer (id, name) VALUES (4, 'DELL');

INSERT INTO product (name, description, image_link, price, category_id, producer_id)
	VALUES ('TabletA', 'TabletA description', '/images/tabletA.jpg', 100.00, 1, 1);
INSERT INTO product (name, description, image_link, price, category_id, producer_id)
	VALUES ('TabletB', 'TabletB description', '/images/tabletB.jpg', 150.00, 1, 2);
INSERT INTO product (name, description, image_link, price, category_id, producer_id)
	VALUES ('SmartphoneA', 'SmartphoneA description', '/images/smartphoneA.jpg', 200.00, 2, 1);
INSERT INTO product (name, description, image_link, price, category_id, producer_id)
	VALUES ('SmartphoneB', 'SmartphoneB description', '/images/smartphoneB.jpg', 200.00, 2, 4);
	
INSERT INTO account (id, name, email) VALUES (1, 'Jack', 'jack@test.com');
INSERT INTO account (id, name, email) VALUES (2, 'Anna', 'anna@test.com');

INSERT INTO "order" (id, account_id, created) VALUES (1, 1, '2019-05-01 05:10:15');
INSERT INTO "order" (id, account_id, created) VALUES (2, 1, '2019-05-01 06:10:15');
INSERT INTO "order" (id, account_id, created) VALUES (3, 1, '2019-05-08 05:10:15');
INSERT INTO "order" (id, account_id, created) VALUES (4, 1, '2019-05-09 07:10:15');
INSERT INTO "order" (id, account_id, created) VALUES (5, 1, '2019-06-01 05:10:15');
INSERT INTO "order" (id, account_id, created) VALUES (6, 1, '2019-06-02 06:10:15');

INSERT INTO order_item (order_id, product_id, quantity) VALUES (1, 1, 1);
INSERT INTO order_item (order_id, product_id, quantity) VALUES (1, 2, 2);