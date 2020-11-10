package com.codecool.shop.dao.dao;

import com.codecool.shop.model.order.OrderAddressDetail;
import java.util.List;

public interface OrderAddressDetailDao {
    void add(OrderAddressDetail orderAddressDetail);
    OrderAddressDetail find(int id);
    void update(OrderAddressDetail orderAddressDetail);
    void remove(int id);
    List<OrderAddressDetail> getAll();
}
