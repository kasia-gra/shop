package com.codecool.shop.dao.dao;

import com.codecool.shop.model.AddressDetail;
import java.util.List;

public interface AddressDetailDao {
    void add(AddressDetail addressDetail);
    AddressDetail find(int id);
    void update(AddressDetail addressDetail);
    void remove(int id);
    List<AddressDetail> getAll();
}
