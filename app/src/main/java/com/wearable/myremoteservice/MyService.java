package com.wearable.myremoteservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class MyService extends Service {

    IMyInterface.Stub mBinder;

    public MyService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new IMyInterface.Stub() {
            public int square(int value) throws RemoteException {
                return value * value;
            }
        };
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
