package com.example.heroicmarketplace.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.heroicmarketplace.Cart.CartDao;
import com.example.heroicmarketplace.Cart.CartDatabase;
import com.example.heroicmarketplace.Cart.CartModel;
import com.example.heroicmarketplace.R;

import java.util.ArrayList;
import java.util.List;

import com.example.heroicmarketplace.Adapters.Cart_Adapter;
import com.example.heroicmarketplace.Models.CartItems_model;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Network.Base_url;


public class Cart extends Fragment {
    RecyclerView recview;
    TextView KOST,KOUNT;
    Cart_Adapter cart_adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    Button Checkout;
    int sum=0,i;
    String count="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart, null, false);

        if( ((AppCompatActivity)getActivity()).getSupportActionBar()!=null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }

        swipeRefreshLayout=view.findViewById(R.id.CARTVIEW);
        Checkout=view.findViewById(R.id.checkout);
        KOUNT=view.findViewById(R.id.count);
        KOST = view.findViewById(R.id.KOSTi);
        recview = view.findViewById(R.id.rv_cart);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));

        getcartdata();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getcartdata();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000); // 3 seconds
            }
        });

        Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 showdialog();
            }
        });

        return view;
    }

    private void getcartdata() {
        CartDatabase db = Room.databaseBuilder(getContext().getApplicationContext(),
                CartDatabase.class, "cart_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();

        CartDao cartDao = db.cDao();
        List<CartModel> carts = cartDao.getallcart();
        cart_adapter = new Cart_Adapter(getContext(), carts);
        recview.setAdapter(cart_adapter);

        for (i = 0; i < carts.size(); i++)
            sum=sum+(carts.get(i). getCost() * carts.get(i).getQnty());
         count= String.valueOf(carts.size());
         KOST.setText( String.format("%s.KES", String.valueOf(sum)));
         KOUNT.setText(count);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cart_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Count",count);
        editor.putBoolean("user_cart", true);
        editor.commit();
    }

    private void showdialog(){
        final Dialog dialog=new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.checkout);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp =new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = 900;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        TextView ITEMS= dialog.findViewById(R.id.items);
        TextView costi=dialog.findViewById(R.id.Totalcosti);
        EditText Phone=dialog.findViewById(R.id.phone);
        Button pay= dialog.findViewById(R.id.PAY);

        ITEMS.setText(count);
        costi.setText(String.format("%s .KES", String.valueOf(sum)));
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        String NO=Phone.getText().toString();

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paynow(NO);
            }
        });

    }

    private void paynow(String no){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Base_url.paynow(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
//                    JSONObject object =new JSONObject(response);
                    JSONArray array = new JSONArray("ResponseDescription");
                           String res=array.toString();
                           if(res.equalsIgnoreCase("Success. Request accepted for processing")){
                               Toast.makeText(getContext(),"wait for an stk push", Toast.LENGTH_LONG);
                           }
                           else {
                               Toast.makeText(getContext(),"an error occurred", Toast.LENGTH_LONG);
                           }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }
    }