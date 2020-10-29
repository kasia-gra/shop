DROP TABLE IF EXISTS public.products;
CREATE TABLE public.products (
    id serial NOT NULL PRIMARY KEY,
    name text NOT NULL,
    price float NOT NULL,
    currency text NOT NULL,
    description text NOT NULL,
    picture_name text NOT NULL,
    category_id int NOT NULL,
    supplier_id int NOT NULL
);

DROP TABLE IF EXISTS public.categories;
CREATE TABLE public.categories (
    id serial NOT NULL PRIMARY KEY,
    name text NOT NULL,
    department text NOT NULL,
    description text NOT NULL
);

DROP TABLE IF EXISTS public.suppliers;
CREATE TABLE public.suppliers (
    id serial NOT NULL PRIMARY KEY,
    name text NOT NULL,
    description text NOT NULL
);

DROP TABLE IF EXISTS public.users;
CREATE TABLE public.users (
    id serial NOT NULL PRIMARY KEY,
    login text NOT NULL,
    email text NOT NULL,
    password text NOT NULL,
    phone_number text,
    registration_date timestamp,
    billing_address_id int NOT NULL,
    address_id int NOT NULL
);

DROP TABLE IF EXISTS public.address;
CREATE TABLE public.address (
    id serial NOT NULL PRIMARY KEY,
    country text NOT NULL,
    city text NOT NULL,
    zip_code text NOT NULL,
    address text NOT NULL
);
