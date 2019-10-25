package com.example.flumpto_rest.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.flumpto_rest.Database.ModelDB.Cart;
import com.example.flumpto_rest.R;
import com.example.flumpto_rest.Utils.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context context;
    List<Cart> cartList;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.cart_item_layout,parent,false);
        return new CartViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {
        Picasso.with(context)
                .load(cartList.get(position).link)
                .into(holder.img_product);
        holder.txt_amount.setNumber(String.valueOf(cartList.get(position).amount));
        holder.txt_price.setText(new StringBuilder("Bs.").append(cartList.get(position).price));
        holder.txt_product_name.setText(new StringBuilder(cartList.get(position).name)
                .append(" x")
                .append(cartList.get(position).amount)
                        .append(cartList.get(position).tamaño == 0 ? "Tamaño S": cartList.get(position).tamaño == 1 ? "Tamaño M": "Tamaño L"));
     /*   if (cartList.get(position).tamaño == 0)
            holder.txt_product_name.setText(new StringBuilder(cartList.get(position).name)
                    .append(" x")
                    .append(cartList.get(position).amount)
                    .append("Tamaño S"));
        if (cartList.get(position).tamaño  == 1)
                holder.txt_product_name.setText(new StringBuilder(cartList.get(position).name)
                .append(" x")
                .append(cartList.get(position).amount)
                .append("Tamaño M"));
        else if (cartList.get(position).tamaño  == 2)
            {    holder.txt_product_name.setText(new StringBuilder(cartList.get(position).name)
                    .append(" x")
                    .append(cartList.get(position).amount)
                    .append("Tamaño L"));

        }*/


        final double preciodeunproducto = cartList.get(position).price / cartList.get(position).amount;
//Auto guardar item cuando se cambia de tamaño
        holder.txt_amount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Cart cart = cartList.get(position);
                cart.amount = newValue;
                cart.price = Math.round(preciodeunproducto*newValue);
                Common.cartRepository.updateCart(cart);

                holder.txt_price.setText(new StringBuilder("Bs.").append(cartList.get(position).price));
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img_product;
        TextView txt_product_name,txt_price;
        ElegantNumberButton txt_amount;
        public RelativeLayout view_background;
        public LinearLayout view_foreground;



        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product =(ImageView)itemView.findViewById(R.id.img_product);
           // txt_amount = (ElegantNumberButton)itemView.findViewById(R.id.txt_amount);
            txt_product_name= (TextView)itemView.findViewById(R.id.txt_product_name);
            txt_price= (TextView)itemView.findViewById(R.id.txt_price);
            txt_amount =(ElegantNumberButton)itemView.findViewById(R.id.txt_amount);

            view_background = (RelativeLayout)itemView.findViewById(R.id.view_background);
            view_foreground = (LinearLayout)itemView.findViewById(R.id.view_foreground);

        }
    }
    public void removeItem(int position)
    {
        cartList.remove(position);
        notifyItemRemoved(position);
    }
    public void restoreItem(Cart item, int position)
    {
        cartList.add(position,item);
        notifyItemInserted(position);
    }
}
