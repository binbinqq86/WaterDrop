package com.example.tb.waterdrop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    
    private WaterDropView waterDropView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        waterDropView= (WaterDropView) findViewById(R.id.water);
    }
}
