package com.thephiloswap.thephiloswapbeta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.backendless.persistence.local.UserIdStorageFactory;

public class MainActivity extends AppCompatActivity {

    TextView tvCreate;
    Button btnSignIn;
    EditText etMail, etPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = findViewById(R.id.btnSignIn);
        tvCreate = findViewById(R.id.tvNewAccount);
        etMail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPassword);

        //sign in button takes the user to the home screen

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //first check to see if both fields are filled out

                String email = etMail.getText().toString();
                String password = etPass.getText().toString();

                if(email.isEmpty() || password.isEmpty()){

                    Toast.makeText(MainActivity.this, "Please enter all fields"
                            , Toast.LENGTH_SHORT).show();

                }else{

                    //otherwise log in the user
                    Backendless.UserService.login(email, password, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {

                            ApplicationClass.user = response;
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(MainActivity.this, "Error: " + fault.getMessage()
                                    , Toast.LENGTH_SHORT).show();

                        }
                    });

                }

            }
        });

        //checking to see if user is still logged in

        Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
            @Override
            public void handleResponse(Boolean response) {

                if(response){

                    String userobjectID = UserIdStorageFactory.instance().getStorage().get();

                    Backendless.Data.of(BackendlessUser.class).findById(userobjectID, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {

                            ApplicationClass.user = response;
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(MainActivity.this, "Error: " + fault.getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    });

                }

            }

            @Override
            public void handleFault(BackendlessFault fault) {

                Toast.makeText(MainActivity.this, "Error: " + fault.getMessage()
                        , Toast.LENGTH_SHORT).show();

            }
        });

        //clicking new account label takes user to new account screen

        tvCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, NewAccountActivity.class);
                startActivity(intent);

            }
        });

    }
}