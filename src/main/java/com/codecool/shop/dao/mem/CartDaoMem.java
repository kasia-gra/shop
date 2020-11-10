package com.codecool.shop.dao.mem;

import com.codecool.shop.dao.dao.CartDao;
import com.codecool.shop.model.order.Cart;
import com.codecool.shop.model.product.Product;

import java.util.ArrayList;
import java.util.List;

public class CartDaoMem implements CartDao {
    private int sequenceNumber = 1;
    private List<Cart> data = new ArrayList<>();
    private static CartDaoMem instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private CartDaoMem() {
    }

    public static CartDaoMem getInstance() {
        if (instance == null) {
            instance = new CartDaoMem();
        }
        return instance;
    }

    @Override
    public void addEmptyCart(Cart cart) {
        cart.setId(sequenceNumber);
        data.add(cart);
        sequenceNumber++;
//        return cart.getId();
    }

    @Override
    public Cart find(int id) {
        return data.stream().
                filter(cart -> cart.getId() == id).
                findFirst().
                orElse(null);
    }

    @Override
    public void addItemToCart(int cartId, int productId, int addedQuantity) {
    }

    @Override
    public void removeItemFromCart(int lineItemId, int cartId){

    };

    @Override
    public void remove(int id) {
        data.remove(find(id));
    }
}

