package com.example.heroicmarketplace.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.example.heroicmarketplace.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.heroicmarketplace.Adapters.Home_Adapter;
import com.example.heroicmarketplace.Adapters.Title_Adapter;
import com.example.heroicmarketplace.Models.Home_model;
import com.example.heroicmarketplace.Models.Title_model;

import Network.Base_url;

public class Home extends Fragment {

    GridView gridView;
    RecyclerView cateview;
    Toolbar toolbar;
    public ArrayList<Title_model> title_model;
    Title_Adapter title_adapter;

    Home_Adapter home_adapter;
    ArrayList<Home_model> home_models;

//    public String url_products = "https://dc28-102-5-254-105.in.ngrok.io/api/category/items";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);

        cateview = view.findViewById(R.id.recycler_category);
        gridView = view.findViewById(R.id.conte_gv);

        title_model = new ArrayList<>();
        cateview.setHasFixedSize(true);
        cateview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        title_adapter = new Title_Adapter(getContext(), title_model);
        cateview.setAdapter(title_adapter);
        home_models = new ArrayList<>();

        String category = "1";

        fetchcategories();
        fetchproducts(category);
        clicks();

        return view;
    }


    private void clicks() {
        title_adapter.Layclick(new Title_Adapter.Moreclicklistener() {
            @Override
            public void onclicking(View v, String category) {
                getActivity().getSupportFragmentManager().beginTransaction().detach(Home.this).attach(Home.this).commit();
                home_models = new ArrayList<>();
                home_adapter = new Home_Adapter(getContext(), home_models);
                gridView.setAdapter(home_adapter);
                fetchproducts(category);

            }
        });
    }

    private void fetchproducts(String category) {
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
                        String image = object.getString("image");
                        String title = object.getString("title");
                        String cost = object.getString("cost");

                        Home_model hmdl = new Home_model(id, image, title, cost);
                        home_models.add(hmdl);
                    }
                    home_adapter = new Home_Adapter(getActivity(), home_models);
                    gridView.setAdapter(home_adapter);
                } catch (JSONException e) {
//                            progressDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                swipegrid.setRefreshing(true);
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
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("category_id", category);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void fetchcategories() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Base_url.getcategories(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("id");
                                String image = object.getString("image");
                                String title = object.getString("title");
                                Title_model tmodel = new Title_model(id, image, title);
                                title_model.add(tmodel);
                                title_adapter.notifyDataSetChanged();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
        });
        RequestQueue queue = Volley.newRequestQueue(getContext());
        jsonObjectRequest.setRetryPolicy(
                new DefaultRetryPolicy(0,
                        -1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }

//    private void all(String category) {
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, Base_url.all, response -> {
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                JSONArray jsonArray = jsonObject.getJSONArray("data");
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject object = jsonArray.getJSONObject(i);
//                    String id = object.getString("id");
//                    String image = object.getString("image");
//                    String title = object.getString("title");
//                    String cost = object.getString("cost");
//
//                    Home_model hmdl = new Home_model(id, image, title, cost);
//                    home_models.add(hmdl);
//                }
//                home_adapter = new Home_Adapter(getActivity(), home_models);
//                gridView.setAdapter(home_adapter);
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }, error -> {
//            error.printStackTrace();
//            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//
//        })
//        {
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> map = new HashMap<>();
//                map.put("category_id", category);
//                return map;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.add(stringRequest);
//
//    }
}

