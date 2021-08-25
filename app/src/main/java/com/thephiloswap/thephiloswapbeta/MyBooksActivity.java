package com.thephiloswap.thephiloswapbeta;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
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

        showProgress(true);
        Backendless.Data.of(Book.class).find(new AsyncCallback<List<Book>>(){
            @Override
            public void handleResponse( List<Book> foundBooks )
            {

                ApplicationClass.books = foundBooks;
                ApplicationClass.swapBooks.clear();
                ApplicationClass.userBooks.clear();
                //we have 2 array lists, one for books owned by the user, and one with books they can swap
                for(Book book: ApplicationClass.books) {

                    if (book.getOwnerObjectId().equals(ApplicationClass.user.getObjectId())) {

                        ApplicationClass.userBooks.add(book);

                    } else {

                        ApplicationClass.swapBooks.add(book);

                    }
                }
                myAdapter = new BookAdapter(MyBooksActivity.this, (ArrayList<Book>) ApplicationClass.userBooks, true);

                //set adapter for recylcer view

                recyclerView.setAdapter(myAdapter);
                layoutManager = new LinearLayoutManager(MyBooksActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                showProgress(false);

            }
            @Override
            public void handleFault( BackendlessFault fault )
            {

                Toast.makeText(MyBooksActivity.this, "Error: " + fault.getMessage()
                        , Toast.LENGTH_SHORT).show();
                showProgress(false);

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