-- SQL Manager Lite for PostgreSQL 5.9.4.51539
-- ---------------------------------------
-- Host      : localhost
-- Database  : ishop
-- Version   : PostgreSQL 9.6.1, compiled by Visual C++ build 1800, 64-bit



SET search_path = public, pg_catalog;
DROP TRIGGER IF EXISTS productdelete ON public.product;
DROP TRIGGER IF EXISTS productinsert ON public.product;
DROP TRIGGER IF EXISTS productupdate ON public.product;
DROP FUNCTION IF EXISTS public.product_count_delete_product ();
DROP FUNCTION IF EXISTS public.product_count_insert_product ();
DROP FUNCTION IF EXISTS public.product_count_update_product ();
DROP TABLE IF EXISTS public.order_item;
DROP TABLE IF EXISTS public."order";
DROP TABLE IF EXISTS public.account;
DROP TABLE IF EXISTS public.product;
DROP TABLE IF EXISTS public.producer;
DROP TABLE IF EXISTS public.category;
SET check_function_bodies = false;
--
-- Definition for function product_count_update_product (OID = 42253) : 
--
CREATE FUNCTION public.product_count_update_product (
)
RETURNS trigger
AS 
$body$
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
$body$
LANGUAGE plpgsql;
--
-- Definition for function product_count_insert_product (OID = 42254) : 
--
CREATE FUNCTION public.product_count_insert_product (
)
RETURNS trigger
AS 
$body$
BEGIN
	UPDATE category SET product_count = product_count+1
    	WHERE id = NEW.category_id;
    UPDATE producer SET product_count = product_count+1
    	WHERE id = NEW.producer_id;
    RETURN NEW;
END;
$body$
LANGUAGE plpgsql;
--
-- Definition for function product_count_delete_product (OID = 42255) : 
--
CREATE FUNCTION public.product_count_delete_product (
)
RETURNS trigger
AS 
$body$
BEGIN
	UPDATE category SET product_count = product_count-1
    	WHERE id = OLD.category_id;
    UPDATE producer SET product_count = product_count-1
    	WHERE id = OLD.producer_id;
    RETURN OLD;
END;
$body$
LANGUAGE plpgsql;
--
-- Structure for table category (OID = 42172) : 
--
CREATE TABLE public.category (
    id serial NOT NULL,
    name varchar(60) NOT NULL,
    url varchar(60) NOT NULL,
    product_count integer DEFAULT 0 NOT NULL
)
WITH (oids = false);
ALTER TABLE ONLY public.category ALTER COLUMN id SET STATISTICS 0;
ALTER TABLE ONLY public.category ALTER COLUMN name SET STATISTICS 0;
ALTER TABLE ONLY public.category ALTER COLUMN url SET STATISTICS 0;
--
-- Structure for table producer (OID = 42183) : 
--
CREATE TABLE public.producer (
    id serial NOT NULL,
    name varchar(60) NOT NULL,
    product_count integer DEFAULT 0 NOT NULL
)
WITH (oids = false);
ALTER TABLE ONLY public.producer ALTER COLUMN id SET STATISTICS 0;
ALTER TABLE ONLY public.producer ALTER COLUMN product_count SET STATISTICS 0;
--
-- Structure for table product (OID = 42192) : 
--
CREATE TABLE public.product (
    id serial NOT NULL,
    name varchar(255) NOT NULL,
    description text NOT NULL,
    image_link varchar(255) NOT NULL,
    price numeric(8,2) NOT NULL,
    category_id integer NOT NULL,
    producer_id integer NOT NULL
)
WITH (oids = false);
ALTER TABLE ONLY public.product ALTER COLUMN id SET STATISTICS 0;
ALTER TABLE ONLY public.product ALTER COLUMN name SET STATISTICS 0;
ALTER TABLE ONLY public.product ALTER COLUMN description SET STATISTICS 0;
ALTER TABLE ONLY public.product ALTER COLUMN price SET STATISTICS 0;
ALTER TABLE ONLY public.product ALTER COLUMN category_id SET STATISTICS 0;
--
-- Structure for table account (OID = 42213) : 
--
CREATE TABLE public.account (
    id serial NOT NULL,
    name varchar(60) NOT NULL,
    email varchar(100) NOT NULL
)
WITH (oids = false);
ALTER TABLE ONLY public.account ALTER COLUMN id SET STATISTICS 0;
ALTER TABLE ONLY public.account ALTER COLUMN name SET STATISTICS 0;
ALTER TABLE ONLY public.account ALTER COLUMN email SET STATISTICS 0;
--
-- Structure for table order (OID = 42223) : 
--
CREATE TABLE public."order" (
    id bigserial NOT NULL,
    account_id integer NOT NULL,
    created timestamp(0) without time zone DEFAULT now() NOT NULL
)
WITH (oids = false);
ALTER TABLE ONLY public."order" ALTER COLUMN id SET STATISTICS 0;
ALTER TABLE ONLY public."order" ALTER COLUMN account_id SET STATISTICS 0;
ALTER TABLE ONLY public."order" ALTER COLUMN created SET STATISTICS 0;
--
-- Structure for table order_item (OID = 42237) : 
--
CREATE TABLE public.order_item (
    id bigserial NOT NULL,
    order_id bigint NOT NULL,
    product_id integer NOT NULL,
    quantity integer NOT NULL
)
WITH (oids = false);
ALTER TABLE ONLY public.order_item ALTER COLUMN id SET STATISTICS 0;
ALTER TABLE ONLY public.order_item ALTER COLUMN order_id SET STATISTICS 0;
ALTER TABLE ONLY public.order_item ALTER COLUMN product_id SET STATISTICS 0;
ALTER TABLE ONLY public.order_item ALTER COLUMN quantity SET STATISTICS 0;
--
-- Definition for index category_pkey (OID = 42177) : 
--
ALTER TABLE ONLY category
    ADD CONSTRAINT category_pkey
    PRIMARY KEY (id);
