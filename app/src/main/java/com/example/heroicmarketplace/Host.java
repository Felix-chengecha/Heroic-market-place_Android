package com.example.heroicmarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heroicmarketplace.Cart.CartDao;
import com.example.heroicmarketplace.Cart.CartDatabase;
import com.example.heroicmarketplace.Cart.CartModel;
import com.example.heroicmarketplace.fragments.Cart;
import com.example.heroicmarketplace.fragments.Home;
import com.example.heroicmarketplace.fragments.Search;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Host extends AppCompatActivity {
    public TextView Hcart;
    ImageView search;
    public Toolbar toolbar;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        search=findViewById(R.id.search_link);
        Hcart=findViewById(R.id.hcart);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        toolbar=findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new Home()).commit();

        getcartcount();

        Hcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replacefrag(new Cart());
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replacefrag(new Search());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {

            switch (item.getItemId())
            {

                case R.id.home:
                    if (!isconneted(Host.this)) {
                        showcustomdialog();
                    } else {
//                            replacefrag(new MainActivity2());
                    }
                    break;
                case R.id.cart:
                    if (!isconneted(Host.this)) {
                        showcustomdialog();
                    } else {
//                            replacefrag(new MainActivity2());
                    }
                    break;

                case R.id.bookmarks:
                    if (!isconneted(Host.this))
                    {
                        showcustomdialog();
                    } else {
//                            replacefrag(new All_users_home());
                    }
                    break;
                case R.id.myaccount:
                    if (!isconneted( Host.this))
                    {
                        showcustomdialog();
                    } else {
//                            replacefrag(new SearchHome());
                    }
                    break;
                case R.id.share:
//                        replacefrag(new FragmentShare());
                    break;
                case R.id.feedback:
//                        replacefrag(new FragmentContact());
                    break;
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void getcartcount() {
        CartDatabase db= Room.databaseBuilder(getApplicationContext(),
                        CartDatabase.class,"cart_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();
        CartDao cartDao=db.cDao();
        List<CartModel> carts = cartDao.getallcart();
        String count= String.valueOf(carts.size());
        Hcart.setText(count);
    }

    private void showcustomdialog() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
        alertDialog.setMessage("!no internet connection please connect to proceed")
                .setCancelable(false)
                .setPositiveButton("connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_IP_SETTINGS));
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                        Toast.makeText(Host.this,date,Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
        alertDialog.create().show();
    }

    private boolean isconneted(Context context){
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wificon=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobicon=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wificon.isConnected() && wificon !=null) || mobicon.isConnected() && mobicon !=null )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void showDialog () {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?");
        alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    private void replacefrag(@NonNull Fragment fragment) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
