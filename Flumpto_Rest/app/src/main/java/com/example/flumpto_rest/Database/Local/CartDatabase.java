package com.example.flumpto_rest.Database.Local;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.flumpto_rest.Database.ModelDB.Cart;

@Database(entities = {Cart.class},version = 1 )
public abstract class CartDatabase extends RoomDatabase {
    public abstract CartDAO cartDAO();
    private static CartDatabase instance;

    public static  CartDatabase getInstance(Context context)
    {
        if(instance==null)
            instance = Room.databaseBuilder(context,CartDatabase.class, "VV_FlumptoDb")
                    .allowMainThreadQueries()
                    .build();
        return instance;

    }
}