--
-- Definition for index category_url_key (OID = 42179) : 
--
ALTER TABLE ONLY category
    ADD CONSTRAINT category_url_key
    UNIQUE (url);
--
-- Definition for index producer_pkey (OID = 42188) : 
--
ALTER TABLE ONLY producer
    ADD CONSTRAINT producer_pkey
    PRIMARY KEY (id);
--
-- Definition for index product_pkey (OID = 42199) : 
--
ALTER TABLE ONLY product
    ADD CONSTRAINT product_pkey
    PRIMARY KEY (id);
--
-- Definition for index product_category_fk (OID = 42201) : 
--
ALTER TABLE ONLY product
    ADD CONSTRAINT product_category_fk
    FOREIGN KEY (category_id) REFERENCES category(id) ON UPDATE CASCADE ON DELETE RESTRICT;
--
-- Definition for index product_producerfk (OID = 42206) : 
--
ALTER TABLE ONLY product
    ADD CONSTRAINT product_producerfk
    FOREIGN KEY (producer_id) REFERENCES producer(id) ON UPDATE CASCADE ON DELETE RESTRICT;
--
-- Definition for index account_pkey (OID = 42217) : 
--
ALTER TABLE ONLY account
    ADD CONSTRAINT account_pkey
    PRIMARY KEY (id);
--
-- Definition for index account_email_key (OID = 42219) : 
--
ALTER TABLE ONLY account
    ADD CONSTRAINT account_email_key
    UNIQUE (email);
--
-- Definition for index order_pkey (OID = 42228) : 
--
ALTER TABLE ONLY "order"
    ADD CONSTRAINT order_pkey
    PRIMARY KEY (id);
--
-- Definition for index order_account_fk (OID = 42230) : 
--
ALTER TABLE ONLY "order"
    ADD CONSTRAINT order_account_fk
    FOREIGN KEY (account_id) REFERENCES account(id) ON UPDATE CASCADE ON DELETE RESTRICT;
--
-- Definition for index order_item_pkey (OID = 42241) : 
--
ALTER TABLE ONLY order_item
    ADD CONSTRAINT order_item_pkey
    PRIMARY KEY (id);
--
-- Definition for index order_item_order_fk (OID = 42243) : 
--
ALTER TABLE ONLY order_item
    ADD CONSTRAINT order_item_order_fk
    FOREIGN KEY (order_id) REFERENCES "order"(id) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Definition for index order_item_product_fk (OID = 42248) : 
--
ALTER TABLE ONLY order_item
    ADD CONSTRAINT order_item_product_fk
    FOREIGN KEY (product_id) REFERENCES product(id) ON UPDATE CASCADE ON DELETE RESTRICT;
--
-- Definition for trigger productupdate (OID = 42256) : 
--
CREATE TRIGGER productupdate
    AFTER UPDATE ON product
    FOR EACH ROW
    EXECUTE PROCEDURE product_count_update_product ();
--
-- Definition for trigger productinsert (OID = 42257) : 
--
CREATE TRIGGER productinsert
    AFTER INSERT ON product
    FOR EACH ROW
    EXECUTE PROCEDURE product_count_insert_product ();
--
-- Definition for trigger productdelete (OID = 42258) : 
--
CREATE TRIGGER productdelete
    AFTER DELETE ON product
    FOR EACH ROW
    EXECUTE PROCEDURE product_count_delete_product ();
--
-- Comments
--
COMMENT ON SCHEMA public IS 'standard public schema';
COMMENT ON COLUMN public.category.product_count IS 'Consider delete this column and use join to get the value it holds';
