package com.thephiloswap.thephiloswapbeta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class NewAccountActivity extends AppCompatActivity {

    Button btnCreateAccount, btnBack;
    EditText etName, etEmail, etPassword, etConPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        btnCreateAccount = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBackToLogin);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);
        etConPass = findViewById(R.id.etReenter);

        //when create account button clicked return to login screen for now

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String pass = etPassword.getText().toString();
                String conPass = etConPass.getText().toString();

                //first test to see if all fields have been entered

                if(name.isEmpty() || email.isEmpty() || pass.isEmpty() || conPass.isEmpty()){

                    Toast.makeText(NewAccountActivity.this, "Enter all fields",
                            Toast.LENGTH_SHORT).show();

                }else{

                    //check to make sure password is entered correctly

                    if(pass.equals(conPass)){

                        BackendlessUser user = new BackendlessUser();
                        user.setEmail(email);
                        user.setPassword(pass);
                        user.setProperty("name", name);

                        //register a new user

                        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                            @Override
                            public void handleResponse(BackendlessUser response) {
                                //if registration was successful, close this screen and tell the user
                                NewAccountActivity.this.finish();
                                Toast.makeText(NewAccountActivity.this, "Account created succesfully"
                                        , Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(NewAccountActivity.this, "Error: " + fault.getMessage()
                                        , Toast.LENGTH_SHORT).show();

                            }
                        });

                    }else{

                        //otherwise ask user to enter fields correctly
                        Toast.makeText(NewAccountActivity.this, "Error: Password doesn't match"
                                , Toast.LENGTH_SHORT).show();

                    }


                }

            }
        });

        //when back button pressed return to login screen

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NewAccountActivity.this.finish();

            }
        });
    }
}