package com.colorcloud.p2pmesh;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class WiFiServiceDiscovery {
    public static final String TAG = "TheWord";
    public static final int MAXIMUM_SERVICE_PER_DEVICE=10;
    // TXT RECORD properties
    public static final String TXTRECORD_PROP_AVAILABLE = "available";
    public static  String SERVICE_INSTANCE = "goodSammy";
    public static final String SERVICE_REG_TYPE = "_presence._tcp";
    public static final String Data = "DataString";

    // public static final int MESSAGE_READ = 0x400 + 1;
    // public static final int MY_HANDLE = 0x400 + 2;
    private WifiP2pManager manager;
    //static final int SERVER_PORT = 4545;
    private Channel channel;
    private BroadcastReceiver receiver = null;
    private WifiP2pDnsSdServiceRequest serviceRequest;


    static ArrayList<String> MessageList;


    /** Called when the activity is first created. */

    static void makeList()
    {
        MessageList=new ArrayList<String>();
    }
    WiFiServiceDiscovery(Context con)
    {
        manager=(WifiP2pManager) con.getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(con, con.getMainLooper(), null);
        clearServices();


       // discoverService();

        //this is the new part
        Thread refreshThread = new Thread(new UpdateThread());
        refreshThread.start();
        Thread ListMaintainer = new Thread(new ServiceMaintainer());
        ListMaintainer.start();
        //ends


    }




    /**
     * Registers a local service and then initiates a service discovery
     */

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void registerMessage(String Message)
    {
        this.SERVICE_INSTANCE=Message;
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



        //discoverService();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void removeMessage(String Message)
    {
        this.SERVICE_INSTANCE=Message;
       // Log.i("word","Service starter in");
        Map<String, String> record = new HashMap<String, String>();
        record.put(TXTRECORD_PROP_AVAILABLE, "visible");
        record.put(Data,"papaya");
        WifiP2pDnsSdServiceInfo service = WifiP2pDnsSdServiceInfo.newInstance(
                SERVICE_INSTANCE, SERVICE_REG_TYPE, record);



        manager.removeLocalService(channel, service, new ActionListener() {
            @Override
            public void onSuccess() {
                //Log.i("word","Service Started");

            }
            @Override
            public void onFailure(int error) {
             //   Log.i("word","Service failed");
            }
        });



        //discoverService();
    }
    public void clearServices( )
    {


        manager.clearLocalServices(channel, new ActionListener() {
            @Override
            public void onSuccess() {
                Log.i("clear","Services Cleared");
            }

            @Override
            public void onFailure(int reason) {

            }
        });

        //discoverService();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)


    public void discoverService() {
        /*
         * Register listeners for DNS-SD services. These are callbacks invoked
         * by the system when a service is actually discovered.
         */
        //Log.i("word","Service discovery time");
        manager.setDnsSdResponseListeners(channel,
                new DnsSdServiceResponseListener() {
                    @Override
                    public void onDnsSdServiceAvailable(String instanceName,
                                                        String registrationType, WifiP2pDevice srcDevice) {
                        // A service has been discovered. Is this our app?
                        //Log.i(TAG,"service Discovered non instance"+instanceName);

                            Log.i("word_","new Service actually Discovered");
                        if (!MessageList.contains(instanceName)) {
                            Log.i(TAG," new service Discovered"+instanceName);
                            MessageList.add(instanceName);
                            //Add it to your personal broadcast list
                            registerMessage(instanceName);

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
 public class UpdateThread implements Runnable{

     @Override
     public void run() {
         while (true)
         {
             discoverService();
             try {
                 Thread.sleep(5000);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }

     }
 }
    public class ServiceMaintainer implements Runnable{

        @Override
        public void run() {
            while (true)
            {

                try {
                    if(MessageList.size()>11) {
                        String message = MessageList.get(11);
                        removeMessage(message);
                    }

                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();}
                    catch(ArrayIndexOutOfBoundsException e)
                    {
                        //do nothing, this will happen until the first 10 spaces are filled.
                    }
                catch(NullPointerException e)
                {
                    //DO nothing
                }

            }

        }
    }



}