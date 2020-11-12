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

    @Override
    public void addData() {
    }

    public void addDataForProductTest() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO public.supplier (name, description)\n" +
                    "VALUES ('supplier_1', 'suppl_description_1');\n" +
                    "INSERT INTO public.supplier (name, description)\n" +
                    "VALUES ('supplier_2', 'suppl_description_2');\n" +
                    "\n" +
                    "INSERT INTO public.category (name, department, description)\n" +
                    "VALUES ('cat_1', 'cat_dept_1', 'cat_description_1');\n" +
                    "\n" +
                    "INSERT INTO public.product (name, default_price, description, picture_name, category_id, supplier_id)\n" +
                    "VALUES ('prod_1', 100, 'prod_desc_1', 'product_1.jpg', 1, 1);\n" +
                    "INSERT INTO public.product (name, default_price, description, picture_name, category_id, supplier_id)\n" +
                    "VALUES ('prod_2', 200, 'prod_desc_2', 'product_2.jpg', 1, 1);\n" +
                    "INSERT INTO public.product (name, default_price, description, picture_name, category_id, supplier_id)\n" +
                    "VALUES ('prod_3', 300, 'prod_desc_3', 'product_3.jpg', 1, 1);\n" +
                    "INSERT INTO public.product (name, default_price, description, picture_name, category_id, supplier_id)\n" +
                    "VALUES ('prod_4', 400, 'prod_desc_4', 'product_4.jpg', 1, 2);";
            Statement st = conn.createStatement();
            st.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
