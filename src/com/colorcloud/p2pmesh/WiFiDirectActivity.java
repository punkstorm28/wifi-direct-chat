package com.colorcloud.p2pmesh;


import android.app.Activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



import java.util.ArrayList;


public class WiFiDirectActivity extends Activity {

    public static final String TAG = "PTP_Activity";
    LocationManage locationFinder;
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
        text.setText(append);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);   // statically draw two <fragment class=>
        WiFiServiceDiscovery.makeList();
        Discoverer=new WiFiServiceDiscovery(this);


        Toggle = (Button) findViewById(R.id.Tg);
        Add = (Button) findViewById(R.id.b1);

        text = (TextView) findViewById(R.id.tex);
        message = (EditText) findViewById(R.id.key);


        //new LongOperation().execute();
        ServiceCreator loud = new ServiceCreator(this);



        Thread networkMonitorThread = new Thread(new networkMonitor());
        networkMonitorThread.start();

        ServiceDiscovery discover = new ServiceDiscovery(this);
        writeToTextView(readFromList(discover.ServiceList));

        word = new SpreadWord(this);
        Toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    word.exec();
                   // writeToTextView(readFromList(discover.ServiceList));
                   // Log.e("word__", "spread init " + word);
                   // int dBm = getSignalStrength();
                    String loction=null;
                    if ((loction=getLocation())!=null)
                        Discoverer.registerMessage(loction);

                  else
                        Discoverer.registerMessage("SOS2133");
                    Log.e("location",loction);

                    writeToTextView(loction);


                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Log.e("null", "spread fail");
                }
            }
        });
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Discoverer.registerMessage(message.getText().toString());
                    message.setText("");
                    Discoverer.discoverService();
                    writeToTextView(Discoverer.MessageList.toString());


                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Log.e("null", "spread fail");
                }
            }
        });
    }



    public String getLocation()
    {
        String address = "";
        GPSService mGPSService = new GPSService(this);
        mGPSService.getLocation();
        double latitude = mGPSService.getLatitude();
        double longitude = mGPSService.getLongitude();
        return address = mGPSService.getLocationAddress();
    }


   public int getSignalStrength()
    {
        TelephonyManager telephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        CellInfoGsm cellinfogsm = (CellInfoGsm)telephonyManager.getAllCellInfo().get(0);
        CellSignalStrengthGsm cellSignalStrengthGsm = cellinfogsm.getCellSignalStrength();
        return cellSignalStrengthGsm.getDbm();
    }

    /*
    public int getSignalStrength()
    {
        TelephonyManager telephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        CellInfoWcdma cellinfogsm = (CellInfoWcdma) telephonyManager.getAllCellInfo().get(0);
        CellSignalStrengthWcdma cellSignalStrengthGsm = cellinfogsm.getCellSignalStrength();
        return cellSignalStrengthGsm.getDbm();
    }
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
        String Message=null;
        @Override
        protected String doInBackground(String... params) {

            Message=Discoverer.MessageList.toString();
             return Message;
        }

        @Override
        protected void onPostExecute(String result) {
            message.append(Message);

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


class networkMonitor implements Runnable {
    private int MAXIMUM_MESSAGES = 1;

    @Override
    public void run() {
        while (true) {
            try {

                int signalStrength = getSignalStrength();
                if (signalStrength >= (-100)) {
                    //Try sending the SOS messages/uploading their locations
                    if (MAXIMUM_MESSAGES == 3) {
                         SMSsender.sendMessage("8971738947","SOS",WiFiDirectActivity.this);
                        Log.i("SMS", "sending SMS");
                        MAXIMUM_MESSAGES--;

                    }

                }
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}



}

