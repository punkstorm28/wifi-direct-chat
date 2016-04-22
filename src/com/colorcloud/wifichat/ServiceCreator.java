package com.colorcloud.wifichat;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;


import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * Created by vyomkeshjha on 21/04/16.
 */
public class ServiceCreator {
    int port;
    Context appContext;
    String serviceName="Asus";
    String serviceType="_http._tcp.";
    ServiceCreator(Context app)
    {   appContext =app;
        port =getFreePort();
        registerService(port);
        startRegistrationListener();
    }
    public void registerService(int port) {
        // Create the NsdServiceInfo object, and increase it's population by reproduction
        NsdServiceInfo serviceInfo  = new NsdServiceInfo();

        // The name is subject to change based on conflicts
        // with other services advertised on the same network

        serviceInfo.setServiceType(serviceType);
        serviceInfo.setServiceName(serviceName);
        //Log.e("word__","service type before reg= "+serviceInfo.getServiceType());
        String toByte ="123";
        serviceInfo.setAttribute("arr", String.valueOf(toByte.getBytes()));
        serviceInfo.setAttribute("par", String.valueOf(toByte.getBytes()));

        serviceInfo.setPort(port);
        NsdManager mNsdManager =(NsdManager) appContext.getSystemService(Context.NSD_SERVICE);

        mNsdManager.registerService(
                serviceInfo, NsdManager.PROTOCOL_DNS_SD, startRegistrationListener());
        Log.e("word__","service manager= "+mNsdManager.toString());
        mNsdManager.toString();
        Log.e("word__","service type after reg= "+serviceInfo.getServiceType());
        Log.e("word__","service attrib after reg= "+serviceInfo.getAttributes().keySet().toArray()[0]);

    }
    private int getFreePort()
    {
        int Port;

        // Initialize a server socket on the next available port.
        ServerSocket mServerSocket = null;
        try {
            mServerSocket = new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Store the chosen port.
        Port = mServerSocket.getLocalPort();



        return Port;
    }

    public NsdManager.RegistrationListener startRegistrationListener() {
        NsdManager.RegistrationListener RegistrationListener = new NsdManager.RegistrationListener() {
            String ServiceName;
            String Type;

            @Override
            public void onServiceRegistered(NsdServiceInfo ServiceInfo) {
                // Save the service name.  Android may have changed it in order to
                // resolve a conflict, so update the name you initially requested
                // with the name Android actually used.
              ServiceName = ServiceInfo.getServiceName();
                Type= ServiceInfo.getServiceType();

                Log.e("word__","Registration done "+ServiceName+" ++ "+Type);
                Log.e("word__","service attribs registered= "+ServiceInfo);
            }


            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {

            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0) {

            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
            }
        };
        return RegistrationListener;
    }
    void destroyService()
    {

    }
}
