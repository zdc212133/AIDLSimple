// CityManager.aidl
package com.aidl.simple;

// Declare any non-default types here with import statements
import com.aidl.simple.City;
// 在参数修饰符中，in表示客户端流向服务端
//out 表示服务端流向客户端

interface CityManager {
    List<City> getCitys();

    int getCityAreaSize(in String cityName);

    void addCity(in City city);

    void removeCity(in int cityName);

}
