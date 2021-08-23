package com.thephiloswap.thephiloswapbeta;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements BookAdapter.ItemClicked{

    Button btnAddBook;
    ImageButton ivRefresh;

    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnAddBook = findViewById(R.id.btnAddBook);

        ivRefresh = findViewById(R.id.ivRefresh);

        recyclerView = findViewById(R.id.list);

        //retrieve table of books from backened and add them to our list

        Backendless.Data.of(Book.class).find(new AsyncCallback<List<Book>>(){
            @Override
            public void handleResponse(List<Book> foundBooks )
            {

                ApplicationClass.books = foundBooks;
                myAdapter = new BookAdapter(HomeActivity.this, (ArrayList<Book>) ApplicationClass.books);

                //set adapter for recylcer view

                recyclerView.setAdapter(myAdapter);
                layoutManager = new LinearLayoutManager(HomeActivity.this);
                recyclerView.setLayoutManager(layoutManager);

            }
            @Override
            public void handleFault( BackendlessFault fault )
            {

                Toast.makeText(HomeActivity.this, "Error: " + fault.getMessage()
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
                        myAdapter = new BookAdapter(HomeActivity.this, (ArrayList<Book>) ApplicationClass.books);

                        //set adapter for recylcer view

                        recyclerView.setAdapter(myAdapter);

                    }
                    @Override
                    public void handleFault( BackendlessFault fault )
                    {

                        Toast.makeText(HomeActivity.this, "Error: " + fault.getMessage()
                                , Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        //when the add book button is pressed it takes the user to a screen to add books

        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this, AddBookActivity.class);
                startActivityForResult(intent, 1);

            }
        });
    }

    //when activity started from add book screen update the list

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){

            myAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClicked(int index) {

    }
}