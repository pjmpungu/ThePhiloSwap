package com.thephiloswap.thephiloswapbeta;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements BookAdapter.ItemClicked{

    Button btnAddBook;
    ImageButton ivRefresh;

    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnAddBook = findViewById(R.id.btnAddBook);

        ivRefresh = findViewById(R.id.ivRefresh);

        recyclerView = findViewById(R.id.list);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Setup drawer view

        drawerToggle = setupDrawerToggle();

        // Setup toggle to display hamburger icon with nice animation
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

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

    //in the implentation of the BookAdapter interface, if an item is clicked
    //then start the book description activity with the information on the book

    @Override
    public void onItemClicked(int index) {
        Intent intent = new Intent(HomeActivity.this, BookDescriptionActivity.class);
        intent.putExtra("book", index);
        startActivity(intent);

    }

    //following methods help set up the hamburger menu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        //selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE 1: Make sure to override the method with only a single `Bundle` argument
    // Note 2: Make sure you implement the correct `onPostCreate(Bundle savedInstanceState)` method.
    // There are 2 signatures and only `onPostCreate(Bundle state)` shows the hamburger icon.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }
}