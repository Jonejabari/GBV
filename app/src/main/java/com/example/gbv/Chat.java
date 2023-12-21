package com.example.gbv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Chat extends AppCompatActivity {

    // initialize variables..
    EditText Phone, Message;
    Button BtnSent;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //assign variables
        Phone = findViewById(R.id.Phone);
        Message = findViewById(R.id.Message);
        BtnSent = findViewById(R.id.btnSent);
        back = findViewById(R.id.Back);

        //method to go back to dashboard..
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Chat.this, Dashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        BtnSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check condition on permission
                if (ContextCompat.checkSelfPermission(Chat.this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED){
                    //when permission granted
                    //create method
                    sendSMS();
                }else {
                    //when permission not granted
                    //request permission
                    ActivityCompat.requestPermissions(Chat.this, new String[]{Manifest.permission.SEND_SMS},100);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //check condition
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //permission is granted
            //call method
            sendSMS();
        }else {
            //when permission denied
            //display toast message
            Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSMS() {
        //get value to editText
        String phone = Phone.getText().toString();
        String message = Message.getText().toString();

        //check condition of string is empty or not
        if (!phone.isEmpty() && !message.isEmpty()){
            //initialize sms message
            SmsManager smsManager = SmsManager.getDefault();
            //send message
            smsManager.sendTextMessage(phone,null,message,null,null);
            //display toast message
            Toast.makeText(this, "SMS sent successfully", Toast.LENGTH_SHORT).show();
        }else {
            //when string is empty the display toast message
            Toast.makeText(this, "Please enter phone number and message", Toast.LENGTH_SHORT).show();
        }
    }
}
