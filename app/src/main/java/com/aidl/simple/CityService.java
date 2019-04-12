package com.aidl.simple;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * 使用过程和RemoteService一样
 */
public class CityService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    CityManager.Stub iBinder=new CityManager.Stub() {
        @Override
        public List<City> getCitys() throws RemoteException {
            return null;
        }

        @Override
        public int getCityAreaSize(String cityName) throws RemoteException {
            return 0;
        }

        @Override
        public void addCity(City city) throws RemoteException {

        }

        @Override
        public void removeCity(int cityName) throws RemoteException {

        }
    };
}
