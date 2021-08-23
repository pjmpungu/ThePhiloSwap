package com.thephiloswap.thephiloswapbeta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BookDescriptionActivity extends AppCompatActivity {

    TextView tvTitle, tvAuthor, tvDes;

    Button btnCancel, btnSwap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_description);

        //setup this activity so it's a popup

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.75),(int)(height * 0.75));

        tvTitle = findViewById(R.id.tvBDTitle);
        tvAuthor = findViewById(R.id.tvBDAuthor);
        tvDes = findViewById(R.id.tvBDDescription);

        btnCancel = findViewById(R.id.btnExit);
        btnSwap = findViewById(R.id.btnSwap);

        //use index from intent to display data of the book

        int index = getIntent().getIntExtra("book", 0);

        tvTitle.setText("Title: " + ApplicationClass.books.get(index).getTitle());
        tvAuthor.setText("Author: " + ApplicationClass.books.get(index).getAuthor());
        tvDes.setText(ApplicationClass.books.get(index).getDescription());

        //when cancel button is pressed close the activity

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BookDescriptionActivity.this.finish();

            }
        });

        //if swap button pressed, open up the request book activity

        btnSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BookDescriptionActivity.this.finish();
                Intent intent = new Intent(BookDescriptionActivity.this, RequestBookActivity.class);
                intent.putExtra("index", index);
                startActivity(intent);

            }
        });

    }
}