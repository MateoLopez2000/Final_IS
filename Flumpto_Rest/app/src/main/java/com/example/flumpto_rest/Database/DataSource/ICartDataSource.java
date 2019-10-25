package com.example.flumpto_rest.Database.DataSource;

import com.example.flumpto_rest.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public interface ICartDataSource {
    Flowable<List<Cart>> getCartItems();
    Flowable<List<Cart>> getCartItemById(int cartItemId);
    int countCartItmes();
    float sumPrice();
    void emptyCart();
    void  insertToCart(Cart...carts);
    void  updateCart(Cart...carts);
    void deleteCartItem(Cart cart);
}
