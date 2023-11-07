package com.example.gbv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText inputEmail, inputPassword;
    Button btnLogin, btnRegister;
    TextView forget,  register;
    String emailPatten = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 123;
    public static String adrr;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "Myconts";
    private static final String Key_num = "number";
    private static final String Key_message = "message";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputEmail = findViewById(R.id.Username);
        inputPassword = findViewById(R.id.Password);
        register = findViewById(R.id.register);
        btnLogin = findViewById(R.id.btnLog);
        forget = findViewById(R.id.forget);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        //btnRegister = findViewById(R.id.btnRegister);


        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Forget.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin();

            }
        });
    }

    private void performLogin() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (!email.matches(emailPatten)) {
            inputEmail.setError("Please enter correct email");
        } else if (password.isEmpty() || password.length() < 6) {
            inputPassword.setError("Enter proper password");
        } else {
            progressDialog.setMessage("Please wait while Login...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(MainActivity.this, "Login is completed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(MainActivity.this, Dashboard.class);

        //this line prevent to back to this activity
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}


