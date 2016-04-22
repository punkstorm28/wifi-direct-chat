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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



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
    Button Toggle;
    Button Add;
    static TextView text;
    EditText message;
    WiFiServiceDiscovery Discoverer;

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
        Discoverer=new WiFiServiceDiscovery(this);


        Toggle =(Button)findViewById(R.id.Tg);
        Add =(Button)findViewById(R.id.b1);

        text =(TextView) findViewById(R.id.tex);
        message =(EditText)findViewById(R.id.key);

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
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Discoverer.startRegistrationAndDiscovery(message.getText().toString());
                    message.setText("");
                    writeToTextView(Discoverer.MessageList.toString());

                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                    Log.e("null","spread fail");
                }
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

    /**
     * Remove all peers and clear all fields. This is called on
     * BroadcastReceiver receiving a state change event.
     */
    private class LongOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            for(int i=0;i<5;i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            TextView txt = (TextView) findViewById(R.id.tex);
            // txt.setText("Executed");
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
