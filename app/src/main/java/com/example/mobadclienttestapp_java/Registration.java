package com.example.mobadclienttestapp_java;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Registration extends AppCompatActivity {

    private final String email_backup="example@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        Intent intent = new Intent(this, MainActivity.class);

        EditText editText = findViewById(R.id.registrationEmail);

        Button register = findViewById(R.id.registerButton);

        register.setOnClickListener(view -> {
            String email = editText.getText().toString();
            if(email.isEmpty()){
                intent.putExtra("email",email_backup);
            }
            else{
                intent.putExtra("email",email);
            }
            startActivity(intent);
            finish();
        });
    }
}