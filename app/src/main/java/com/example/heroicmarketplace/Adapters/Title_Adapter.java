package com.example.heroicmarketplace.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.heroicmarketplace.Models.Title_model;
import com.example.heroicmarketplace.R;

import java.util.ArrayList;

public class Title_Adapter extends RecyclerView.Adapter<title_adapter> {
    public final Context context;
    public  final ArrayList<Title_model> title_models;
    public  Title_Adapter.Moreclicklistener morelistener;
    boolean check;


    public Title_Adapter(Context context, ArrayList<Title_model> title_model) {
        this.context = context;
        this.title_models =title_model;

    }

    @NonNull
    @Override
    public title_adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.category_head_items,null,false);
          return  new title_adapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull title_adapter holder, int position) {
        Title_model tmodel=title_models.get(position);

           holder.title.setText(tmodel.getTitle());
            Glide.with(context)
                .load(tmodel.getImage())
                .into(holder.img);

            holder.catlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    morelistener.onclicking(v,tmodel.getId());
                    check = true;
                        holder.title.setBackgroundColor(ContextCompat.getColor(context, R.color.lblue));
                }
            });
            if(!check){
                holder.title.setBackgroundColor(ContextCompat.getColor(context, R.color.white2));
            }
    }

    @Override
    public int getItemCount() {
        return title_models.size();
    }


    public interface Moreclicklistener{
        void onclicking(View v, String pid );
    }

    public void Layclick(Title_Adapter.Moreclicklistener mrlistener) {
        this.morelistener=mrlistener;
    }




}


  class title_adapter extends RecyclerView.ViewHolder{
        ImageView img;
        TextView title;
        RelativeLayout catlay;
      public title_adapter(@NonNull View itemView) {
          super(itemView);
          img=itemView.findViewById(R.id.img_cat);
          title=itemView.findViewById(R.id.category);
          catlay=itemView.findViewById(R.id.cat_layout);
      }
  }