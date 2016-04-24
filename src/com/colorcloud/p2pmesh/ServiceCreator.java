package com.colorcloud.p2pmesh;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by vyomkeshjha on 21/04/16.
 */
public class ServiceCreator {
    int port;
    Context appContext;
    String serviceName="SammyLuv";
    String serviceType="_http._tcp.";
    ServiceCreator(Context app)
    {   appContext =app;
        port =getFreePort();
        port =8654;
        registerService(port);
        startRegistrationListener();
    }
    public void registerService(int port) {
        // Create the NsdServiceInfo object, and populate it.
        NsdServiceInfo serviceInfo  = new NsdServiceInfo();

        // The name is subject to change based on conflicts
        // with other services advertised on the same network.

        serviceInfo.setServiceType(serviceType);
        serviceInfo.setServiceName(serviceName);
        Log.e("word__","service type before reg= "+serviceInfo.getServiceType());
        serviceInfo.setPort(port);
        serviceInfo.setAttribute("arr","124355");
        NsdManager mNsdManager =(NsdManager) appContext.getSystemService(Context.NSD_SERVICE);

        mNsdManager.registerService(
                serviceInfo, NsdManager.PROTOCOL_DNS_SD, startRegistrationListener());
        Log.e("word__","service manager= "+mNsdManager.toString());
        mNsdManager.toString();
        Log.e("word__","service type after reg= "+serviceInfo.getServiceType());
    }
    private int getFreePort()
    {
        int Port=34;

        // Initialize a server socket on the next available port.
        ServerSocket mServerSocket = null;
        try {
            mServerSocket = new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Store the chosen port.
        // Port = mServerSocket.getLocalPort();
        Port =4988;


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
            }


            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Registration failed!  Put debugging code here to determine why.
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0) {
                // Service has been unregistered.  This only happens when you call
                // NsdManager.unregisterService() and pass in this listener.
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Unregistration failed.  Put debugging code here to determine why.
            }
        };
        return RegistrationListener;
    }
    void destroyService()
    {

    }
}
