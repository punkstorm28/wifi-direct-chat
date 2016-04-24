package com.colorcloud.p2pmesh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.widget.Button;


/**
 * Created by Kushagra Sharma on 23-04-2016.
 */
public class Locationchk extends Activity {
Button b1;
    private static int SPLASH_TIME_OUT=4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent i=new Intent(Locationchk.this,Login.class);
                startActivity(i);
                finish();
            }
        },SPLASH_TIME_OUT);

    }}





