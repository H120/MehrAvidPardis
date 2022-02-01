package com.example.myapplication;

import static com.example.myapplication.NFCActivity.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editText_username , editText_password;
    Button button_login , button_singup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        clicklistener();

    }

    private void init(){

        editText_username = (EditText) findViewById(R.id.textview_username);
        editText_password = (EditText) findViewById(R.id.textview_password);
        button_login = (Button) findViewById(R.id.login_button);
        button_singup = (Button) findViewById(R.id.signup_button);
    }

    private void clicklistener() {

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (editText_username.getText().toString()){
                    case "a":{

                        switch (editText_password.getText().toString()){
                            case "a":{
                                Intent intent=new Intent(MainActivity.this,NFCActivity.class);
                                finish();
                                startActivity(intent);
                                break;
                            }
                        }break;
                    }
                }
            }
        });
    }
}