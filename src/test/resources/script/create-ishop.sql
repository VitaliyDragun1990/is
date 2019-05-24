CREATE TABLE category (
	id INT PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(60) NOT NULL,
	url VARCHAR(60) UNIQUE NOT NULL,
	product_count INT DEFAULT 0 NOT NULL
);

CREATE TABLE producer (
	id INT PRIMARY KEY,
	name VARCHAR(60) NOT NULL,
	product_count INT DEFAULT 0 NOT NULL
);

CREATE TABLE product (
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	description VARCHAR(1024) NOT NULL,
	image_link VARCHAR(255) NOT NULL,
	price NUMERIC(8, 2) NOT NULL,
	category_id INT NOT NULL,
	producer_id INT NOT NULL,
	FOREIGN KEY (category_id) REFERENCES category(id)
		ON DELETE RESTRICT
		ON UPDATE CASCADE,
	FOREIGN KEY (producer_id) REFERENCES producer(id)
		ON DELETE RESTRICT
		ON UPDATE CASCADE
);

CREATE TABLE account (
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(60) NOT NULL,
	email VARCHAR(100) UNIQUE NOT NULL,
	avatar_url VARCHAR(255)
);

CREATE TABLE "order" (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	account_id INT NOT NULL,
	created TIMESTAMP DEFAULT now() NOT NULL,
	FOREIGN KEY (account_id) REFERENCES account(id)
		ON DELETE RESTRICT
		ON UPDATE CASCADE
);

CREATE TABLE order_item (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	order_id BIGINT NOT NULL,
	product_id INT NOT NULL,
	quantity INT NOT NULL,
	FOREIGN KEY (order_id) REFERENCES "order"(id)
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	FOREIGN KEY (product_id) REFERENCES product(id)
		ON DELETE RESTRICT
		ON UPDATE CASCADE
);

CREATE TRIGGER productUpdate
	AFTER UPDATE ON product FOR EACH ROW
	CALL "com.revenat.h2.trigger.ProductCountUpdateProductTrigger";
	
CREATE TRIGGER productInsert
	AFTER INSERT ON product FOR EACH ROW
	CALL "com.revenat.h2.trigger.ProductCountInsertProductTrigger";

CREATE TRIGGER productDelete
	AFTER DELETE ON product FOR EACH ROW
	CALL "com.revenat.h2.trigger.ProductCountDeleteProductTrigger";





