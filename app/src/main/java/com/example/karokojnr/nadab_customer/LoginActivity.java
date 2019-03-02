package com.example.karokojnr.nadab_customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karokojnr.nadab_customer.api.HotelService;
import com.example.karokojnr.nadab_customer.api.RetrofitInstance;
import com.example.karokojnr.nadab_customer.model.UserLogin;
import com.example.karokojnr.nadab_customer.utils.Constants;
import com.example.karokojnr.nadab_customer.utils.SharedPrefManager;
import com.example.karokojnr.nadab_customer.utils.utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private Button btn_login;
    private ProgressBar mLoading;
    private static final String TAG = "LoginActivity";
    private TextView mRegister;
    private SharedPreferences mSharePrefs;
    private ProgressDialog progressDialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_login );

        progressDialog = new ProgressDialog(this);
        context = this;
        email = (EditText) findViewById ( R.id.et_email );
        password = (EditText) findViewById ( R.id.et_password );
        mRegister = (TextView) findViewById ( R.id.tv_register );
        btn_login = (Button)findViewById ( R.id.btn_login );
        mLoading = (ProgressBar) findViewById(R.id.login_loading);


        // Redirect home if is logged in
        if(SharedPrefManager.getInstance(this).isLoggedIn()) {
            sendToken();
            goHome();
            finish();
        }

        btn_login.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                //first getting the values
                String mEmail = email.getText().toString();
                String mPassword = password.getText().toString();

                //validating inputs
                if (TextUtils.isEmpty(mEmail)) {
                    email.setError("Please enter your username");
                    email.requestFocus();
                    return;
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                    email.setError("Enter a valid email");
                    email.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(mPassword)) {
                    password.setError("Please enter your password");
                    password.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(mPassword)) {
                    password.setError("Please enter your password");
                    password.requestFocus();
                    return;
                }
                showProgressDialogWithTitle ( );



                HotelService service = RetrofitInstance.getRetrofitInstance().create(HotelService.class);
                Call<UserLogin> call = service.login(mEmail, mPassword);

                call.enqueue(new Callback<UserLogin>() {
                    @Override
                    public void onResponse(Call<UserLogin> call, Response<UserLogin> response) {
                        if (response.isSuccessful()) {

                            if(response.body().isSuccess()){
                                // Persist to local storage
                                Log.wtf ( TAG, "onResponse: "+response.body().getCustomer()  );
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body().getCustomer(), response.body().getToken());
                                // Start Home activity
                                goHome();
                                // Send FCM token to server
                                 sendToken();
                            } else {
                                mLoading.setVisibility(View.INVISIBLE);
                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            mLoading.setVisibility(View.INVISIBLE);
                            Toast.makeText(context, "Error logging in...", Toast.LENGTH_SHORT).show();
                        }
                        hideProgressDialogWithTitle();
                    }
                    @Override
                    public void onFailure(Call<UserLogin> call, Throwable t) {
                        mLoading.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
            }
        } );
        mRegister.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( LoginActivity.this, RegisterActivity.class );
                startActivity ( intent );
            }
        } );

    }

    private void sendToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w("FCM_TOKEN", "getInstanceId failed", task.getException());
                    return;
                }

                // Get new Instance ID token
                String token = task.getResult().getToken();
                Log.d("FCM_TOKEN", "Token:: "+token);
                utils.sendRegistrationToServer(LoginActivity.this, token);
            }
        });
    }


    private void goHome() {
        Intent intent = new Intent ( LoginActivity.this, MainActivity.class );
        startActivity ( intent );
    }
    // Method to show Progress bar
    private void showProgressDialogWithTitle() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //Without this user can hide loader by tapping outside screen
        progressDialog.setCancelable(false);
        //Setting Title
        progressDialog.setTitle("Logging In");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

    }

    // Method to hide/ dismiss Progress bar
    private void hideProgressDialogWithTitle() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.dismiss();
    }
    //Styling for double press back button
    private static long back_pressed;
    @Override
    public void onBackPressed(){
        if (back_pressed + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
        }
        else{
            Toast.makeText(this, "Press once again to exit", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }

}

