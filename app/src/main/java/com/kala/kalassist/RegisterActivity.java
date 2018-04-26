package com.kala.kalassist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Jok3r on 29.12.2017.
 */

public class RegisterActivity extends AppCompatActivity implements DatabaseActions.DBRequestListener{

    EditText editTextEmail;
    EditText editTextUsername;
    EditText editTextPassword;
    FancyButton buttonRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail = (EditText)findViewById(R.id.editText_email);
        editTextUsername = (EditText)findViewById(R.id.editText_username);
        editTextPassword = (EditText)findViewById(R.id.editText_password);
        buttonRegister = (FancyButton)findViewById(R.id.button_register);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(editTextEmail.getText().toString(),
                                editTextUsername.getText().toString(),
                                editTextPassword.getText().toString());
            }
        });
    }

    @Override
    public void onDBRequestFinished(String response) {
        if (response.equals("REGISTER OK")) {
            Toast.makeText(this,"Erfolgreich Registriert !",Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        else if (response.equals("USER ALREADY EXISTS")){
            Toast.makeText(this,"Benutzer existiert bereits !",Toast.LENGTH_SHORT).show();

        }
        else
            Toast.makeText(this,"Fehler beim registrieren !",Toast.LENGTH_SHORT).show();
    }

    public void register(String email, String username, String password) {
        DatabaseActions.registerAccount(email,username,password,this,this);
    }

}
