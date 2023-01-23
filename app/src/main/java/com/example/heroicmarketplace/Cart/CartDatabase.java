package com.example.heroicmarketplace.Cart;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.heroicmarketplace.Models.CartItems_model;

@Database(entities = {CartModel.class}, version = 7)
public abstract class CartDatabase extends RoomDatabase {

    private static CartDatabase instance;

    public abstract CartDao cDao();



    public static synchronized CartDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (CartDatabase.class){
                if(instance == null)
                {
                    instance=Room.databaseBuilder(context.getApplicationContext(),
                            CartDatabase.class,"cart_db").fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
            return instance;
        }



//
//            instance = Room.databaseBuilder(context.getApplicationContext(),
//                            CartDatabase.class, "cart_database")
//                    .fallbackToDestructiveMigration()
//                    .addCallback(roomCallback)
//                    .build();
//        }
//        return instance;
//    }
//
//    public static RoomDatabase.Callback roomCallback = new Callback() {
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//            new PopulateDbAsyncTask(instance).execute();
//        }
//    };
//
//    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
//        PopulateDbAsyncTask(CartDatabase instance) {
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            return null;
//        }
//    }
}