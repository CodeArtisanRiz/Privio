package com.t3g.privio.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.t3g.privio.R;

public class AcActivity extends AppCompatActivity {
    Button lc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac);
        lc.findViewById(R.id.lc);
        lc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AcActivity.this, "Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}