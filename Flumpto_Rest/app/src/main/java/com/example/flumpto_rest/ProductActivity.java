package com.example.flumpto_rest;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flumpto_rest.Adapter.ProductAdapter;
import com.example.flumpto_rest.Model.Product;
import com.example.flumpto_rest.Retrofit.FlumptoAPI;
import com.example.flumpto_rest.Utils.Common;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ProductActivity extends AppCompatActivity {
    FlumptoAPI mService;

    RecyclerView lst_product;

    TextView txt_banner_name;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        mService = Common.getAPI();
        lst_product =(RecyclerView)findViewById(R.id.recycler_products);
        lst_product.setLayoutManager(new GridLayoutManager(this,2));
        lst_product.setHasFixedSize(true);

        txt_banner_name = (TextView)findViewById(R.id.txt_menu_name);
        loadListProduct(Common.currentCategory.ID);
    }

    private void loadListProduct(String menuId) {
        compositeDisposable.add(mService.getProduct(menuId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<Product>>() {
            @Override
            public void accept(List<Product> products) throws Exception {
                displayProductList(products);
            }
        }));

    }

    private void displayProductList(List<Product> products) {
        ProductAdapter adapter = new ProductAdapter(this,products);
        lst_product.setAdapter(adapter);
    }
    boolean isBackButtonClicked = false;
    @Override
    public void onBackPressed() {
        if(isBackButtonClicked)
        {
            super.onBackPressed();
            return;
        }
        this.isBackButtonClicked = true;
        Toast.makeText(this,"Porfavor vuelva a presionar ATRAS para salir",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        isBackButtonClicked = false;
    }
}
