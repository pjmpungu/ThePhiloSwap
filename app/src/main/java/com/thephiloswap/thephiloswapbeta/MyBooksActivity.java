package com.thephiloswap.thephiloswapbeta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;

public class MyBooksActivity extends HomeActivity implements BookAdapter.ItemClicked {

    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_my_books, null, false);
        mDrawer.addView(contentView, 0);
        nvDrawer.setCheckedItem(R.id.my_books);

        recyclerView = findViewById(R.id.list);

        myAdapter = new BookAdapter(MyBooksActivity.this, (ArrayList<Book>) ApplicationClass.books, true);

        //set adapter for recylcer view

        recyclerView.setAdapter(myAdapter);
        layoutManager = new LinearLayoutManager(MyBooksActivity.this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public void onItemClicked(int index) {

    }
}