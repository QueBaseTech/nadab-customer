package com.example.karokojnr.nadab_customer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.karokojnr.nadab_customer.api.HotelService;
import com.example.karokojnr.nadab_customer.api.RetrofitInstance;
import com.example.karokojnr.nadab_customer.model.Customer;
import com.example.karokojnr.nadab_customer.model.CustomerResponse;
import com.example.karokojnr.nadab_customer.utils.Constants;
import com.example.karokojnr.nadab_customer.utils.CustomerSharedPreference;
import com.example.karokojnr.nadab_customer.utils.SharedPrefManager;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeProfile  extends AppCompatActivity implements View.OnClickListener{

    private EditText tv_user_name,tv_user_email,tv_message, tv_user_mobile;
    private SharedPreferences pref;
    private Button btn_change_password;
    private FloatingActionButton btn_logout;
    private EditText et_old_password,et_new_password;
    private AlertDialog dialog;
    private ProgressBar mLoading;
    ProgressDialog progressDialog;
    ImageView imageView;
    private Handler handler = new Handler ();
    private int progressStatus = 0;
    private String pUserName, pEmail, pMobileNumber, pUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setDisplayShowHomeEnabled ( true );
        toolbar.setNavigationIcon(R.drawable.ic_arrow);
        toolbar.setNavigationOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) { finish (); }
        } );


        tv_user_name = (EditText) findViewById(R.id.tv_user_name);
        tv_user_email = (EditText) findViewById(R.id.tv_user_email);
        tv_user_mobile = (EditText) findViewById ( R.id.tv_mobile );
        imageView = (ImageView)findViewById ( R.id.ivImage );
        progressDialog = new ProgressDialog (this);
        mLoading = (ProgressBar) findViewById(R.id.edit_loading);

        //getting the current user
        CustomerSharedPreference customer = SharedPrefManager.getInstance(this).getUser ();
        tv_user_name.setText(String.valueOf(customer.getUser_fullname ()));
        tv_user_email.setText(String.valueOf ( customer.getEmail ()));
        tv_user_mobile.setText(String.valueOf ( customer.getTv_mobile ()));
        Glide.with(this)
                .load(RetrofitInstance.BASE_URL+"images/uploads/customers/"+ String.valueOf(customer.getIvImage()))
                .into(imageView);
        pUserId = SharedPrefManager.getInstance(ChangeProfile.this).getUser().getId();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.changeprofile_menu, menu);
        return super.onCreateOptionsMenu ( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change:
                updateProfile ();
                Intent intent = new Intent ( this, ProfileActivity.class );
                startActivity ( intent );
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateProfile() {
        String userName = tv_user_name.getText ().toString ().trim ();
        String userEmail = tv_user_email.getText ().toString ().trim ();
        String userMobileNumber = tv_user_mobile.getText ().toString ().trim ();
        // MultipartBody.Part fileToUpload;
        HotelService service = RetrofitInstance.getRetrofitInstance ().create ( HotelService.class );
        //RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), image);
        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), userName.getBytes ().toString());
        RequestBody mobilenumber = RequestBody.create(MediaType.parse("text/plain"), userMobileNumber.getBytes ().toString());
        RequestBody emailaddress = RequestBody.create(MediaType.parse("text/plain"), userEmail);
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), pUserId);
        final String authToken = SharedPrefManager.getInstance(ChangeProfile.this).getToken();

        Call<CustomerResponse> call = service.profileEdit(authToken, pUserId, userName, userEmail, userMobileNumber);


        call.enqueue ( new Callback<CustomerResponse> () {
            @Override
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                if (response.isSuccessful ()) {
                    assert response.body () != null;
                    SharedPrefManager.getInstance(ChangeProfile.this).userLogin(response.body().getCustomer(), authToken);
                    Toast.makeText ( ChangeProfile.this, "Profile edited successfully...", Toast.LENGTH_SHORT ).show ();
                    finish();
                    Intent intent = new Intent ( getApplicationContext (), ProfileActivity.class );
                    startActivity ( intent );
                } else {
                    hideProgressDialogWithTitle ();
                    Toast.makeText ( ChangeProfile.this, "Error editing...", Toast.LENGTH_SHORT ).show ();
                }
            }

            @Override
            public void onFailure(Call<CustomerResponse> call, Throwable t) {
                hideProgressDialogWithTitle ();
                Toast.makeText ( ChangeProfile.this, "Something went wrong...Error message: " + t.getMessage (), Toast.LENGTH_SHORT ).show ();
            }
        } );
    }

    private void showProgressDialogWithTitle() {
        progressDialog.setTitle("Editing Profile");
        progressDialog.setMessage("Please Wait.....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.show();

        // Start Process Operation in a background thread
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    try{
                        // This is mock thread using sleep to show progress
                        Thread.sleep(200);
                        progressStatus += 5;
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    // Change percentage in the progress bar
                    handler.post(new Runnable() {
                        public void run() {
                            progressDialog.setProgress(progressStatus);
                        }
                    });
                }
                //hide Progressbar after finishing process
                hideProgressDialogWithTitle();
            }
        }).start();

    }

    // Method to hide/ dismiss Progress bar
    private void hideProgressDialogWithTitle() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.dismiss();
    }
    @Override
    public void onClick(View v) {

    }
}