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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
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
    BookAdapter myAdapter;
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


        showProgress(true);

        Backendless.Data.of(Book.class).find(new AsyncCallback<List<Book>>(){
            @Override
            public void handleResponse(List<Book> foundBooks )
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
                myAdapter = new BookAdapter(HomeActivityFrag.this, (ArrayList<Book>) ApplicationClass.swapBooks, false);

                //set adapter for recylcer view

                recyclerView.setAdapter(myAdapter);
                layoutManager = new LinearLayoutManager(HomeActivityFrag.this);
                recyclerView.setLayoutManager(layoutManager);
                showProgress(false);

            }
            @Override
            public void handleFault( BackendlessFault fault )
            {

                Toast.makeText(HomeActivityFrag.this, "Error: " + fault.getMessage()
                        , Toast.LENGTH_SHORT).show();
                showProgress(false);

            }
        });

        //when you click the refresh button it updates the list

        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                        myAdapter.notifyDataSetChanged();
                        showProgress(false);

                    }
                    @Override
                    public void handleFault( BackendlessFault fault )
                    {

                        Toast.makeText(HomeActivityFrag.this, "Error: " + fault.getMessage()
                                , Toast.LENGTH_SHORT).show();
                        showProgress(false);

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

    //code for creating search bar


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }
}