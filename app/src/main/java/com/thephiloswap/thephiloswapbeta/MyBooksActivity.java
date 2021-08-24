package com.thephiloswap.thephiloswapbeta;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;
import java.util.List;

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

        Backendless.Data.of(Book.class).find(new AsyncCallback<List<Book>>(){
            @Override
            public void handleResponse( List<Book> foundBooks )
            {

                ApplicationClass.books = foundBooks;
                myAdapter = new BookAdapter(MyBooksActivity.this, (ArrayList<Book>) ApplicationClass.books, true);

                //set adapter for recylcer view

                recyclerView.setAdapter(myAdapter);
                layoutManager = new LinearLayoutManager(MyBooksActivity.this);
                recyclerView.setLayoutManager(layoutManager);

            }
            @Override
            public void handleFault( BackendlessFault fault )
            {

                Toast.makeText(MyBooksActivity.this, "Error: " + fault.getMessage()
                        , Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onItemClicked(int index) {

        //when an item is clicked open up the book description index
        Intent intent = new Intent(MyBooksActivity.this, MyBooksDescriptionActivity.class);
        intent.putExtra("book", index);
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(myAdapter!=null)
            myAdapter.notifyDataSetChanged();
    }


}