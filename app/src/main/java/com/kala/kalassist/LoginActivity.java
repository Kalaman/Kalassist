package com.kala.kalassist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Kalaman on 29.12.2017.
 */

public class LoginActivity extends AppCompatActivity  implements DatabaseActions.DBRequestListener{

    EditText editTextUsername;
    EditText editTextPassword;
    FancyButton buttonLogin;
    FancyButton buttonRegisterNow;
    CheckBox checkBoxRememberMe;
    PrefManager prefManager;

    public static String savedUsername;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = (EditText)findViewById(R.id.editText_username);
        editTextPassword = (EditText)findViewById(R.id.editText_password);
        buttonLogin = (FancyButton)findViewById(R.id.button_login);
        buttonRegisterNow = (FancyButton)findViewById(R.id.button_register);
        checkBoxRememberMe = (CheckBox)findViewById(R.id.checkbox_remember);

        prefManager = new PrefManager(this);

        savedUsername = prefManager.readSharedString(PrefManager.KEY_USERNAME);
        String savedPassword = prefManager.readSharedString(PrefManager.KEY_PASSWORD);

        //Wenn es bereits gespeicherte Anmeldedaten gibt, nutze sie
        if (!savedUsername.equals("")) {
            editTextUsername.setText(savedUsername);
            editTextPassword.setText(savedPassword);

            editTextUsername.setSelection(editTextUsername.getText().length());
            editTextPassword.setSelection(editTextPassword.getText().length());

            checkBoxRememberMe.setChecked(true);
        }

        buttonRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegister = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intentRegister);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(editTextUsername.getText().toString(),editTextPassword.getText().toString());
            }
        });

        checkBoxRememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    saveCredentials();
                }
            }
        });

        editTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkBoxRememberMe.setChecked(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkBoxRememberMe.setChecked(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @Override
    public void onDBRequestFinished(String response) {
        if (response.equals("LOGIN OK")) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        else if (response.equals("LOGIN ERROR")){
            Toast.makeText(this,"Bitte Anmeldedaten überprüfen",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"Fehler beim anmelden",Toast.LENGTH_SHORT).show();
        }
    }

    public void login(String username, String password) {
        DatabaseActions.loginAccount(username,password,this,this);
    }

    /**
     * Speichert die Anmeldedaten lokal ab
     */
    public void saveCredentials () {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        prefManager.writeSharedString(PrefManager.KEY_USERNAME,username);
        prefManager.writeSharedString(PrefManager.KEY_PASSWORD,password);
    }
}
