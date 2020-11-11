
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

