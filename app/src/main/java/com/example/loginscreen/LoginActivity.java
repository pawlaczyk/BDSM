package com.example.loginscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;


public class LoginActivity extends Activity {
    UserDatabase db;

    EditText textUsername;
    EditText textUserPassword;
    CardView cardViewLogin;
    TextView textRegisterHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new UserDatabase(this);

        textUsername = (EditText) findViewById(R.id.textUsername);
        textUserPassword = (EditText) findViewById(R.id.textUserPassword);
        cardViewLogin = (CardView) findViewById(R.id.cardViewLogin); //funkcja logowania
        cardViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rawUsername = textUsername.getText().toString();
                UserData userData = UserData.getInstance();
                try {
                    userData.setHashUsername(rawUsername); //TODO
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String rawPassword = textUserPassword.getText().toString();
                userData.setDatabaseRawPassword(rawPassword); // TODO

                Boolean loginStatus = false;
                try {
                    loginStatus = db.login(rawUsername, rawPassword);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.i("LOGIN", "przed bazą danych");
                Log.i("LOGIN", "wartosc: " + loginStatus);
                if (loginStatus == true) {
                    // TODO nie moge sie dostac do swojej bazy
                    try {
                        userData.setHashUsername(rawUsername); //TODO jeszcze dla pewnosci kolejny raz nadpisuje
                        NoteDatabase.getInstance(LoginActivity.this, userData.getHashUsername()); // tu sie po raz pierwszy utworzy baza z haslem
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "Zalgowano pomyślnie", Toast.LENGTH_SHORT).show();
                    Log.i("Login", "Zalgowano pomyślnie: " + rawUsername);

                    Intent noteIntent = new Intent(LoginActivity.this, NoteActivity.class);
                    noteIntent.putExtra("username", userData.getHashUsername()); // TODO
                    startActivity(noteIntent);
                } else
                    Toast.makeText(getApplicationContext(), "Nieprawidłowy login lub hasło", Toast.LENGTH_SHORT).show();
                    Log.i("Login", "Login error for user: " + rawUsername);
            }
        });



        textRegisterHere = (TextView) findViewById(R.id.textRegisterHere);
        textRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, MainActivity.class); //przekierowanie do rejestracji
                Log.i("Activity", "Switch to MainActivity");
                startActivity(registerIntent);
            }
        });

    }
}
