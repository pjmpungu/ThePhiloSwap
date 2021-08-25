package com.thephiloswap.thephiloswapbeta;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
        Book book = ApplicationClass.swapBooks.get(index);

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

                    showProgress(true);

                    Backendless.Messaging.sendTextEmail("Swap Request",
                            ApplicationClass.generateEmail(book, address, phone, (String) owner.getProperty("name")), owner.getEmail(), new AsyncCallback<MessageStatus>() {
                                @Override
                                public void handleResponse(MessageStatus response) {


                                    //remove the book from the list and database once the email has been sent

                                    Backendless.Data.of(Book.class).remove(book, new AsyncCallback<Long>() {
                                        @Override
                                        public void handleResponse(Long response) {

                                            ApplicationClass.swapBooks.remove(index);
                                            RequestBookActivity.this.finish();
                                            Toast.makeText(RequestBookActivity.this, "Your request has been sent"
                                                    , Toast.LENGTH_SHORT).show();
                                            showProgress(false);

                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {

                                            Toast.makeText(RequestBookActivity.this, "Your request has been sent"
                                                    , Toast.LENGTH_SHORT).show();
                                            showProgress(false);

                                        }
                                    });

                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {

                                    Toast.makeText(RequestBookActivity.this, "Error: " + fault.getMessage()
                                            , Toast.LENGTH_SHORT).show();
                                    showProgress(false);

                                }
                            });

                }

            }
        });
    }

    //method for progress bar
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        View content = findViewById(R.id.content);;
        View mProgressView = findViewById(R.id.login_progress);;
        TextView tvLoad = findViewById(R.id.tvLoad);;

        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            content.setVisibility(show ? View.GONE : View.VISIBLE);
            content.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    content.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            content.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}