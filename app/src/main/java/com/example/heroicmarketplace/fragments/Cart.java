package com.example.heroicmarketplace.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.heroicmarketplace.Cart.CartDao;
import com.example.heroicmarketplace.Cart.CartDatabase;
import com.example.heroicmarketplace.Cart.CartModel;
import com.example.heroicmarketplace.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.heroicmarketplace.Adapters.Cart_Adapter;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.heroicmarketplace.Network.Base_url;


public class Cart extends Fragment {
    RecyclerView recview;
    TextView KOST,KOUNT;
    Cart_Adapter cart_adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    Button Checkout;
    int sum=0,i;
    String count="";
    String cart_id=" ";

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

//                filteritems();
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

        cart_id=String.valueOf(sum*carts.size()/Math.random()*100)+"Hmp";

        Toast.makeText(getContext(),cart_id, Toast.LENGTH_LONG).show();

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


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PNO=Phone.getText().toString();
                    String NO=PNO.substring(1);
                if(PNO.isEmpty()){
                    Phone.setError("please enter your phone number");
                }
                else if(PNO.length()<10){
                    Phone.setError("please enter a valid phone number");
                }
                else{
                    processpay("254"+NO);
                }
            }
        });

    }

    private void processpay(String no){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Base_url.paynow(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject object =new JSONObject(response);
                     String feed=object.getString("res");
                           if(feed.equalsIgnoreCase("transaction queued for processing")){
                               Toast.makeText(getContext(),"wait for an stk push and enter your mpesa pin", Toast.LENGTH_LONG).show();
                               Toast.makeText(getContext(),"wait for an stk push and enter your mpesa pin", Toast.LENGTH_LONG).show();

                               new Handler().postDelayed(new Runnable() {
                                   @Override
                                   public void run() {
                                       savecart();
                                   }
                               }, 2000);

                           }
                           else {
                               Toast.makeText(getContext(),"an error occurred try again", Toast.LENGTH_LONG).show();
                           }
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(getContext(),"an error occurred try again", Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = getString(R.string.network_error);
                } else if (error instanceof ServerError) {
                    message = getString(R.string.server_error);
                } else if (error instanceof AuthFailureError) {
                    message = getString(R.string.auth_error);
                } else if (error instanceof ParseError) {
                    message = getString(R.string.parse_error);
                } else if (error instanceof TimeoutError) {
                    message = getString(R.string.timeout_error);
                } else {
                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap <String, String > map=new HashMap<>();
                map.put("phone", no);
                map.put("amount", String.valueOf(sum));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void savecart(){
        CartDatabase db = Room.databaseBuilder(getContext().getApplicationContext(),
                        CartDatabase.class, "cart_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();

        CartDao cartDao = db.cDao();
        List<CartModel> carts = cartDao.getallcart();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Base_url.paynow(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject object =new JSONObject(response);
                    String feed=object.getString("msg");
                    if(feed.equalsIgnoreCase("transaction successfully completed")){
                        Toast.makeText(getContext(),"transaction successfully completed", Toast.LENGTH_LONG).show();
                        saveTransaction();
                    }
                    else {
                        Toast.makeText(getContext(),"an error occurred try again", Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(getContext(),"an error occurred try again", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = getString(R.string.network_error);
                } else if (error instanceof ServerError) {
                    message = getString(R.string.server_error);
                } else if (error instanceof AuthFailureError) {
                    message = getString(R.string.auth_error);
                } else if (error instanceof ParseError) {
                    message = getString(R.string.parse_error);
                } else if (error instanceof TimeoutError) {
                    message = getString(R.string.timeout_error);
                } else {
                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap <String, String > map=new HashMap<>();
                map.put("cart_id", cart_id);
                           for(int i=0; i<carts.size(); i++) {
                    map.put("items", carts.get(i).getTitle());
                }
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void saveTransaction(){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Base_url.paynow(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject object =new JSONObject(response);
                    String feed=object.getString("msg");
                    if(feed.equalsIgnoreCase("transaction successfully completed")){
                        Toast.makeText(getContext(),"transaction successfully completed", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getContext(),"an error occurred try again", Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(getContext(),"an error occurred try again", Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = getString(R.string.network_error);
                } else if (error instanceof ServerError) {
                    message = getString(R.string.server_error);
                } else if (error instanceof AuthFailureError) {
                    message = getString(R.string.auth_error);
                } else if (error instanceof ParseError) {
                    message = getString(R.string.parse_error);
                } else if (error instanceof TimeoutError) {
                    message = getString(R.string.timeout_error);
                } else {
                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap <String, String > map=new HashMap<>();
                    map.put("cart_id",cart_id);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    }