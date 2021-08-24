package com.thephiloswap.thephiloswapbeta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MyBooksDescriptionActivity extends AppCompatActivity {

    EditText etTitle, etAuthor, etDes;

    Button btnCancel, btnSwap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books_description);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.75),(int)(height * 0.75));


        etTitle = findViewById(R.id.etBDTitle);
        etAuthor = findViewById(R.id.etBDAuthor);
        etDes = findViewById(R.id.etDes);

        btnCancel = findViewById(R.id.btnDelete);
        btnSwap = findViewById(R.id.btnSave);

        //use index from intent to display data of the book

        int index = getIntent().getIntExtra("book", 0);

        etTitle.setText("Title: " + ApplicationClass.books.get(index).getTitle());
        etAuthor.setText("Author: " + ApplicationClass.books.get(index).getAuthor());
        etDes.setText(ApplicationClass.books.get(index).getDescription());

        //when delete button is pressed delete the book

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //if save button is pressed save changes to book

        btnSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });
    }
}