package com.example.tb.waterdrop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Main2Activity extends AppCompatActivity {
    
    private RadioButton mRb1RadioButton;
    private RadioButton mRb3RadioButton;
    private RadioGroup mRgRadioGroup;
    private CubicTo mCubicCubicTo;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mRb1RadioButton = (RadioButton) findViewById(R.id.rb1);
        mRb3RadioButton = (RadioButton) findViewById(R.id.rb2);
        mRgRadioGroup = (RadioGroup) findViewById(R.id.rg);
        mCubicCubicTo = (CubicTo) findViewById(R.id.cubic);
        
        mRgRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.rb1){
                    mCubicCubicTo.setMode(true);
                }else if(checkedId==R.id.rb2){
                    mCubicCubicTo.setMode(false);
                }
            }
        });
    }
}
