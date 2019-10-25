package com.example.flumpto_rest.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.flumpto_rest.Database.ModelDB.Cart;
import com.example.flumpto_rest.Interface.ItemClickListener;
import com.example.flumpto_rest.Model.Product;
import com.example.flumpto_rest.R;
import com.example.flumpto_rest.Utils.Common;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

   Context context;
   List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.product_item_layout,null);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, final int position) {

        holder.txt_price.setText(new StringBuilder("Bs.").append(productList.get(position).Price).toString());
        holder.txt_product_name.setText(productList.get(position).Name);
        holder.btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddToCartDialog(position);
            }
        });

        Picasso.with(context)
                .load(productList.get(position).Link)
                .into(holder.img_product);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddToCartDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.add_to_cart_layout,null);

        ImageView img_product_dialog = (ImageView)itemView.findViewById(R.id.img_cart_product);
        final ElegantNumberButton txt_count= (ElegantNumberButton)itemView.findViewById(R.id.txt_count);
        TextView txt_product_dialog = (TextView)itemView.findViewById(R.id.txt_cart_product_name);

      //  EditText edt_comment= (EditText)itemView.findViewById(R.id.edt_comment);

        RadioButton rdi_sizeS = (RadioButton)itemView.findViewById(R.id.rdi_sizeS);
        RadioButton rdi_sizeM = (RadioButton)itemView.findViewById(R.id.rdi_sizeM);
        RadioButton rdi_sizeL = (RadioButton)itemView.findViewById(R.id.rdi_sizeL);

        rdi_sizeS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Common.tamañoProducto=0;
            }
        });
        rdi_sizeM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Common.tamañoProducto=1;
            }
        });
        rdi_sizeL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Common.tamañoProducto=2;
            }
        });



        Picasso.with(context)
                .load(productList.get(position).Link)
                .into(img_product_dialog);
        txt_product_dialog.setText(productList.get(position).Name);

            builder.setView(itemView);
            builder.setNegativeButton("AÑADIR A CARRITO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (Common.tamañoProducto == -1)
                    {
                        Toast.makeText(context, "Porfavor escoja un tamaño",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showConfirmDialog(position,txt_count.getNumber());
                    dialog.dismiss();
                }
            });
            builder.show();
    }

    private void showConfirmDialog(final int position, final String number) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.confirm_add_to_cart_layout,null);

        ImageView img_product_dialog = (ImageView)itemView.findViewById(R.id.img_product);
        final TextView txt_product_dialog = (TextView)itemView.findViewById(R.id.txt_cart_product_name);
        final TextView txt_product_price = (TextView)itemView.findViewById(R.id.txt_cart_product_price);

        Picasso.with(context).load(productList.get(position).Link).into(img_product_dialog);
      txt_product_dialog.setText(new StringBuilder(productList.get(position).Name).append(" x")
              .append(Common.tamañoProducto == 0 ? "Tamaño S": Common.tamañoProducto == 1 ? "Tamaño M": "Tamaño L")
               .append(number).toString());


                double price = (Double.parseDouble(productList.get(position).Price)*Double.parseDouble(number));

                if(Common.tamañoProducto ==1)
                    price+=(7.0*Double.parseDouble(number));
                if(Common.tamañoProducto ==2)
                    price+=(11.0*Double.parseDouble(number));


        final double finalPrice = Math.round(price);

        txt_product_price.setText(new StringBuilder("Bs.").append(finalPrice));
        builder.setNegativeButton("CONFIRMAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        dialog.dismiss();
                        try {
                            Cart cartItem = new Cart();
                            cartItem.name = productList.get(position).Name;
                            cartItem.amount = Integer.parseInt(number);
                            cartItem.price = finalPrice;
                            cartItem.tamaño = Common.tamañoProducto;
                            cartItem.link = productList.get(position).Link;

                            //Añadir a base datos
                            Common.cartRepository.insertToCart(cartItem);

                            Log.d("VV_DEBUG", new Gson().toJson(cartItem));

                            Toast.makeText(context, "Item agregado al carrito satisfactoriamente", Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(context,ex.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setView(itemView);
                builder.show();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
