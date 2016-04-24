package com.colorcloud.p2pmesh;


import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by vyomkeshjha on 20/04/16.
 */
public class SpreadWord {
    Context mContext ;
    int PORT =8080;
    DatagramSocket socket;
    String data="this is data";

    SpreadWord(Context main)
    {
        this.mContext=main;
        try {
            socket = new DatagramSocket(PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }
    void exec()
    {
        new NetIO().execute("");
    }

    InetAddress getBroadcastAddress() throws IOException {
        Log.e("word__","inside getBroadcast");

        WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        Log.e("word__","wifi ="+wifi.toString());
        DhcpInfo dhcp = wifi.getDhcpInfo();
        // handle null somehow

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }
    public void sendPacket() throws IOException {

        Log.e("word__","socket created "+socket.toString());
        socket.setBroadcast(true);
        InetAddress adr=getBroadcastAddress();
        Log.e("word__","adress  "+adr.toString());
        DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(),
                adr, PORT);
        Log.e("word__","packet send:  "+packet.getAddress().toString());
        try
        {socket.send(packet);}catch (Exception e){ Log.e("word__","send packet failed  "+adr.toString());e.printStackTrace();}
        DatagramPacket packetRc=null;
        try
        {byte[] buf = new byte[1024]; packetRc = new DatagramPacket(buf, buf.length); socket.receive(packetRc);}catch (Exception e){ Log.e("word__","Receive packet failed  "+adr.toString());e.printStackTrace();}
        Log.e("word__","the received packet is "+packetRc.getData().toString());
/*

// If you want to listen for a response ...
        byte[] buf = new byte[1024];
        DatagramPacket packet2 = new DatagramPacket(buf, buf.length);
        socket.receive(packet2);
        Log.w("data_","received"+packet2.getData().toString());
*/

    }


    private class NetIO extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            {
                try {

                    sendPacket();



                }  catch (IOException e) {
                    Log.e("word__","execution failed  ");

                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("word__","execution done  ");
        }

        @Override
        protected void onPreExecute() {
            Log.e("word__","execution started  ");

        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}
