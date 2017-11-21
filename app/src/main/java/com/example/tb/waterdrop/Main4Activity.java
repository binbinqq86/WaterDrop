package com.example.tb.waterdrop;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Main4Activity extends AppCompatActivity {
    private static final String TAG = "Main4Activity";
    private ViewPager vp;
    private List<View> list=new ArrayList<>();
    private float lastDis;
    private WaterAnim waterAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        vp=findViewById(R.id.vp);
        waterAnim=findViewById(R.id.water);
        for (int i = 0; i < 5; i++) {
            TextView tv=new TextView(Main4Activity.this);
            tv.setText(i+"===");
            list.add(tv);
        }
        vp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }
    
            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(list.get(position));
            }
    
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(list.get(position));
                return list.get(position);
            }
        });
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(positionOffset==0){
                    return;
                }
                Log.e(TAG, "onPageScrolled: "+lastDis+"$"+positionOffset );
                waterAnim.setDeltaDistance(lastDis,positionOffset);
                lastDis=positionOffset;
            }
    
            @Override
            public void onPageSelected(int position) {
        
            }
    
            @Override
            public void onPageScrollStateChanged(int state) {
        
            }
        });
    }
}
