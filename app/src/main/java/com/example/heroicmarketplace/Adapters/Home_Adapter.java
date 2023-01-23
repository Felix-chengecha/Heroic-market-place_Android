package com.example.heroicmarketplace.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.heroicmarketplace.Models.Home_model;
import com.example.heroicmarketplace.R;
import com.example.heroicmarketplace.fragments.More_details;

import java.util.ArrayList;

public class Home_Adapter  extends ArrayAdapter<Home_model> {
    public final Context context;


    public Home_Adapter(Context context, ArrayList<Home_model> home_models) {
        super(context, 0, home_models);
        this.context = context;


    }


    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {

            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.home_items, parent, false);
        }

        Home_model hmdl=getItem(position);
        ImageView imgv=listitemView.findViewById(R.id.home_img);
        TextView title=listitemView.findViewById(R.id.home_title);
        TextView cost=listitemView.findViewById(R.id.home_cost);
        RelativeLayout relativeLayout=listitemView.findViewById(R.id.Rlayout);

        title.setText(hmdl.getTitle());
        cost.setText(String.format("%s KES", hmdl.getCost()));
        Glide.with(context)
                .load(hmdl.getImage())
                .into(imgv);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity appCompatActivity=(AppCompatActivity) v.getContext();
                More_details more_details=new More_details();

                FragmentManager fragmentManager = appCompatActivity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, more_details);
                Bundle args = new Bundle();
                args.putString("prod_id", hmdl.getId());
                more_details.setArguments(args);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return listitemView;
    }
}
