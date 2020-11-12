package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.dao.DataDao;

import javax.sql.DataSource;
import java.io.*;
import java.nio.file.Files;
import java.sql.*;

public class DataDaoJdbc implements DataDao {

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
            String sql = "INSERT INTO public.supplier (name, description)\n" +
                    "VALUES ('Amazon', 'Digital content and services');\n" +
                    "INSERT INTO public.supplier (name, description)\n" +
                    "VALUES ('Lenovo', 'Computers');\n" +
                    "\n" +
                    "INSERT INTO public.category (name, department, description)\n" +
                    "VALUES ('Tablets', 'Hardware', 'A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.');\n" +
                    "\n" +
                    "INSERT INTO public.product (name, default_price, description, picture_name, category_id, supplier_id)\n" +
                    "VALUES ('Amazon Fire', 49.9, 'Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.', 'product_1.jpg', 1, 1);\n" +
                    "INSERT INTO public.product (name, default_price, description, picture_name, category_id, supplier_id)\n" +
                    "VALUES ('Lenovo IdeaPad Miix 700', 479, 'Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand', 'product_2.jpg', 1, 2);\n" +
                    "INSERT INTO public.product (name, default_price, description, picture_name, category_id, supplier_id)\n" +
                    "VALUES ('Amazon Fire HD 8', 89, 'Amazon''s latest Fire HD 8 tablet is a great value for media consumption.', 'product_3.jpg', 1, 1);\n" +
                    "INSERT INTO public.product (name, default_price, description, picture_name, category_id, supplier_id)\n" +
                    "VALUES ('Hello Kitty Smartphone', 99, 'Smartphone for girls', 'product_4.jpg', 2, 4);";
            Statement st = conn.createStatement();
            st.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
