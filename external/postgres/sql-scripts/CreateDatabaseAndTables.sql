CREATE TABLE public.category (
  id SERIAL,
  name VARCHAR(60) NOT NULL,
  url VARCHAR(60) NOT NULL,
  product_count INTEGER DEFAULT 0 NOT NULL,
  CONSTRAINT category_pkey PRIMARY KEY(id),
  CONSTRAINT category_url_key UNIQUE(url)
) 
WITH (oids = false);

ALTER TABLE public.category
  ALTER COLUMN id SET STATISTICS 0;

ALTER TABLE public.category
  ALTER COLUMN name SET STATISTICS 0;

ALTER TABLE public.category
  ALTER COLUMN url SET STATISTICS 0;

COMMENT ON COLUMN public.category.product_count
IS 'Consider delete this column and use join to get the value it holds';


ALTER TABLE public.category
  OWNER TO ishop;

  CREATE TABLE public.producer (
  id SERIAL,
  name VARCHAR(60) NOT NULL,
  product_count INTEGER DEFAULT 0 NOT NULL,
  CONSTRAINT producer_pkey PRIMARY KEY(id)
) 
WITH (oids = false);

ALTER TABLE public.producer
  ALTER COLUMN id SET STATISTICS 0;

ALTER TABLE public.producer
  ALTER COLUMN product_count SET STATISTICS 0;


ALTER TABLE public.producer
  OWNER TO ishop;
  
CREATE TABLE public.product (
  id SERIAL,
  name VARCHAR(255) NOT NULL,
  description TEXT NOT NULL,
  image_link VARCHAR(255) NOT NULL,
  price NUMERIC(8,2) NOT NULL,
  category_id INTEGER NOT NULL,
  producer_id INTEGER NOT NULL,
  CONSTRAINT product_pkey PRIMARY KEY(id),
  CONSTRAINT product_category_fk FOREIGN KEY (category_id)
    REFERENCES public.category(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
    NOT DEFERRABLE,
  CONSTRAINT product_producerfk FOREIGN KEY (producer_id)
    REFERENCES public.producer(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
    NOT DEFERRABLE
) 
WITH (oids = false);

ALTER TABLE public.product
  ALTER COLUMN id SET STATISTICS 0;

ALTER TABLE public.product
  ALTER COLUMN name SET STATISTICS 0;

ALTER TABLE public.product
  ALTER COLUMN description SET STATISTICS 0;

ALTER TABLE public.product
  ALTER COLUMN price SET STATISTICS 0;

ALTER TABLE public.product
  ALTER COLUMN category_id SET STATISTICS 0;


ALTER TABLE public.product
  OWNER TO ishop;

 CREATE TABLE public.account (
  id SERIAL,
  name VARCHAR(60) NOT NULL,
  email VARCHAR(100) NOT NULL,
  avatar_url VARCHAR(255),
  CONSTRAINT account_email_key UNIQUE(email),
  CONSTRAINT account_pkey PRIMARY KEY(id)
) 
WITH (oids = false);

ALTER TABLE public.account
  ALTER COLUMN id SET STATISTICS 0;

ALTER TABLE public.account
  ALTER COLUMN name SET STATISTICS 0;

ALTER TABLE public.account
  ALTER COLUMN email SET STATISTICS 0;


ALTER TABLE public.account
  OWNER TO ishop;

 CREATE TABLE public."order" (
  id BIGSERIAL,
  account_id INTEGER NOT NULL,
  created TIMESTAMP(0) WITHOUT TIME ZONE DEFAULT now() NOT NULL,
  CONSTRAINT order_pkey PRIMARY KEY(id),
  CONSTRAINT order_account_fk FOREIGN KEY (account_id)
    REFERENCES public.account(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
    NOT DEFERRABLE
) 
WITH (oids = false);

ALTER TABLE public."order"
  ALTER COLUMN id SET STATISTICS 0;

ALTER TABLE public."order"
  ALTER COLUMN account_id SET STATISTICS 0;

ALTER TABLE public."order"
  ALTER COLUMN created SET STATISTICS 0;


ALTER TABLE public."order"
  OWNER TO ishop;

 CREATE TABLE public.order_item (
  id BIGSERIAL,
  order_id BIGINT NOT NULL,
  product_id INTEGER NOT NULL,
  quantity INTEGER NOT NULL,
  CONSTRAINT order_item_pkey PRIMARY KEY(id),
  CONSTRAINT order_item_order_fk FOREIGN KEY (order_id)
    REFERENCES public."order"(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
    NOT DEFERRABLE,
  CONSTRAINT order_item_product_fk FOREIGN KEY (product_id)
    REFERENCES public.product(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
    NOT DEFERRABLE
) 
WITH (oids = false);

ALTER TABLE public.order_item
  ALTER COLUMN id SET STATISTICS 0;

ALTER TABLE public.order_item
  ALTER COLUMN order_id SET STATISTICS 0;

ALTER TABLE public.order_item
  ALTER COLUMN product_id SET STATISTICS 0;

ALTER TABLE public.order_item
  ALTER COLUMN quantity SET STATISTICS 0;


ALTER TABLE public.order_item
  OWNER TO ishop;
  
-- TRIGGER CREATION STATEMENTS --
CREATE OR REPLACE FUNCTION product_count_update_product()
	RETURNS TRIGGER AS
$$
BEGIN
	IF OLD.category_id != NEW.category_id THEN
    	UPDATE category SET product_count = product_count-1
        	WHERE id = OLD.caregory_id;
        UPDATE category SET product_count = product_count+1
        	WHERE id = NEW.category_id;
    END IF;
    IF OLD.producer_id != NEW.producer_id THEN
    	UPDATE producer SET product_count = product_count-1
        	WHERE id = OLD.producer_id;
        UPDATE producer SET product_count = product_count+1
        	WHERE id = NEW.producer_id;
    END IF;
    RETURN NEW;
END;
$$
LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION product_count_insert_product()
	RETURNS TRIGGER AS
$$
BEGIN
	UPDATE category SET product_count = product_count+1
    	WHERE id = NEW.category_id;
    UPDATE producer SET product_count = product_count+1
    	WHERE id = NEW.producer_id;
    RETURN NEW;
END;
$$
LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION product_count_delete_product()
	RETURNS TRIGGER AS
$$
BEGIN
	UPDATE category SET product_count = product_count-1
    	WHERE id = OLD.category_id;
    UPDATE producer SET product_count = product_count-1
    	WHERE id = OLD.producer_id;
    RETURN OLD;
END;
$$
LANGUAGE 'plpgsql';

CREATE TRIGGER productUpdate
	AFTER UPDATE ON product FOR EACH ROW
    EXECUTE PROCEDURE product_count_update_product();
    
CREATE TRIGGER productInsert
	AFTER INSERT ON product FOR EACH ROW
    EXECUTE PROCEDURE product_count_insert_product();
    
CREATE TRIGGER productDelete
	AFTER DELETE ON product FOR EACH ROW
    EXECUTE PROCEDURE product_count_delete_product();
