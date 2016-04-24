package com.colorcloud.p2pmesh;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;
import java.util.ArrayList;

import java.util.Map;

/**
 * Created by vyomkeshjha on 21/04/16.
 */
public class ServiceDiscovery {
    // Instantiate a new DiscoveryListener
    String TAG ="Discover__";
    String SERVICE_TYPE ="_http._tcp.";
    String mServiceName ="DisMan";
    NsdManager mNsdManager;
    NsdManager.DiscoveryListener mDiscoveryListener;
    NsdManager.ResolveListener mResolveListener;
    NsdServiceInfo mService;
    ArrayList<String> ServiceList;
    Map<String,byte[]> Attributes;
    synchronized public int addToList(String newString)
    {
        ServiceList.add(newString);
        return ServiceList.size();
    }
    ServiceDiscovery(Context appContext)
    {
        //Attributes = new HashMap<String, String>();
        ServiceList =new ArrayList<String>();
        mNsdManager =(NsdManager) appContext.getSystemService(Context.NSD_SERVICE);
        Log.d(TAG, "start discovery?");
        mDiscoveryListener = new NsdManager.DiscoveryListener() {

            //  Called as soon as service discovery begins.
            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                // A service was found!  Do something with it.

                Log.d(TAG, "Service discovery success" +service);


                if(!ServiceList.contains(service.getServiceName()))
                {
                    addToList(service.getServiceName());



                }

                Log.v(TAG,ServiceList.toString());
        //WiFiDirectActivity.text.setText(service.getServiceName());
                if (service.getServiceType().equals(SERVICE_TYPE)) {

                    initializeResolveListener();
                    mNsdManager.resolveService(service, mResolveListener);
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                // When the network service is no longer available.
                // Internal bookkeeping code goes here.

                if(ServiceList.contains(service.getServiceName()))
                {
                    ServiceList.remove(service.getServiceName());



                }
                Log.e(TAG, "service lost" + service);

            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "Discovery stopped: " + serviceType);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }
        };

      mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD,mDiscoveryListener);
    }
    public void initializeResolveListener() {
        mResolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Called when the resolve fails.  Use the error code to debug.
                Log.e(TAG, "Resolve failed " + errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                mService= serviceInfo;
                Log.e(TAG, "Resolve Succeeded. " + mService);
                Log.e(TAG, "Resolve Succeeded. " + mService.getPort());
                Map<String,byte[]> kvMap=mService.getAttributes();
                Log.e(TAG, "Resolve kvMap. " + kvMap);


                //
                Log.d(TAG,"port: ");



            }
        };
    }
}
