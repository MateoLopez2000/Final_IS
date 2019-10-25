package com.example.flumpto_rest;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.example.flumpto_rest.Adapter.CartAdapter;
import com.example.flumpto_rest.Database.ModelDB.Cart;
import com.example.flumpto_rest.Retrofit.FlumptoAPI;
import com.example.flumpto_rest.Utils.Common;
import com.example.flumpto_rest.Utils.RecycleItemTouchHelper;
import com.example.flumpto_rest.Utils.RecyclerItemTouchHelperListener;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity  implements RecyclerItemTouchHelperListener {

    RecyclerView recycler_cart;
    Button btn_place_order;
    CompositeDisposable compositeDisposable;
    List<Cart> cartList = new ArrayList<>();

    RelativeLayout rootLayout;

    CartAdapter cartAdapter;

    FlumptoAPI mService;
    FlumptoAPI mServicesScalars;

    //String Global

    String token;
    String amount;
    String orderAddress;
    String NIT;
    HashMap<String,String> params;
    private static final int PATMENT_REQUEST_CODE = 7777;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mService = Common.getAPI();
        mServicesScalars = Common.getScalarsAPI();
        compositeDisposable = new CompositeDisposable();



        recycler_cart = (RecyclerView)findViewById(R.id.recycler_cart);
        recycler_cart.setLayoutManager(new LinearLayoutManager(this));
        recycler_cart.setHasFixedSize(true);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecycleItemTouchHelper(0,ItemTouchHelper.LEFT,this );
                new ItemTouchHelper(simpleCallback).attachToRecyclerView(recycler_cart);

        btn_place_order = (Button)findViewById(R.id.btn_place_order);
        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              placeOrder();
            }
        });

        rootLayout = (RelativeLayout)findViewById(R.id.rootLayout);

        loadCartItems();
        loadToken();
    }

    private void loadToken() {

        final android.app.AlertDialog waitingDialog = new SpotsDialog(CartActivity.this);
        waitingDialog.show();
        waitingDialog.setMessage("Porfavor espere...");

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Common.API_TOKEN_URL, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    waitingDialog.dismiss();
                    btn_place_order.setEnabled(false);
                    Toast.makeText(CartActivity.this,throwable.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                     waitingDialog.dismiss();
                     token = responseString;
                     btn_place_order.setEnabled(true);
            }
        });
    }

    private void placeOrder() {
        // Crear dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Subir Orden");
        View submit_order_layout = LayoutInflater.from(this).inflate(R.layout.submit_order_layout, null);

         final EditText edt_NIT =(EditText)submit_order_layout.findViewById(R.id.edt_NIT);
         final EditText edt_other_address =(EditText)submit_order_layout.findViewById(R.id.edt_other_address);

         final RadioButton rdi_user_address = (RadioButton)submit_order_layout.findViewById(R.id.rdi_user_address);
         final RadioButton rdi_other_address = (RadioButton)submit_order_layout.findViewById(R.id.rdi_other_address);

         rdi_user_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
       @Override
       public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
           if(isChecked) {
               edt_other_address.setEnabled(false);
           }
       }
      });
         rdi_other_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
     @Override
     public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
         if(isChecked) {
             edt_other_address.setEnabled(true);
         }}
 });builder.setView(submit_order_layout);
        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
     @Override
     public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
            }
 }).setPositiveButton("SUBIR", new DialogInterface.OnClickListener() {
     @Override
     public void onClick(DialogInterface dialog, int which) {
         NIT = edt_NIT.getText().toString();
        // final String orderAddress;
         if (rdi_user_address.isChecked()){

             orderAddress = Common.currentUser.getAddress();}
         else if (rdi_other_address.isChecked()){
             orderAddress = edt_other_address.getText().toString();}
         else{
             orderAddress = "";}

         //Pago
         DropInRequest dropInRequest = new DropInRequest().clientToken(token);
         startActivityForResult(dropInRequest.getIntent(CartActivity.this),PATMENT_REQUEST_CODE);




     }
   });
     builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       if (requestCode == PATMENT_REQUEST_CODE)
       {
           if(resultCode == RESULT_OK)
           {
               DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
               PaymentMethodNonce nonce = result.getPaymentMethodNonce();
               String strNonce = nonce.getNonce();

               if(Common.cartRepository.sumPrice() > 0)
               {
                   amount = String.valueOf(Common.cartRepository.sumPrice());
                   params = new HashMap<>();
                   params.put("amount",amount);
                   params.put("nonce",strNonce);

                   sendPayment();
               }
               else
               {
                   Toast.makeText(this,"El Costo Total es 0", Toast.LENGTH_SHORT).show();
               }
           }
           else if(resultCode == RESULT_CANCELED)
               Toast.makeText(this,"El Pago ha sido cancelado", Toast.LENGTH_SHORT).show();
           else
           {
               Exception error = (Exception)data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
               Log.e("EDMT ERROR",error.getMessage());
           }
       }
}

    private void sendPayment() {
        mServicesScalars.payment(params.get("nonce"),params.get("amount"))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.body().toString().contains("Successful"))
                        {
                            Toast.makeText(CartActivity.this,"Transacción completada exitosamenete", Toast.LENGTH_SHORT).show();
                            //Subir Orden
                            compositeDisposable.add(
                                    Common.cartRepository.getCartItems()
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(new Consumer<List<Cart>>() {
                                                @Override
                                                public void accept(List<Cart> carts) throws Exception {
                                                    if (!TextUtils.isEmpty(orderAddress)){
                                                        sendOrdertoServer(Common.cartRepository.sumPrice(), carts, orderAddress,NIT);}
                                                    else
                                                    {Toast.makeText(CartActivity.this, "La Dirección no puede ser vacia", Toast.LENGTH_SHORT).show();}
                                                }
                                            })
                            );
                        }
                        else
                        {
                            Toast.makeText(CartActivity.this,"Transacción fallida", Toast.LENGTH_SHORT).show();
                        }
                        Log.d("EDMT_INFO",response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("EDMT_INFO",t.getMessage());
                    }
                });
    }

    private void sendOrdertoServer(float sumprice, List<Cart> carts, String orderAddress, String NIT) {
        if(carts.size() > 0)
        {
            String orderDetail = new Gson().toJson(carts);
            mService.submitOrder(sumprice,orderDetail,orderAddress,Common.currentUser.getPhone(),NIT)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Toast.makeText(CartActivity.this,"Orden Subida", Toast.LENGTH_SHORT).show();

                            Common.cartRepository.emptyCart();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("ERROR",t.getMessage());
                        }
                    });
        }
    }


    private void loadCartItems() {
        compositeDisposable.add(
                Common.cartRepository.getCartItems()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Cart>>() {
                    @Override
                    public void accept(List<Cart> carts) throws Exception {
                        displayCartItem(carts);
                    }
                })
        );
    }

    private void displayCartItem(List<Cart> carts) {
        cartList = carts;
        cartAdapter = new CartAdapter(this,carts);
        recycler_cart.setAdapter(cartAdapter);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadCartItems();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof CartAdapter.CartViewHolder)
        {
         String name = cartList.get(viewHolder.getAdapterPosition()).name;

         final Cart deletedItem = cartList.get(viewHolder.getAdapterPosition());
         final int deletedIndex = viewHolder.getAdapterPosition();

         //Remover item del adapter
            cartAdapter.removeItem(deletedIndex);
            // Eliminar de la base de datos
            Common.cartRepository.deleteCartItem(deletedItem);

            Snackbar snackbar = Snackbar.make(rootLayout,new StringBuilder(name).append(" eliminado del Carrito").toString(),
                    Snackbar.LENGTH_LONG);
            snackbar.setAction("DESHACER", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartAdapter.restoreItem(deletedItem,deletedIndex);
                    Common.cartRepository.insertToCart(deletedItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();


        }
    }
}
