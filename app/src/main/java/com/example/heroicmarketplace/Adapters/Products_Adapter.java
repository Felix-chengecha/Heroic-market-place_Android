package com.example.heroicmarketplace.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.heroicmarketplace.Models.Home_model;
import com.example.heroicmarketplace.R;
import com.example.heroicmarketplace.fragments.More_details;

import java.util.List;

public class Products_Adapter extends RecyclerView.Adapter<product_adapter> {
    public final Context context;
    public final List<Home_model> home_modelList;

    public Products_Adapter(Context context, List<Home_model> home_models) {
        this.context = context;
        this.home_modelList =home_models;
    }

    @NonNull
    @Override
    public product_adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.product_items,parent,false);
        return new product_adapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull product_adapter holder, int position) {
              Home_model home_model=home_modelList.get(position);
              holder.cost.setText(String.format("%s. KES", home_model.getCost()));
              holder.name.setText(home_model.getTitle());

        Glide.with(context)
                .load(home_model.getImage())
                .into(holder.img);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity appCompatActivity=(AppCompatActivity) v.getContext();
                More_details more_details=new More_details();

                FragmentManager fragmentManager = appCompatActivity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, more_details);
                Bundle args = new Bundle();
                args.putString("prod_id", home_model.getId());
                more_details.setArguments(args);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return home_modelList.size();
    }
}

   class product_adapter extends RecyclerView.ViewHolder{
       ImageView img;
       TextView name,cost;
       RelativeLayout relativeLayout;
       public product_adapter(@NonNull View itemView) {
           super(itemView);
           img=itemView.findViewById(R.id.prod_img);
           name=itemView.findViewById(R.id.prod_name);
           cost=itemView.findViewById(R.id.prod_cost);
           relativeLayout=itemView.findViewById(R.id.others_rl);
       }
   }
