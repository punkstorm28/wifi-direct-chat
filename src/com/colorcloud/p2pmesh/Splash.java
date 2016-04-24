package com.colorcloud.p2pmesh;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.colorcloud.wifichat.R;

/**
 * Created by Kushagra Sharma on 23-04-2016.
 */
public class Splash extends Activity {

    Button b1;
    private static int SPLASH_TIME_OUT=6000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final VideoView videovw=(VideoView)findViewById(R.id.videoView);
        videovw.setMediaController(new
                MediaController(Splash.this));
        Uri video1 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.load1);
        videovw.setVideoURI(video1);
        videovw.start();
        videovw.suspend();





        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent i=new Intent(Splash.this,Locationchk.class);
                startActivity(i);
                finish();
            }
        },SPLASH_TIME_OUT);
    }}





