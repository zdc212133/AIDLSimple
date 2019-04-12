package com.aidl.simple;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.support.annotation.Nullable;

public class RemoteService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    RemoteServer.Stub iBinder=new RemoteServer.Stub() {
        @Override
        public String getResultMsg() throws RemoteException {
            return "getResult msg"+ Process.myPid()+Process.myUid();
        }

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }
    };

}
