package com.example.heroicmarketplace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT=1300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        CartDatabase db= Room.databaseBuilder(getApplicationContext(),
//                CartDatabase.class,"Cart-database").build();
//        Cart first=new Cart("shoe","addidas image","2000");
//        Cart second=new Cart("Shirt","plolo tshirt","750");

//        db.cDao().inserAll(first,second);
//
//        List<Cart> cartList=db.cDao().getAllcart();
//           for(Cart list: cartList)
//           {
//               Log.d("cart", list.title+ " "+ list.image+ "" +list.cost);
//           }
//        handless();
handless();

    }




    private  void handless() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isconneted(MainActivity.this))
                {
//                    showcustomdialog();
                    Intent intent = new Intent(MainActivity.this, Host.class);
                    startActivity(intent);
                    finish();
                } else
                {
                        Intent intent = new Intent(MainActivity.this, Host.class);
                        startActivity(intent);
                        finish();
                    }
            }
        }, SPLASH_TIME_OUT);
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

                        Toast.makeText(MainActivity.this,date,Toast.LENGTH_LONG).show();
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
}