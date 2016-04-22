/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.colorcloud.wifichat;


import android.app.Activity;

import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;




import java.util.ArrayList;

/**
 * An activity that uses WiFi Direct APIs to discover and connect with available
 * devices. WiFi Direct APIs are asynchronous and rely on callback mechanism
 * using interfaces to notify the application of operation success or failure.
 * The application should also register a BroadcastReceiver for notification of
 * WiFi state related events.
 */
public class WiFiDirectActivity extends Activity {

    public static final String TAG = "PTP_Activity";
    
    SpreadWord word;
    boolean mHasFocus = false;
    private boolean retryChannel = false;
    Button Toggle,b1;
    TextView t1,t2;
    static TextView text;
    String KeyVal;






    public synchronized String readFromList(ArrayList list)
    {
        return list.toString();
    }

    public static void writeToTextView(String append)
    {
        text.append(append);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);   // statically draw two <fragment class=>
        Toggle =(Button)findViewById(R.id.Tg);
        text =(TextView) findViewById(R.id.tex);
        b1=(Button)findViewById(R.id.b1);
        t1=(EditText)findViewById(R.id.key);
        t2=(EditText)findViewById(R.id.value);

        writeToTextView("+apples+");

        ServiceCreator loud = new ServiceCreator(this);
        ServiceDiscovery discover = new ServiceDiscovery(this);
        writeToTextView(readFromList(discover.ServiceList));
        word = new SpreadWord(this);
        Toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //word.exec();
                    writeToTextView(readFromList(discover.ServiceList));
                    Log.e("word__","spread init "+word);
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                    Log.e("null","spread fail");
                }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                KeyVal=t1.getText().toString()+" "+t2.getText().toString();
                writeToTextView(KeyVal);
                t1.setText("");
                t2.setText("");
            }
        });





    }

    /** 
     *
     */
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }
    
	@Override
	public void onStop() {  // the activity is no long visible
		super.onStop();
		mHasFocus = false;
	}

    @Override
    public void onDestroy() {
    	super.onDestroy();

    }


}
