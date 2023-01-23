package com.example.heroicmarketplace.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.heroicmarketplace.Cart.CartDao;
import com.example.heroicmarketplace.Cart.CartDatabase;
import com.example.heroicmarketplace.Cart.CartModel;
import com.example.heroicmarketplace.R;
import com.example.heroicmarketplace.fragments.Cart;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Cart_Adapter extends RecyclerView.Adapter<cart_adapter> {
     Context context;
     List<CartModel> cartlist;

    public Cart_Adapter(Context context, List<CartModel> cartlist) {
        this.context = context;
        this.cartlist = cartlist;
    }


    @NonNull
    @Override
    public cart_adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.cart_items,null,false);
        return  new cart_adapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull cart_adapter holder, int position) {
        CartModel cart=cartlist.get(position);
                holder.cost.setText(String.format("%s. KES", String.valueOf(cart.getCost())));
                holder.title.setText(cartlist.get(position).getTitle());
                                   Glide.with(context)
                                        .load(cart.getImage())
                                        .into(holder.img);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "pull to refresh the cart", Toast.LENGTH_LONG).show();

                CartDatabase db= Room.databaseBuilder(context,
                                CartDatabase.class,"cart_db")
                               .fallbackToDestructiveMigration()
                               .allowMainThreadQueries().build();

                db.cDao().deleteById(cart.getId());
                    cartlist.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartlist.size();
    }

}

  class cart_adapter extends RecyclerView.ViewHolder{
      ImageView img,delete;
      TextView cost,title;

      public cart_adapter(@NonNull View itemView) {
          super(itemView);

          delete=itemView.findViewById(R.id.deleteicon);
          img=itemView.findViewById(R.id.cart_img);
          cost=itemView.findViewById(R.id.cart_cost);
          title=itemView.findViewById(R.id.cart_title);
      }

  }