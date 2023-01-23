package com.example.heroicmarketplace.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.util.StringUtil;

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
import com.bumptech.glide.Glide;
import com.example.heroicmarketplace.Adapters.Home_Adapter;
import com.example.heroicmarketplace.Adapters.Products_Adapter;
import com.example.heroicmarketplace.Models.Home_model;
import com.example.heroicmarketplace.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Network.Base_url;

public class Search extends Fragment {

    EditText search;
    GridView gridView;
    TextView MSG;
    Home_Adapter home_adapter;
    TextInputLayout textInputLayout;
    ArrayList<Home_model> home_models;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.search,container,false);

        if( ((AppCompatActivity)getActivity()).getSupportActionBar()!=null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }

        textInputLayout=view.findViewById(R.id.sl);
        search=view.findViewById(R.id.searchquery);
        gridView=view.findViewById(R.id.search_gv);
        MSG=view.findViewById(R.id.msg);

        home_models = new ArrayList<>();
        home_adapter = new Home_Adapter(getContext(), home_models);
        gridView.setAdapter(home_adapter);


        textInputLayout.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                           home_models.clear();
                           MSG.setText(" ");
                    String edit=search.getText().toString().trim();
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    searchproduct(edit);

                    return true;
                }
                return false;
            }
        });

        return view;
    }

    private void searchproduct(String query){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_url.searchproduct(), new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
                try {
                        JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if(jsonArray.length()==0)
                            {
                                MSG.setText("no result for your search");
                            }
                            else{
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("id");
                                String image = object.getString("image");
                                String title = object.getString("title");
                                String cost = object.getString("cost");

                                Home_model hmdl = new Home_model(id, image, title, cost);
                                home_models.add(hmdl);
                            }
                        }
                    home_adapter = new Home_Adapter(getActivity(), home_models);
                    gridView.setAdapter(home_adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
                map.put("uinput", query);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

}
