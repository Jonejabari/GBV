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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    EditText inputEmail, inputName, inputPassword;
    TextView login;
    Button btnRegister;
    String emailPatten = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputEmail = findViewById(R.id.Username);
        inputPassword = findViewById(R.id.Password);
        login = findViewById(R.id.login);
        btnRegister = findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(this);


        //btnLogin = findViewById(R.id.btnLog);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformAuth();
            }
        });
    }

    private void PerformAuth() {

        String email = inputEmail.getText().toString();
        //String name = inputName.getText().toString();
        String password = inputPassword.getText().toString();

        /*database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");




        String email = inputEmail.getText().toString();
        String name = inputName.getText().toString();
        String password = inputPassword.getText().toString();

        GlobalVar globalVar = new GlobalVar(email,name,password);
        reference.child(name).setValue(globalVar);

        Toast.makeText(this, "You have successfully registered", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Register.this, MainActivity.class);
        //this line prevent to back to this activity
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/






        if (!email.matches(emailPatten)) {
            inputEmail.setError("Please enter correct email");
        } else if (password.isEmpty() || password.length() < 6) {
            inputPassword.setError("Enter proper password");
        } else {
            progressDialog.setMessage("Please wait while Registration...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(Register.this, "Registration is completed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Register.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(Register.this, MainActivity.class);

        //this line prevent to back to this activity
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}