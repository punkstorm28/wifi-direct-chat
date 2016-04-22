package com.colorcloud.wifichat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.DnsSdServiceResponseListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdTxtRecordListener;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Build;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * The main activity for the sample. This activity registers a local service and
 * perform discovery over Wi-Fi p2p network. It also hosts a couple of fragments
 * to manage chat operations. When the app is launched, the device publishes a
 * chat service and also tries to discover services published by other peers. On
 * selecting a peer published service, the app initiates a Wi-Fi P2P (Direct)
 * connection with the peer. On successful connection with a peer advertising
 * the same service, the app opens up sockets to initiate a chat.
 * {@code WiFiChatFragment} is then added to the the main activity which manages
 * the interface and messaging needs for a chat session.
 */
public class WiFiServiceDiscovery {
    public static final String TAG = "TheWord";
    // TXT RECORD properties
    public static final String TXTRECORD_PROP_AVAILABLE = "available";
    public static final String SERVICE_INSTANCE = "_wifidemotest";
    public static final String SERVICE_REG_TYPE = "_presence._tcp";
    public static final String Data = "DataString";
   // public static final int MESSAGE_READ = 0x400 + 1;
   // public static final int MY_HANDLE = 0x400 + 2;
    private WifiP2pManager manager;
    //static final int SERVER_PORT = 4545;
    private Channel channel;
    private BroadcastReceiver receiver = null;
    private WifiP2pDnsSdServiceRequest serviceRequest;



    /** Called when the activity is first created. */

       WiFiServiceDiscovery(Context con)
       {
        manager=(WifiP2pManager) con.getSystemService(Context.WIFI_P2P_SERVICE);
           channel = manager.initialize(con, con.getMainLooper(), null);
        startRegistrationAndDiscovery();


    }
    /**
     * Registers a local service and then initiates a service discovery
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void startRegistrationAndDiscovery()
    {
        Log.i("word","Service starter in");
        Map<String, String> record = new HashMap<String, String>();
        record.put(TXTRECORD_PROP_AVAILABLE, "visible");
        record.put(Data,"papaya");
        WifiP2pDnsSdServiceInfo service = WifiP2pDnsSdServiceInfo.newInstance(
                SERVICE_INSTANCE, SERVICE_REG_TYPE, record);
        manager.addLocalService(channel, service, new ActionListener() {
            @Override
            public void onSuccess() {
            Log.i("word","Service Started");

            }
            @Override
            public void onFailure(int error) {
                Log.i("word","Service failed");
            }
        });


        discoverService();
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)


    public void discoverService() {
        /*
         * Register listeners for DNS-SD services. These are callbacks invoked
         * by the system when a service is actually discovered.
         */
        Log.i("word","Service discovery time");
        manager.setDnsSdResponseListeners(channel,
                new DnsSdServiceResponseListener() {
                    @Override
                    public void onDnsSdServiceAvailable(String instanceName,
                                                        String registrationType, WifiP2pDevice srcDevice) {
                        // A service has been discovered. Is this our app?

                        if (instanceName.equalsIgnoreCase(SERVICE_INSTANCE)) {
                            Log.i(TAG,"service Discovered"+instanceName);

                            // update the UI and add the item the discovered
                            // device.

                        }
                    }
                }, new DnsSdTxtRecordListener() {
                    /**
                     * A new TXT record is available. Pick up the advertised
                     * buddy name.
                     */
                    @Override
                    public void onDnsSdTxtRecordAvailable(
                            String fullDomainName, Map<String, String> record,
                            WifiP2pDevice device) {
                        Log.d("word",
                                device.deviceName + " is "
                                        + record.get(TXTRECORD_PROP_AVAILABLE)+record.get(SERVICE_INSTANCE)+record.get(Data));

                    }
                });
        // After attaching listeners, create a service request and initiate
        // discovery.
        serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();

        manager.addServiceRequest(channel, serviceRequest,
                new ActionListener() {
                    @Override
                    public void onSuccess() {
                      //  Log.d("word","service success on success"+serviceRequest);
                    }
                    @Override
                    public void onFailure(int arg0) {
                        Log.d("word","service fail on fail");
                    }
                });
        manager.discoverServices(channel, new ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("word","discovery success= "+channel.toString());

            }
            @Override
            public void onFailure(int arg0) {

            }
        });
    }



}