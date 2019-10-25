package com.example.flumpto_rest.Utils;

import com.example.flumpto_rest.Database.DataSource.CartRepository;
import com.example.flumpto_rest.Database.Local.CartDatabase;
import com.example.flumpto_rest.Model.Category;
import com.example.flumpto_rest.Model.User;
import com.example.flumpto_rest.Retrofit.FlumptoAPI;
import com.example.flumpto_rest.Retrofit.RetrofitClient;
import com.example.flumpto_rest.Retrofit.RetrofitScalarsClient;

import retrofit2.Retrofit;

public class Common {
    public static final String BASE_URL = "http://flumpto.site/";
    public static final String API_TOKEN_URL = "http://flumpto.site/braintree/main.php";

    public static User currentUser = null;
    public static Category currentCategory= null;

    public static int tamañoProducto = -1; // Ninguno escogido

    //Database
    public static CartDatabase cartDatabase;
    public static CartRepository cartRepository;

    public static FlumptoAPI getAPI()

    {
        return RetrofitClient.getClient(BASE_URL).create(FlumptoAPI.class);
    }
    public static FlumptoAPI getScalarsAPI()

    {
        return RetrofitScalarsClient.getScalarsClient(BASE_URL).create(FlumptoAPI.class);
    }

}
