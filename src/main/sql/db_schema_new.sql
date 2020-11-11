DROP TYPE IF EXISTS status CASCADE;
CREATE TYPE status AS ENUM ('paid', 'checked', 'confirmed', 'shipped');


DROP TABLE IF EXISTS public.order;
CREATE TABLE public.order
(
    id                       serial NOT NULL PRIMARY KEY,
    cart_id                  int    NOT NULL,
    user_id                  int    NOT NULL,
    order_address_details_id int    NOT NULL,
    date                     timestamp,
    current_status           status

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

ALTER TABLE ONLY "order"
    ADD CONSTRAINT "cart_id" FOREIGN KEY (cart_id)
        REFERENCES cart (id) ON DELETE CASCADE;

ALTER TABLE ONLY "order"
    ADD CONSTRAINT "user_id" FOREIGN KEY (user_id)
        REFERENCES "user" (id) ON DELETE CASCADE;

ALTER TABLE ONLY "order"
    ADD CONSTRAINT "order_address_details_id" FOREIGN KEY (order_address_details_id)
        REFERENCES "address_detail" (id) ON DELETE CASCADE;

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
