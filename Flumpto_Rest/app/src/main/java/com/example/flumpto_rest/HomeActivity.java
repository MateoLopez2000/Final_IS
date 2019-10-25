package com.example.flumpto_rest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.flumpto_rest.Adapter.CategoryAdapter;
import com.example.flumpto_rest.Database.Local.CartDataSource;
import com.example.flumpto_rest.Model.Banner;
import com.example.flumpto_rest.Model.Category;
import com.example.flumpto_rest.Retrofit.FlumptoAPI;
import com.example.flumpto_rest.Utils.Common;
import com.facebook.accountkit.AccountKit;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.example.flumpto_rest.Utils.Common.cartDatabase;
import static com.example.flumpto_rest.Utils.Common.cartRepository;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txt_name,txt_phone;
    SliderLayout sliderLayout;
     FlumptoAPI mService;

     RecyclerView lst_menu;

    NotificationBadge badge;
    ImageView cart_icon;

     CompositeDisposable compositeDisposable = new CompositeDisposable();
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mService = Common.getAPI();

        lst_menu =(RecyclerView)findViewById(R.id.lst_menu);
        lst_menu.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        lst_menu.setHasFixedSize(true);
        sliderLayout = (SliderLayout)findViewById(R.id.slider);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        txt_name =(TextView)headerView.findViewById(R.id.txt_name);
        txt_phone=(TextView)headerView.findViewById(R.id.txt_phone);

        txt_name.setText(Common.currentUser.getName());
        txt_phone.setText(Common.currentUser.getPhone());

        getBannerImage();
        getMenu();

        initDB();

    }

    private void initDB() {
         cartDatabase = cartDatabase.getInstance(this);
         Common.cartRepository = cartRepository.getInstance(CartDataSource.getInstance(cartDatabase.cartDAO()));
    }

    private void getMenu() {
        compositeDisposable.add(mService.getMenu()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Category>>() {
                    @Override
                    public void accept(List<Category> categories) throws Exception {
                        displayMenu(categories);
                    }
                }));
    }

    private void displayMenu(List<Category> categories) {
        CategoryAdapter adapter = new CategoryAdapter(this,categories);
        lst_menu.setAdapter(adapter);
    }

    private void getBannerImage() {
         compositeDisposable.add(mService.getBanners()
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new Consumer<List<Banner>>() {
                     @Override
                     public void accept(List<Banner> banners) throws Exception {
                     displayImage(banners);
                     }
                 }));
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
         super.onDestroy();
    }

    private void displayImage(List<Banner> banners) {
        HashMap<String, String> bannerMap = new HashMap<>();
        for(Banner item:banners)
            bannerMap.put(item.getName(),item.getLink());
        for(String name:bannerMap.keySet())
        {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView.description(name)
                    .image(bannerMap.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            sliderLayout.addSlider(textSliderView);
        }
    }

    boolean isBackButtonClicked = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(isBackButtonClicked)
            {
                super.onBackPressed();
                return;
            }
            this.isBackButtonClicked = true;
            Toast.makeText(this,"Porfavor vuelva a presionar ATRAS para salir",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        View view = menu.findItem(R.id.cart_menu).getActionView();
        badge = (NotificationBadge)view.findViewById(R.id.badge);
        cart_icon = (ImageView)view.findViewById(R.id.cart_icon);
        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,CartActivity.class));
            }
        });
        updateCartCount();
        return true;
    }

    private void updateCartCount() {
         if(badge == null) return;
         runOnUiThread(new Runnable() {
             @Override
             public void run() {
                 if(cartRepository.countCartItmes()==0)
                     badge.setVisibility(View.INVISIBLE);
                 else
                     {
                        badge.setVisibility(View.VISIBLE);
                        badge.setText(String.valueOf(cartRepository.countCartItmes()));
                 }

             }
         });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cart_menu) {
            return true;
        }
        else if(id == R.id.search_menu)
        {
            startActivity(new Intent(HomeActivity.this,SearchActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sign_out) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Cerrar Aplicación");
            builder.setMessage("Seguro que quiere salir?");
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AccountKit.logOut();

                    Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setPositiveButton("CANCELAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();

        }
        else if(id ==  R.id.nav_location)
        {
            startActivity(new Intent(HomeActivity.this,MapsActivity.class));

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartCount();
        isBackButtonClicked = false;
    }




}
