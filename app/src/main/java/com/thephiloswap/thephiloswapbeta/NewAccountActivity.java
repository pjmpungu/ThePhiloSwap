package com.thephiloswap.thephiloswapbeta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewAccountActivity extends AppCompatActivity {

    Button btnCreateAccount, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        btnCreateAccount = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBackToLogin);

        //when create account button clicked return to login screen for now

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NewAccountActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        //when back button pressed return to login screen

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NewAccountActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }
}