package com.thephiloswap.thephiloswapbeta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class AddBookActivity extends AppCompatActivity {

    Button btnAdd;

    EditText etTitle;
    EditText etAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        etTitle = findViewById(R.id.etTitle);
        etAuthor = findViewById(R.id.etAuthor);
        btnAdd = findViewById(R.id.btnAdd);

        //onclick listener for adding a new book

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if both fields are empty then send a message to the user

                String title = etTitle.getText().toString();
                String author = etAuthor.getText().toString();

                if(title.isEmpty() || author.isEmpty()){

                    Toast.makeText(AddBookActivity.this, "Please enter all fields"
                            , Toast.LENGTH_SHORT).show();

                }else{

                    Book book = new Book();
                    book.setAuthor(author);
                    book.setTitle(title);
                    book.setOwnerEmail(ApplicationClass.user.getEmail());

                    Backendless.Persistence.save(book, new AsyncCallback<Book>() {
                        @Override
                        public void handleResponse(Book response) {
                            Toast.makeText(AddBookActivity.this, "Book added succesfully"
                                    , Toast.LENGTH_SHORT).show();
                            AddBookActivity.this.finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(AddBookActivity.this, "Error: " + fault.getMessage()
                                    , Toast.LENGTH_SHORT).show();

                        }
                    });

                }

            }
        });
    }
}