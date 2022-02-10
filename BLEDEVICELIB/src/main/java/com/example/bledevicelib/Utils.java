package com.example.bledevicelib;

public class Utils {

    public static final String INSTANT_KWH = "read obis=1.0.1.8.0.255:2 type=reg \n";
    public static final String METER_MAKE = "read obis=0.0.96.1.1.255:2 type=string \n";
    public static final String METER_SERIAl_NUMBER = "read obis=0.0.96.1.0.255:2 type=string \n";
    public static final String PROFILE_BILLING_DATA_1P = "read obis=1.0.98.1.0.255:2 type=pg index=5 count=4 bill=1 \n";
    public static final String PROFILE_BILLING_DATA_3P = "read obis=1.0.98.1.0.255:2 type=pg index=11 count=4 bill=1 \n";
    public static final String LNT_NONDLMS_1P = "set l=y c=32 s=1 auth=1 pwd=lnt1 iface=n1l \n";
    public static final String LNT_NONDLMS_3P = "set l=y c=32 s=1 auth=1 pwd=lnt1 iface=n3l \n";
}
