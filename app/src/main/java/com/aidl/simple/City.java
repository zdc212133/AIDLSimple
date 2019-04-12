package com.aidl.simple;

import android.os.Parcel;
import android.os.Parcelable;

public class City implements Parcelable {
    /**
     * 城市名
     */
    private String name;
    /**
     * 城市面积
     */
    private int areaSize;

    protected City(Parcel in) {
        name = in.readString();
        areaSize = in.readInt();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAreaSize() {
        return areaSize;
    }

    public void setAreaSize(int areaSize) {
        this.areaSize = areaSize;
    }


    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(areaSize);
    }

    public void readToParcel(Parcel parcel){
        name=parcel.readString();
        areaSize=parcel.readInt();
    }

    @Override
    public String toString() {
        return "{ name:"+name+",areaSize:"+areaSize+"}";
    }
}
