package com.example.bledevicelib;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class PrinterInfo {

    private final BluetoothDevice printer;
    private static PrinterInfo printerInfo;

    public PrinterInfo(Context context) {
        Pref.init(context);
        this.printer = getPrinter();
    }

    public static PrinterInfo with(Context context,String printerName) {
        printerInfo = new PrinterInfo(context,printerName);
        return printerInfo;
    }

    public PrinterInfo(Context context, String printerName) {
        Pref.init(context);
        printer = getPrinter(printerName);
    }

    private static BluetoothDevice getPrinter() {
        return getPrinter(Pref.getString(Pref.SAVED_DEVICE));
    }

    public static void showPrinterList(FragmentActivity activity, int activeColor, OnConnectPrinter onConnectPrinter) {
        showPrinterList(activity, activeColor, 0, onConnectPrinter);
    }

    public static void showPrinterList(FragmentActivity activity, int activeColor, int inactiveColor, OnConnectPrinter onConnectPrinter) {
        Pref.init(activity);
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        int activeColorResource = activeColor == 0 ? activeColor : ContextCompat.getColor(activity, activeColor);
        int inactiveColorResource = inactiveColor == 0 ? inactiveColor : ContextCompat.getColor(activity, inactiveColor);
        if (defaultAdapter != null && !defaultAdapter.getBondedDevices().isEmpty()) {
            FragmentManager fm = activity.getSupportFragmentManager();
            DeviceListFragment fragment = DeviceListFragment.newInstance();
            fragment.setDeviceList(defaultAdapter.getBondedDevices());
            fragment.setOnConnectPrinter(onConnectPrinter);
            fragment.setColorTheme(activeColorResource, inactiveColorResource);
            fragment.show(fm, "DeviceListFragment");
        } else {
            onConnectPrinter.onConnectPrinter("failed to connect printer");
        }
    }

    public static BluetoothDevice getPrinter(String printerName) {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice printer = null;
        if (defaultAdapter == null) return null;
        for (BluetoothDevice device : defaultAdapter.getBondedDevices()) {
            if (device.getName().equalsIgnoreCase(printerName)) {
                printer = device;
            }
        }
        return printer;
    }

    public interface OnConnectPrinter {
        void onConnectPrinter(String printerName);
    }
}
