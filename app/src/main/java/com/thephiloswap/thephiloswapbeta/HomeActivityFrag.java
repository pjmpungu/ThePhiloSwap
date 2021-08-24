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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;
import java.util.List;

public class HomeActivityFrag extends HomeActivity implements BookAdapter.ItemClicked{

    Button btnAddBook;
    ImageButton ivRefresh;

    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_home_frag, null, false);
        mDrawer.addView(contentView, 0);
        nvDrawer.setCheckedItem(R.id.home);

        btnAddBook = findViewById(R.id.btnAddBook);

        ivRefresh = findViewById(R.id.ivRefresh);

        recyclerView = findViewById(R.id.list);



        Backendless.Data.of(Book.class).find(new AsyncCallback<List<Book>>(){
            @Override
            public void handleResponse(List<Book> foundBooks )
            {

                ApplicationClass.books = foundBooks;
                myAdapter = new BookAdapter(HomeActivityFrag.this, (ArrayList<Book>) ApplicationClass.books, false);

                //set adapter for recylcer view

                recyclerView.setAdapter(myAdapter);
                layoutManager = new LinearLayoutManager(HomeActivityFrag.this);
                recyclerView.setLayoutManager(layoutManager);

            }
            @Override
            public void handleFault( BackendlessFault fault )
            {

                Toast.makeText(HomeActivityFrag.this, "Error: " + fault.getMessage()
                        , Toast.LENGTH_SHORT).show();

            }
        });

        //when you click the refresh button it updates the list

        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Backendless.Data.of(Book.class).find(new AsyncCallback<List<Book>>(){
                    @Override
                    public void handleResponse( List<Book> foundBooks )
                    {

                        ApplicationClass.books = foundBooks;
                        myAdapter = new BookAdapter(HomeActivityFrag.this, (ArrayList<Book>) ApplicationClass.books, false);

                        //set adapter for recylcer view

                        recyclerView.setAdapter(myAdapter);

                    }
                    @Override
                    public void handleFault( BackendlessFault fault )
                    {

                        Toast.makeText(HomeActivityFrag.this, "Error: " + fault.getMessage()
                                , Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        //when the add book button is pressed it takes the user to a screen to add books

        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivityFrag.this, AddBookActivity.class);
                startActivity(intent);

            }
        });
    }

    //when activity started from add book screen update the list


    @Override
    protected void onResume() {
        super.onResume();

        if(myAdapter!=null)
            myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(int index) {
        Intent intent = new Intent(HomeActivityFrag.this, BookDescriptionActivity.class);
        intent.putExtra("book", index);
        startActivity(intent);

    }


}