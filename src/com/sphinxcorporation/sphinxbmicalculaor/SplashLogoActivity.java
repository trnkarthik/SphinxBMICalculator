package com.sphinxcorporation.sphinxbmicalculaor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
 
public class SplashLogoActivity extends Activity {
 
	 Animation mAnimation;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_logo);
        Thread splashTread = new Thread() {
            @Override
            public void run() {
 
            	
            	
            	 RelativeLayout mLayout = (RelativeLayout)findViewById(R.id.splashScreenLayout);
             	 TextView mTextView = (TextView) findViewById(R.id.sphinxLogoScreenTagLine );
                 mAnimation = new TranslateAnimation(100f, 0f, 0.0f, 0.0f);
                 mAnimation.setDuration(1000);
                 mTextView.setAnimation(mAnimation);
                 
                 TextView myText = (TextView) findViewById(R.id.sphinxLogoScreenTitle );

            	 Animation animation=new TranslateAnimation(-100f, 0f, 0.0f, 0.0f);
                 animation.setDuration(1000);
                 myText.startAnimation(animation);
                 
                 
                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
 
                    startActivity(new Intent(SplashLogoActivity.this, BMIActivity.class));
                            finish();
                }
            }
        };
        splashTread.start();
 
    }
 
}