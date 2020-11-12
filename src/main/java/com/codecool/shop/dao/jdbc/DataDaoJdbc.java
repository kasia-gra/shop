package com.codecool.shop.dao.jdbc;

import javax.sql.DataSource;
import java.io.*;
import java.nio.file.Files;
import java.sql.*;

public class DataDaoJdbc {

    private final DataSource dataSource;

    public DataDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void createTable() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
        String sql = "\n" +
                "DROP TABLE IF EXISTS public.product;\n" +
                "CREATE TABLE public.product\n" +
                "(\n" +
                "    id            serial NOT NULL PRIMARY KEY,\n" +
                "    name          text   NOT NULL,\n" +
                "    default_price float  NOT NULL,\n" +
                "    description   text   NOT NULL,\n" +
                "    picture_name  text   NOT NULL,\n" +
                "    category_id   int    NOT NULL,\n" +
                "    supplier_id   int    NOT NULL\n" +
                ");\n" +
                "\n" +
                "\n" +
                "DROP TABLE IF EXISTS public.category;\n" +
                "CREATE TABLE public.category\n" +
                "(\n" +
                "    id          serial NOT NULL PRIMARY KEY,\n" +
                "    name        text   NOT NULL,\n" +
                "    department  text   NOT NULL,\n" +
                "    description text   NOT NULL\n" +
                ");\n" +
                "\n" +
                "\n" +
                "DROP TABLE IF EXISTS public.supplier;\n" +
                "CREATE TABLE public.supplier\n" +
                "(\n" +
                "    id          serial NOT NULL PRIMARY KEY,\n" +
                "    name        text   NOT NULL,\n" +
                "    description text   NOT NULL\n" +
                ");\n";
        Statement st = conn.createStatement();
        st.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addData() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO public.supplier (id, name, description)\n" +
                    "VALUES (1, 'Amazon', 'Digital content and services');\n" +
                    "INSERT INTO public.supplier (id, name, description)\n" +
                    "VALUES (2, 'Lenovo', 'Computers');\n" +
                    "INSERT INTO public.supplier (id, name, description)\n" +
                    "VALUES (3, 'Nokia', 'Smartphones and stuff');\n" +
                    "INSERT INTO public.supplier (id, name, description)\n" +
                    "VALUES (4, 'Samsung', 'Just stuff');\n" +
                    "INSERT INTO public.supplier (id, name, description)\n" +
                    "VALUES (6, 'Lego', 'Bricks');\n" +
                    "\n" +
                    "INSERT INTO public.category (id, name, department, description)\n" +
                    "VALUES (1, 'Tablets', 'Hardware', 'A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.');\n" +
                    "INSERT INTO public.category (id, name, department, description)\n" +
                    "VALUES (2, 'Smartphones', 'Hardware', 'Smartphone, Everyone knows what is it.');\n" +
                    "INSERT INTO public.category (id, name, department, description)\n" +
                    "VALUES (3, 'Lego Bricks', 'Toys', 'Plastic construction toys that are manufactured by The Lego Group, a privately held company based in Billund, Denmark.');\n" +
                    "\n" +
                    "INSERT INTO public.product (id, name, default_price, description, picture_name, category_id, supplier_id)\n" +
                    "VALUES (1, 'Amazon Fire', 49.9, 'Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.', 'product_1.jpg', 1, 1);\n" +
                    "INSERT INTO public.product (id, name, default_price, description, picture_name, category_id, supplier_id)\n" +
                    "VALUES (2, 'Lenovo IdeaPad Miix 700', 479, 'Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand', 'product_2.jpg', 1, 2);\n" +
                    "INSERT INTO public.product (id, name, default_price, description, picture_name, category_id, supplier_id)\n" +
                    "VALUES (3, 'Amazon Fire HD 8', 89, 'Amazon''s latest Fire HD 8 tablet is a great value for media consumption.', 'product_3.jpg', 1, 1);\n" +
                    "INSERT INTO public.product (id, name, default_price, description, picture_name, category_id, supplier_id)\n" +
                    "VALUES (4, 'Hello Kitty Smartphone', 99, 'Smartphone for girls', 'product_4.jpg', 2, 4);\n" +
                    "INSERT INTO public.product (id, name, default_price, description, picture_name, category_id, supplier_id)\n" +
                    "VALUES (5, 'Pink flip smartphone', 79, 'Smartphone for little girls', 'product_5.jpg', 2, 4);\n" +
                    "INSERT INTO public.product (id, name, default_price, description, picture_name, category_id, supplier_id)\n" +
                    "VALUES (6, 'Old school phone', 149, 'Smartphone for real man', 'product_6.jpg', 2, 3);\n" +
                    "INSERT INTO public.product (id, name, default_price, description, picture_name, category_id, supplier_id)\n" +
                    "VALUES (7, 'Lego set', 59, 'Lego set for kids', 'product_7.jpg', 3, 6);\n" +
                    "INSERT INTO public.product (id, name, default_price, description, picture_name, category_id, supplier_id)\n" +
                    "VALUES (8, 'Lego set Fire Station', 209, 'Lego set with fire station for kids', 'product_8.jpg', 3, 6);\n" +
                    "INSERT INTO public.product (id, name, default_price, description, picture_name, category_id, supplier_id)\n" +
                    "VALUES (9, 'Lego set Star Wars', 119, 'Lego set with Star Wars theme for kids', 'product_8.jpg', 3, 6);";
            Statement st = conn.createStatement();
            st.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
