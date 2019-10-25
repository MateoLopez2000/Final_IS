package com.example.flumpto_rest.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.flumpto_rest.Interface.ItemClickListener;
import com.example.flumpto_rest.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView img_product;
    TextView txt_menu_name;
    ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CategoryViewHolder(View itemView){
        super(itemView);

      img_product=(ImageView)itemView.findViewById(R.id.image_product);
    txt_menu_name=(TextView)itemView.findViewById(R.id.txt_menu_name);
    itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v);
    }
}
