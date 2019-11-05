package in.walkin.nearx_example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Pattern;

import in.walkin.nearxsdk.NearX;

public class SettingsScreen extends AppCompatActivity implements View.OnClickListener {

    ImageView back;
    EditText et_mob_no;
    EditText et_name,et_key;
    Button btn_submit;
    NearX nearx;
    public static String Auth_Key = "";

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final String TAG = "SettingsScreen";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        nearx = new NearX(this);
        nearx.checkHandset();

        findViews();

        if (!checkPermissions()) {
            showSnackbar(getString(R.string.insufficient_permissions));
            requestPermissions();
        }

        et_mob_no.setText(nearx.getPhoneNumber());
        et_name.setText(nearx.getUserNmae());
        et_key.setText(nearx.getAuthKey());

        Auth_Key = nearx.getAuthKey();


        btn_submit.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    private void findViews() {
        back=findViewById(R.id.back);
        et_mob_no = findViewById(R.id.et_mobNo);
        et_key = findViewById(R.id.et_key);
        btn_submit = findViewById(R.id.btn_submit);
        et_name = findViewById(R.id.et_name);
    }

    private boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() != 10) {
                check = false;
                et_mob_no.setError("Not Valid Number");
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

    private String validationsRequest(){
        String mobileNumber = et_mob_no.getText().toString();
        String name = et_name.getText().toString();
        String auth_key = et_key.getText().toString();
        
        if(!isValidMobile(mobileNumber) ){
            return "Phone Number is not Valid";
        } else if(name.equals("")){
            et_name.setError("Enter Name");
            return "Enter Your Name";
        } else if(auth_key.equals("")){
            et_key.setError("Enter App-key");
            return "Enter App-Key";
        } else  return "success";

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_submit:

                String valid = validationsRequest();
                String mobileNumber = et_mob_no.getText().toString();
                String name = et_name.getText().toString();
                String authKey = et_key.getText().toString();

                if(valid.equals("success")) {

                    if (!checkPermissions()) {
                        showSnackbar(getString(R.string.insufficient_permissions));
                        requestPermissions();
                        return;
                    }else{
                        if (util.isNetworkAvailable(getApplicationContext())) {
                            nearx.saveUser(mobileNumber, name, authKey);
                            Toast.makeText(this, "You're set to go!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SettingsScreen.this, MainActivity.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(SettingsScreen.this, "No internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                
                break;
            case R.id.back:
                onBackPressed();
                break;
        }
    }


    private void showSnackbar(final String text) {
        View container = findViewById(android.R.id.content);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(SettingsScreen.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            ActivityCompat.requestPermissions(SettingsScreen.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted.");

//                NearX.initializeGeofences(this);

                Toast.makeText(this, "You're set to go!", Toast.LENGTH_SHORT).show();

            } else {
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

}
