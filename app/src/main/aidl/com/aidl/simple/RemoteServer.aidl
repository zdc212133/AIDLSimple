// RemoteServer.aidl
package com.aidl.simple;

// Declare any non-default types here with import statements

interface RemoteServer {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    String getResultMsg();

    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}
