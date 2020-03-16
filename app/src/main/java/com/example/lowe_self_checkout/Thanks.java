package com.example.lowe_self_checkout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Thanks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks);
        Intent i=getIntent();
        String s=i.getStringExtra("message");
        TextView t;
        t=(TextView)findViewById(R.id.thanksview);
        t.setText(s);

    }
}
