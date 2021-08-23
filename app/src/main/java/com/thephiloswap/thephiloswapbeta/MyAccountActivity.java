package com.thephiloswap.thephiloswapbeta;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

                    Backendless.UserService.update( ApplicationClass.user, new AsyncCallback<BackendlessUser>()
                    {
                        public void handleResponse( BackendlessUser user )
                        {
                            Toast.makeText(MyAccountActivity.this, "Updated Succesfully"
                                    , Toast.LENGTH_SHORT).show();
                        }

                        public void handleFault( BackendlessFault fault )
                        {
                            Toast.makeText(MyAccountActivity.this, "Error: " + fault.getMessage()
                                    , Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });

    }
}