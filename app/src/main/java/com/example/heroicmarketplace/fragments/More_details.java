package com.example.heroicmarketplace.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.heroicmarketplace.Adapters.Home_Adapter;
import com.example.heroicmarketplace.Cart.CartDao;
import com.example.heroicmarketplace.Cart.CartDatabase;
import com.example.heroicmarketplace.Cart.CartModel;
import com.example.heroicmarketplace.Models.CartItems_model;
import com.example.heroicmarketplace.Models.Home_model;
import com.example.heroicmarketplace.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.heroicmarketplace.Adapters.Products_Adapter;

import Network.Base_url;

public class More_details extends Fragment {
    TextView category,name,desc,cost;
    ImageView LIKE,prod_img;
    Button AddCart;
    TextView cart;
    List<Home_model> home_models;
    Products_Adapter products_adapter;
    RecyclerView recyclerView;

    String image="";
    String  title="";
    String Cost="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.more_details,container,false);
         String prod_id = getArguments().getString("prod_id");
        if( ((AppCompatActivity)getActivity()).getSupportActionBar()!=null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }

        cart=view.findViewById(R.id.mcart);
        AddCart=view.findViewById(R.id.Pcart);
        category=view.findViewById(R.id.Tcategory);
        name=view.findViewById(R.id.Pname);
        desc=view.findViewById(R.id.Pdesc);
        cost=view.findViewById(R.id.Pcost);
        prod_img=view.findViewById(R.id.Pimg);
        recyclerView=view.findViewById(R.id.others_rv);

        home_models = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        products_adapter = new Products_Adapter(getContext(),    home_models);
        recyclerView.setAdapter(products_adapter);

        LIKE=view.findViewById(R.id.like);

        LIKE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LIKE.setBackgroundColor(Color.RED);
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               replacefrag(new Cart());
            }
        });

        AddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTocart(prod_id);
            }
        });

        getcartcount();

        fetchdetails(prod_id);

        return view;
    }


    private void getcartcount() {
        CartDatabase db= Room.databaseBuilder(getActivity().getApplicationContext(),
                        CartDatabase.class,"cart_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();

        CartDao cartDao=db.cDao();

        List<CartModel> carts = cartDao.getallcart();
        String count= String.valueOf(carts.size());
        cart.setText(count);
    }

    private void fetchdetails(String prod_id){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_url.productdetails(), new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String description  = object.getString("description");
                        String  Category  = object.getString("category");
                               image  = object.getString("image");
                               title  = object.getString("title");
                               Cost  = object.getString("price");

                        category.setText(Category);
                        name.setText(title);
                        desc.setText(description);
                        cost.setText(String.format("%s KES", Cost));
                               Glide.with(getContext())
                                       .load(image)
                                       .into(prod_img);
                               categs(Category);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "an error occurred try again", Toast.LENGTH_SHORT).show();
                }
                    }
                },
                new Response.ErrorListener()
                    {
                @Override
                    public void onErrorResponse (VolleyError error){
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
            @Override
            protected HashMap<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("prod_id", prod_id);
                return map;
            }
        };
      requestQueue.add(stringRequest);
}

    private void categs(String category){
        String c_id="";
        if(category.equals("Men cloth")) {
            c_id="2";
            fetchfamiliar(c_id);
        }
        if(category.equals("Women cloth")){
            c_id="3";
            fetchfamiliar(c_id);
        }
        if(category.equals("Baby cloth")){
            c_id="4";
            fetchfamiliar(c_id);
        }
        if(category.equals("Beauty")){
            c_id="5";
            fetchfamiliar(c_id);
        }
        if(category.equals("Eletronics")){
            c_id="6";
            fetchfamiliar(c_id);
        }
        if(category.equals("Jewellery")){
            c_id="7";
            fetchfamiliar(c_id);
        }
        if(category.equals("Kitchen appliance")){
            c_id="8";
            fetchfamiliar(c_id);
        }
    }

    private void fetchfamiliar(String category){

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_url.getallproducts(), new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("id");
                        String image  = object.getString("image");
                        String title  = object.getString("title");
                        String cost  = object.getString("cost");

                        Home_model hmdl=new Home_model(id,image,title,cost);
                        home_models.add(hmdl);
                    }
                        products_adapter = new Products_Adapter(getActivity(), home_models);
                        recyclerView.setAdapter(products_adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                    }
                },
                new Response.ErrorListener() {
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
            @Override
            protected HashMap<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("category_id", category);
                return map;
            }
        };
        requestQueue.add(stringRequest);
   }

    private void replacefrag(@NonNull Fragment fragment) {
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void addTocart(String prod_id){
        int Quantity=1;
        int kost=Integer.parseInt(Cost);

        CartDatabase db= Room.databaseBuilder(getActivity().getApplicationContext(),
                         CartDatabase.class,"cart_db")
                          .fallbackToDestructiveMigration()
                         .allowMainThreadQueries().build();

        CartDao cartDao=db.cDao();
        Boolean check= cartDao.is_exist(Integer.parseInt(prod_id));
        if(check==false){

          CartModel second=new CartModel(prod_id,title,image,kost,Quantity);
            db.cDao().insertcart(second);
            Toast.makeText(getContext(), "Product added to cart", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(), "product already exist", Toast.LENGTH_SHORT).show();
        }


        List<CartModel> carts = cartDao.getallcart();
        String count= String.valueOf(carts.size());
        cart.setText(count);


    }



    }