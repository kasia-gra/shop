DROP TYPE IF EXISTS status CASCADE;
CREATE TYPE status AS ENUM ('paid', 'checked', 'confirmed', 'shipped');


DROP TABLE IF EXISTS public.order;
CREATE TABLE public.order
(
    id                       serial NOT NULL PRIMARY KEY,
    cart_id                  int    NOT NULL,
    user_id                  int,
    order_address_details_id int    NOT NULL,
    date                     timestamp,
    current_status           status,
    session_id               int NOT NULL
);


DROP TABLE IF EXISTS public.cart CASCADE;
CREATE TABLE public.cart
(
    id          serial NOT NULL PRIMARY KEY,
    total_price int    NOT NULL,
    cart_size   int    NOT NULL,
    CONSTRAINT cart_id UNIQUE (id)
);


DROP TABLE IF EXISTS public.line_item;
CREATE TABLE public.line_item
(
    id               serial NOT NULL PRIMARY KEY,
    cart_id          int    NOT NULL,
    product_id       int    NOT NULL,
    quantity         int    NOT NULL,
    total_line_price int    NOT NULL,
    CONSTRAINT product_per_cart UNIQUE (cart_id, product_id)
);


DROP TABLE IF EXISTS public.product;
CREATE TABLE public.product
(
    id            serial NOT NULL PRIMARY KEY,
    name          text   NOT NULL,
    default_price float  NOT NULL,
    description   text   NOT NULL,
    picture_name  text   NOT NULL,
    category_id   int    NOT NULL,
    supplier_id   int    NOT NULL
);


DROP TABLE IF EXISTS public.category;
CREATE TABLE public.category
(
    id          serial NOT NULL PRIMARY KEY,
    name        text   NOT NULL,
    department  text   NOT NULL,
    description text   NOT NULL
);


DROP TABLE IF EXISTS public.supplier;
CREATE TABLE public.supplier
(
    id          serial NOT NULL PRIMARY KEY,
    name        text   NOT NULL,
    description text   NOT NULL
);


DROP TABLE IF EXISTS public.user;
CREATE TABLE public.user
(
    id                  serial NOT NULL PRIMARY KEY,
    first_name          text   NOT NULL,
    last_name           text   NOT NULL,
    email               text   NOT NULL,
    password            text   NOT NULL,
    phone_number        text,
    registration_date   timestamp,
    user_address_details_id int    NOT NULL
);


DROP TABLE IF EXISTS public.address_detail;
CREATE TABLE public.address_detail
(
    id                        serial NOT NULL PRIMARY KEY,
    billing_address_id  int    NOT NULL,
    shipping_address_id int    NOT NULL
);


DROP TABLE IF EXISTS public.address;
CREATE TABLE public.address
(
    id       serial NOT NULL PRIMARY KEY,
    country  text   NOT NULL,
    city     text   NOT NULL,
    zip_code text   NOT NULL,
    address  text   NOT NULL
);

DROP TABLE IF EXISTS public.session;
CREATE TABLE public.session
(
    id                       serial NOT NULL PRIMARY KEY
);

ALTER TABLE ONLY "order"
    ADD CONSTRAINT "cart_id" FOREIGN KEY (cart_id)
        REFERENCES cart (id) ON DELETE CASCADE;

ALTER TABLE ONLY "order"
    ADD CONSTRAINT "user_id" FOREIGN KEY (user_id)
        REFERENCES "user" (id) ON DELETE CASCADE;

ALTER TABLE ONLY "order"
    ADD CONSTRAINT "order_address_details_id" FOREIGN KEY (order_address_details_id)
        REFERENCES "address_detail" (id) ON DELETE CASCADE;

ALTER TABLE ONLY "order"
    ADD CONSTRAINT "session_id" FOREIGN KEY (session_id)
        REFERENCES "session" (id) ON DELETE CASCADE;

ALTER TABLE ONLY "line_item"
    ADD CONSTRAINT "cart_id" FOREIGN KEY (cart_id)
        REFERENCES "cart" (id) ON DELETE CASCADE;

ALTER TABLE ONLY "line_item"
    ADD CONSTRAINT "product_id" FOREIGN KEY (product_id)
        REFERENCES "product" (id) ON DELETE CASCADE;

