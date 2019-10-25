package com.example.flumpto_rest.Retrofit;



import com.example.flumpto_rest.Model.Banner;
import com.example.flumpto_rest.Model.Category;
import com.example.flumpto_rest.Model.CheckUserResponse;
import com.example.flumpto_rest.Model.Product;
import com.example.flumpto_rest.Model.User;

import java.util.List;


import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface FlumptoAPI {
    @FormUrlEncoded
    @POST("checkuser.php")
    Call<CheckUserResponse> checkUserExists(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("register.php")
    Call<User> registerNewUser(@Field("phone") String phone,
                               @Field("name") String name,
                               @Field("birthdate") String birthdate,
                               @Field("address") String address);
    @FormUrlEncoded
    @POST("getproduct.php")
    Observable<List<Product>> getProduct(@Field("menuid") String menuID);

    @FormUrlEncoded
    @POST("getuser.php")
    Call<User> getUserInformation(@Field("phone") String phone);

    @GET("getbanner.php")
    Observable<List<Banner>> getBanners();

    @GET("getmenu.php")
    Observable<List<Category>> getMenu();

    @FormUrlEncoded
    @POST("submitorder.php")
    Call<String> submitOrder(@Field("orderPrice") float orderPrice,
                           @Field("orderDetail") String orderDetail,
                           @Field("orderAddress") String orderAddress,
                           @Field("userPhone") String userPhone,
                             @Field("NIT")String NIT);
    @GET("getallproducts.php")
    Observable<List<Product>> getAllProducts();

    @FormUrlEncoded
    @POST("braintree/checkout.php")
    Call<String> payment(@Field("nonce") String nonce,
                             @Field("amount") String amount);
}
