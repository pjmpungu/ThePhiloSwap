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
import com.backendless.messaging.MessageStatus;

import java.util.List;

public class RequestBookActivity extends AppCompatActivity {

    Button btnCancel, btnSwap;
    EditText etAddress, etPhone;
    BackendlessUser owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_book);

        btnCancel = findViewById(R.id.btnCancel);
        btnSwap = findViewById(R.id.btnConfirm);

        etAddress = findViewById(R.id.etAddy);
        etPhone = findViewById(R.id.etPhone);

        int index = getIntent().getIntExtra("index", 0);
        Book book = ApplicationClass.books.get(index);

        //first get the user object that represents the owner of the book

        Backendless.Persistence.of(BackendlessUser.class).findById(book.getOwnerObjectId(), new AsyncCallback<BackendlessUser>() {
            @Override

            public void handleResponse(BackendlessUser response) {
                owner = response;
            }

            @Override
            public void handleFault(BackendlessFault fault) {

                Toast.makeText(RequestBookActivity.this, "Error: " + fault.getMessage()
                        , Toast.LENGTH_SHORT).show();

            }
        });

        //when cancel button is clicked return to the home screen

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestBookActivity.this.finish();

            }
        });

        //when swap button is pressed, send an email

        btnSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address = etAddress.getText().toString();
                String phone = etPhone.getText().toString();

                if(address.isEmpty()){

                    Toast.makeText(RequestBookActivity.this, "Please enter a valid address"
                            , Toast.LENGTH_SHORT).show();

                }else{

                    Backendless.Messaging.sendTextEmail("Swap Request",
                            ApplicationClass.generateEmail(book, address, phone, (String) owner.getProperty("name")), owner.getEmail(), new AsyncCallback<MessageStatus>() {
                                @Override
                                public void handleResponse(MessageStatus response) {


                                    //remove the book from the list and database once the email has been sent

                                    Backendless.Data.of(Book.class).remove(book, new AsyncCallback<Long>() {
                                        @Override
                                        public void handleResponse(Long response) {

                                            ApplicationClass.books.remove(index);
                                            RequestBookActivity.this.finish();
                                            Toast.makeText(RequestBookActivity.this, "Your request has been sent"
                                                    , Toast.LENGTH_SHORT).show();

                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {

                                            Toast.makeText(RequestBookActivity.this, "Your request has been sent"
                                                    , Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {

                                    Toast.makeText(RequestBookActivity.this, "Error: " + fault.getMessage()
                                            , Toast.LENGTH_SHORT).show();

                                }
                            });

                }

            }
        });
    }
}