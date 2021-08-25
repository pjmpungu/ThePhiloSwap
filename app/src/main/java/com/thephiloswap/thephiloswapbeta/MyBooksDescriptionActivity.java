package com.thephiloswap.thephiloswapbeta;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class MyBooksDescriptionActivity extends AppCompatActivity {

    EditText etTitle, etAuthor, etDes, etKeywords;

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
        etKeywords = findViewById(R.id.etKeywords);

        btnCancel = findViewById(R.id.btnDelete);
        btnSwap = findViewById(R.id.btnSave);

        //use index from intent to display data of the book

        int index = getIntent().getIntExtra("book", 0);

        etTitle.setText(ApplicationClass.userBooks.get(index).getTitle());
        etAuthor.setText(ApplicationClass.userBooks.get(index).getAuthor());
        etDes.setText(ApplicationClass.userBooks.get(index).getDescription());
        etKeywords.setText(ApplicationClass.userBooks.get(index).getKeywords());

        //when delete button is pressed delete the book

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //set up a dialog box

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked, delete this book

                                Backendless.Data.of(Book.class).remove(ApplicationClass.userBooks.get(index), new AsyncCallback<Long>() {
                                    @Override
                                    public void handleResponse(Long response) {

                                        Toast.makeText(MyBooksDescriptionActivity.this, "Removed Succesfully"
                                                , Toast.LENGTH_SHORT).show();
                                        MyBooksDescriptionActivity.this.finish();
                                        ApplicationClass.userBooks.remove(index);

                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {

                                        Toast.makeText(MyBooksDescriptionActivity.this, "Error: " + fault.getMessage()
                                                , Toast.LENGTH_SHORT).show();

                                    }
                                });
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(MyBooksDescriptionActivity.this);
                builder.setMessage("Delete " + ApplicationClass.userBooks.get(index).getTitle() + "?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });

        //if save button is pressed save changes to book

        btnSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //first check to make sure necessary fileds are filled
                String title = etTitle.getText().toString();
                String author = etAuthor.getText().toString();
                String keywords = etKeywords.getText().toString();
                String des = etDes.getText().toString();

                if(title.isEmpty() || author.isEmpty() || keywords.isEmpty()){

                    Toast.makeText(MyBooksDescriptionActivity.this,
                            "Please enter all fields", Toast.LENGTH_SHORT).show();

                }else{

                    //otherwise update the object

                    ApplicationClass.userBooks.get(index).setTitle(title);
                    ApplicationClass.userBooks.get(index).setAuthor(author);
                    ApplicationClass.userBooks.get(index).setKeywords(keywords);
                    ApplicationClass.userBooks.get(index).setDescription(des);
                    Backendless.Persistence.of(Book.class).save(ApplicationClass.userBooks.get(index), new AsyncCallback<Book>() {
                        @Override
                        public void handleResponse(Book response) {

                            Toast.makeText(MyBooksDescriptionActivity.this, "Updated succesfully"
                                    , Toast.LENGTH_SHORT).show();

                            MyBooksDescriptionActivity.this.finish();

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(MyBooksDescriptionActivity.this, "Error: " + fault.getMessage()
                                    , Toast.LENGTH_SHORT).show();

                        }
                    });

                }

            }
        });
    }
}