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