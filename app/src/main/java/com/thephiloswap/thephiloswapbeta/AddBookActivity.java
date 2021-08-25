package com.thephiloswap.thephiloswapbeta;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.io.FileNotFoundException;

public class AddBookActivity extends AppCompatActivity {

    Button btnAdd;

    EditText etTitle, etAuthor, etDescription, etKeywords;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        etTitle = findViewById(R.id.etTitle);
        etAuthor = findViewById(R.id.etAuthor);
        etDescription = findViewById(R.id.etDescription);
        etKeywords = findViewById(R.id.etkeywords);
        btnAdd = findViewById(R.id.btnAdd);

        //onclick listener for adding a new book

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if both fields are empty then send a message to the user

                String title = etTitle.getText().toString();
                String author = etAuthor.getText().toString();
                String keyword = etKeywords.getText().toString();
                String des = etDescription.getText().toString();

                if (title.isEmpty() || author.isEmpty() || keyword.isEmpty()) {

                    Toast.makeText(AddBookActivity.this, "Please enter all fields"
                            , Toast.LENGTH_SHORT).show();

                } else {

                    Book book = new Book();
                    book.setAuthor(author);
                    book.setTitle(title);
                    book.setKeywords(keyword);
                    book.setDescription(des);
                    book.setOwnerObjectId(ApplicationClass.user.getObjectId());

                    showProgress(true);

                    Backendless.Persistence.save(book, new AsyncCallback<Book>() {
                        @Override
                        public void handleResponse(Book response) {
                            Toast.makeText(AddBookActivity.this, "Book added succesfully"
                                    , Toast.LENGTH_SHORT).show();
                            AddBookActivity.this.finish();
                            showProgress(false);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(AddBookActivity.this, "Error: " + fault.getMessage()
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