ALTER TABLE ONLY "product"
    ADD CONSTRAINT "category_id" FOREIGN KEY (category_id)
        REFERENCES "category" (id) ON DELETE CASCADE;

ALTER TABLE ONLY "product"
    ADD CONSTRAINT "supplier_id" FOREIGN KEY (supplier_id)
        REFERENCES "supplier" (id) ON DELETE CASCADE;

ALTER TABLE ONLY "address_detail"
    ADD CONSTRAINT "billing_address_detail_id" FOREIGN KEY (billing_address_id)
        REFERENCES "address" (id) ON DELETE CASCADE;

ALTER TABLE ONLY "address_detail"
    ADD CONSTRAINT "shipping_address_detail_id" FOREIGN KEY (shipping_address_id)
        REFERENCES "address" (id) ON DELETE CASCADE;

ALTER TABLE ONLY "user"
    ADD CONSTRAINT "user_address_details_id" FOREIGN KEY (user_address_details_id)
        REFERENCES "address_detail" (id) ON DELETE CASCADE;


INSERT INTO public.supplier (id, name, description)
    VALUES (1, 'Amazon', 'Digital content and services');
INSERT INTO public.supplier (id, name, description)
    VALUES (2, 'Lenovo', 'Computers');
INSERT INTO public.supplier (id, name, description)
    VALUES (3, 'Nokia', 'Smartphones and stuff');
INSERT INTO public.supplier (id, name, description)
    VALUES (4, 'Samsung', 'Just stuff');
INSERT INTO public.supplier (id, name, description)
    VALUES (6, 'Lego', 'Bricks');

INSERT INTO public.category (id, name, department, description)
    VALUES (1, 'Tablets', 'Hardware', 'A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.');
INSERT INTO public.category (id, name, department, description)
    VALUES (2, 'Smartphones', 'Hardware', 'Smartphone, Everyone knows what is it.');
INSERT INTO public.category (id, name, department, description)
    VALUES (3, 'Lego Bricks', 'Toys', 'Plastic construction toys that are manufactured by The Lego Group, a privately held company based in Billund, Denmark.');

INSERT INTO public.product (id, name, default_price, description, picture_name, category_id, supplier_id)
    VALUES (1, 'Amazon Fire', 49.9, 'Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.', 'product_1.jpg', 1, 1);
INSERT INTO public.product (id, name, default_price, description, picture_name, category_id, supplier_id)
    VALUES (2, 'Lenovo IdeaPad Miix 700', 479, 'Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand', 'product_2.jpg', 1, 2);
INSERT INTO public.product (id, name, default_price, description, picture_name, category_id, supplier_id)
    VALUES (3, 'Amazon Fire HD 8', 89, 'Amazon''s latest Fire HD 8 tablet is a great value for media consumption.', 'product_3.jpg', 1, 1);
INSERT INTO public.product (id, name, default_price, description, picture_name, category_id, supplier_id)
    VALUES (4, 'Hello Kitty Smartphone', 99, 'Smartphone for girls', 'product_4.jpg', 2, 4);
INSERT INTO public.product (id, name, default_price, description, picture_name, category_id, supplier_id)
    VALUES (5, 'Pink flip smartphone', 79, 'Smartphone for little girls', 'product_5.jpg', 2, 4);
INSERT INTO public.product (id, name, default_price, description, picture_name, category_id, supplier_id)
    VALUES (6, 'Old school phone', 149, 'Smartphone for real man', 'product_6.jpg', 2, 3);
INSERT INTO public.product (id, name, default_price, description, picture_name, category_id, supplier_id)
    VALUES (7, 'Lego set', 59, 'Lego set for kids', 'product_7.jpg', 3, 6);
INSERT INTO public.product (id, name, default_price, description, picture_name, category_id, supplier_id)
    VALUES (8, 'Lego set Fire Station', 209, 'Lego set with fire station for kids', 'product_8.jpg', 3, 6);
INSERT INTO public.product (id, name, default_price, description, picture_name, category_id, supplier_id)
    VALUES (9, 'Lego set Star Wars', 119, 'Lego set with Star Wars theme for kids', 'product_8.jpg', 3, 6);