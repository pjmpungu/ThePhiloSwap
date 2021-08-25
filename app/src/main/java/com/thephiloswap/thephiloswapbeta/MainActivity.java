package com.thephiloswap.thephiloswapbeta;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.backendless.persistence.local.UserIdStorageFactory;

public class MainActivity extends AppCompatActivity {

    TextView tvCreate, tvReset;
    Button btnSignIn;
    EditText etMail, etPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = findViewById(R.id.btnSignIn);
        tvCreate = findViewById(R.id.tvNewAccount);
        tvReset = findViewById(R.id.tvPasswordReset);
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

                    showProgress(true);
                    //otherwise log in the user
                    Backendless.UserService.login(email, password, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {

                            ApplicationClass.user = response;
                            Intent intent = new Intent(MainActivity.this, HomeActivityFrag.class);
                            MainActivity.this.finish();
                            startActivity(intent);
                            showProgress(false);

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(MainActivity.this, "Error: " + fault.getMessage()
                                    , Toast.LENGTH_SHORT).show();
                            showProgress(false);

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
                            Intent intent = new Intent(MainActivity.this, HomeActivityFrag.class);
                            MainActivity.this.finish();
                            startActivity(intent);
                            showProgress(false);

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(MainActivity.this, "Error: " + fault.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            showProgress(false);

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

        //clicking on reset password sends a reset email

        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if email is empty ask user to enter en email
                String email = etMail.getText().toString();
                if(email.isEmpty()){

                    Toast.makeText(MainActivity.this, "Please enter a valid email address in the email box"
                            , Toast.LENGTH_SHORT).show();

                }else{

                    //make an alert dialog to confirm password reset
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked, reset password

                                    showProgress(true);
                                    Backendless.UserService.restorePassword( email, new AsyncCallback<Void>()
                                    {
                                        public void handleResponse( Void response )
                                        {
                                            Toast.makeText(MainActivity.this, "Reset link was sent to your email"
                                                    , Toast.LENGTH_SHORT).show();
                                            showProgress(false);
                                        }

                                        public void handleFault( BackendlessFault fault )
                                        {
                                            // password revovery failed, to get the error code call fault.getCode()

                                            Toast.makeText(MainActivity.this, "Error: " + fault.getMessage()
                                                    , Toast.LENGTH_SHORT).show();
                                            showProgress(false);
                                        }
                                    });

                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Confirm Password Reset").setPositiveButton("Confirm", dialogClickListener)
                            .setNegativeButton("Cancel", dialogClickListener).show();

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