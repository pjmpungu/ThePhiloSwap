package com.thephiloswap.thephiloswapbeta;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class MyAccountActivity extends HomeActivity{

    EditText etName, etEmail;
    Button btnReset, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_my_account, null, false);
        mDrawer.addView(contentView, 0);
        nvDrawer.setCheckedItem(R.id.my_account);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etMail);

        btnReset = findViewById(R.id.btnReset);
        btnSave = findViewById(R.id.btnUpdate);

        //set all the text boxes to the corresponding data for the user

        etEmail.setText(ApplicationClass.user.getEmail());
        etName.setText((String) ApplicationClass.user.getProperty("name"));

        //when reset button is clicked reset both text boxes

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etEmail.setText(ApplicationClass.user.getEmail());
                etName.setText((String) ApplicationClass.user.getProperty("name"));

            }
        });

        //when save button pressed save data

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etName.getText().toString();
                String mail = etEmail.getText().toString();

                if(name.isEmpty() || mail.isEmpty()){

                    Toast.makeText(MyAccountActivity.this, "Please enter all fields"
                            , Toast.LENGTH_SHORT).show();

                }else{

                    ApplicationClass.user.setEmail(mail);
                    ApplicationClass.user.setProperty("name", name);

                    showProgress(true);

                    Backendless.UserService.update( ApplicationClass.user, new AsyncCallback<BackendlessUser>()
                    {
                        public void handleResponse( BackendlessUser user )
                        {
                            Toast.makeText(MyAccountActivity.this, "Updated Succesfully"
                                    , Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }

                        public void handleFault( BackendlessFault fault )
                        {
                            Toast.makeText(MyAccountActivity.this, "Error: " + fault.getMessage()
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