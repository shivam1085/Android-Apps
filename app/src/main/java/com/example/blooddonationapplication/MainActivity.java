package com.example.blooddonationapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
     Intent intt1,intt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void btnclk1(View view) {
        intt1 = new Intent(MainActivity.this, Login.class);

        startActivity(intt1);
    }

    public void btnclk2(View View) {
        intt2 = new Intent(MainActivity.this, Register.class);
        startActivity(intt2);
    }

}
