package com.example.karokojnr.nadab_customer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karokojnr.nadab_customer.api.HotelService;
import com.example.karokojnr.nadab_customer.api.RetrofitInstance;
import com.example.karokojnr.nadab_customer.model.Hotel;
import com.example.karokojnr.nadab_customer.model.HotelRegister;
import com.example.karokojnr.nadab_customer.model.UserLogin;
import com.example.karokojnr.nadab_customer.utils.Constants;
import com.example.karokojnr.nadab_customer.utils.SharedPrefManager;
import com.example.karokojnr.nadab_customer.utils.utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mobileNumber, userName, userEmail,  password, passwordAgain;
    Button addCustomer;
    ProgressBar mLoading;
    ImageView ivImage;
    TextView ti_first, login, readTerms;
    CheckBox terms_conditions;

    private static final String TAG = "Profile";
    private static final int RESULT_LOAD_IMAGE = 1;
    private Uri selectedImage;
    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;
    private String filePath;
    private File file;
    private Context context;
    private static Animation shakeAnimation;
    private LinearLayout linearLayout;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_register );

        context = this;

        // Initialize all views
        linearLayout = (LinearLayout) findViewById(R.id
                .linear_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setDisplayShowHomeEnabled ( true );

        toolbar.setNavigationIcon(R.drawable.ic_arrow);
        toolbar.setNavigationOnClickListener ( new View.OnClickListener () {

            @Override
            public void onClick(View view) {

                // Your code
                finish ();
            }
        } );
        progressDialog = new ProgressDialog(this);
        ti_first = (TextView)findViewById ( R.id.ti_first );
        mLoading = (ProgressBar) findViewById(R.id.login_loading);
        mobileNumber = (EditText)findViewById ( R.id.mobileNumber );
        userName = (EditText)findViewById ( R.id.fullname );
        userEmail = (EditText)findViewById ( R.id.your_email );
        password = (EditText)findViewById ( R.id.password );
        passwordAgain = (EditText)findViewById ( R.id.passwordAgain );
        addCustomer = (Button)findViewById ( R.id.addCustomer );
        ivImage = (ImageView)findViewById ( R.id.ivImage );
        terms_conditions = (CheckBox) findViewById(R.id.terms_conditions);
        ti_first = (TextView)findViewById ( R.id.ti_first );
        login = (TextView) findViewById(R.id.already_user);
        login.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent i = new Intent ( getApplicationContext (), LoginActivity.class );
                startActivity ( i );
            }
        } );
        readTerms = (TextView)findViewById ( R.id.read_terms_conditions );
        readTerms.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent terms = new Intent ( getApplicationContext (), Terms.class );
                startActivity ( terms );
            }
        } );
        ivImage.setOnClickListener (  this );

        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(getApplicationContext (),
                R.anim.shake);

        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            login.setTextColor(csl);
        } catch (Exception e) {
        }

    }

    public void addCustomer(View view) {
        HotelService service = RetrofitInstance.getRetrofitInstance().create(HotelService.class);
        final UserLogin user = new UserLogin();

        String mmobileNumber = mobileNumber.getText().toString();
        String muserName = userName.getText().toString();
        String muserEmail = userEmail.getText().toString();
        String mpassword = password.getText().toString();
        String mpasswordAgain = passwordAgain.getText().toString();
        String mimage = ivImage.getDrawable ().toString ();


        // Pattern match for email id
        Pattern p = Pattern.compile ( Constants.regEx );
        Matcher m = p.matcher ( muserEmail );
        // Check if all strings are null or not
        if (muserName.equals ( "" ) || muserName.length () == 0 || mimage.equals ( "" ) || mimage.length () == 0 || muserEmail.equals ( "" ) || muserEmail.length () == 0 ||  mmobileNumber.equals ( "" ) || mmobileNumber.length () == 0 || mpassword.equals ( "" ) || mpassword.length () == 0 || mpasswordAgain.equals ( "" ) || mpasswordAgain.length () == 0) {
            linearLayout.startAnimation ( shakeAnimation );

            {
                Snackbar snackbar = Snackbar.make ( linearLayout, "All fields are required!", Snackbar.LENGTH_LONG ).setAction ( "RETRY", new View.OnClickListener () {
                    @Override
                    public void onClick(View view) {
                    }
                } );
                // Changing message text color
                snackbar.setActionTextColor ( Color.GREEN );

                // Changing action button text color
                View sbView = snackbar.getView ();
                TextView textView = (TextView) sbView.findViewById ( android.support.design.R.id.snackbar_text );
                textView.setTextColor ( Color.RED );

                snackbar.show ();

            }
        }

        // Check if email id valid or not
        else if (!m.find())
           /* new CustomToast().Show_Toast(getApplicationContext (), view,
                    "Your Email Id is Invalid.");*/ {
            Snackbar snackbar = Snackbar.make ( linearLayout, "Your Email Id is Invalid!", Snackbar.LENGTH_LONG ).setAction ( "RETRY", new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                }
            } );
            // Changing message text color
            snackbar.setActionTextColor ( Color.GREEN );

            // Changing action button text color
            View sbView = snackbar.getView ();
            TextView textView = (TextView) sbView.findViewById ( android.support.design.R.id.snackbar_text );
            textView.setTextColor ( Color.RED );

            snackbar.show ();
        }

        // Check if both password should be equal
        else if (!mpasswordAgain.equals(mpassword))
           /* new CustomToast().Show_Toast(getApplicationContext (), view,
                    "Both password doesn't match.");*/
        {
            Snackbar snackbar = Snackbar.make ( linearLayout, "Both password doesn't match!", Snackbar.LENGTH_LONG ).setAction ( "RETRY", new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                }
            } );
            // Changing message text color
            snackbar.setActionTextColor ( Color.GREEN );

            // Changing action button text color
            View sbView = snackbar.getView ();
            TextView textView = (TextView) sbView.findViewById ( android.support.design.R.id.snackbar_text );
            textView.setTextColor ( Color.RED );

            snackbar.show ();
        }

        // Make sure user should check Terms and Conditions checkbox
        else if (!terms_conditions.isChecked())
            /*new CustomToast().Show_Toast(getApplicationContext (), view,
                    "Please select Terms and Conditions.");*/
        {
            Snackbar snackbar = Snackbar.make ( linearLayout, "Please select Terms and Conditions!", Snackbar.LENGTH_LONG ).setAction ( "RETRY", new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                }
            } );
            // Changing message text color
            snackbar.setActionTextColor ( Color.GREEN );

            // Changing action button text color
            View sbView = snackbar.getView ();
            TextView textView = (TextView) sbView.findViewById ( android.support.design.R.id.snackbar_text );
            textView.setTextColor ( Color.RED );

            snackbar.show ();
        }


        // Else do signup or do your stuff
        else
            Toast.makeText(getApplicationContext (), "Signing up......", Toast.LENGTH_SHORT)
                    .show();

        //validating inputs

        if (TextUtils.isEmpty(muserName)) {
            userName.setError("Please enter your username");
            userName.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(muserEmail).matches()) {
            userEmail.setError("Enter a valid userEmail");
            userEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(mpassword)) {
            password.setError("Please enter your password");
            password.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mpasswordAgain)) {
            passwordAgain.setError("Please enter your password");
            passwordAgain.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mmobileNumber)) {
            mobileNumber.setError("Please enter a mobile number");
            mobileNumber.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(muserEmail)) {
            userName.setError("Please enter an email");
            userName.requestFocus();
            return;
        }

        showProgressDialogWithTitle ();

        String filePath = getRealPathFromURIPath(selectedImage, this);
        File file = new File(filePath);
        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("profile", file.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        RequestBody userEmail = RequestBody.create(MediaType.parse("text/plain"), muserEmail);
        RequestBody userName = RequestBody.create(MediaType.parse("text/plain"), muserName);
        RequestBody mobileNumber = RequestBody.create(MediaType.parse("text/plain"), mmobileNumber.trim());
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), mpassword);


        Call<UserLogin> call = service.addCustomer(fileToUpload, filename, userName, mobileNumber, userEmail, password);

        call.enqueue(new Callback<UserLogin>() {
            @Override
            public void onResponse(Call<UserLogin> call, Response<UserLogin> response) {
                if (response.isSuccessful()) {

                    if(response.body().isSuccess()){
                        // Persist to local storage
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body().getCustomer(), response.body().getToken());
                        // Start Home activity
                        goHome();
                        // Send FCM token to server
                         sendToken();
                    } else {
                        hideProgressDialogWithTitle();
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    hideProgressDialogWithTitle();
                    Toast.makeText(context, "Error logging in...", Toast.LENGTH_SHORT).show();
                }
                hideProgressDialogWithTitle();
            }
            @Override
            public void onFailure(Call<UserLogin> call, Throwable t) {
                hideProgressDialogWithTitle();
                Log.wtf(TAG, "onFailure: "+t.getMessage() );
                Toast.makeText(RegisterActivity.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });

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
                utils.sendRegistrationToServer(RegisterActivity.this, token);
            }
        });
    }



    private void goHome() {
        Intent intent = new Intent ( context, MainActivity.class );
        startActivity ( intent );
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public void onClick (View v){
        switch (v.getId ()){
            case R.id.ivImage:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult ( galleryIntent, RESULT_LOAD_IMAGE  );
                break;
        }


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            selectedImage = data.getData ();
            ivImage.setImageURI(selectedImage);
        }
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            selectedImage = data.getData();
            if(EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                filePath = getRealPathFromURIPath(selectedImage, RegisterActivity.this);
                file = new File (filePath);
                Log.d(TAG, "Filename " + file.getName());
            }else{
                EasyPermissions.requestPermissions(this, "This app needs access to your file storage so that it can read photos.", READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, RegisterActivity.this);
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

}
