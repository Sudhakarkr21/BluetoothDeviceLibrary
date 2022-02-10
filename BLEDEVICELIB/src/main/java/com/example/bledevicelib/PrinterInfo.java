package com.example.bledevicelib;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.nio.charset.StandardCharsets;

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

    public static void alertDialogSelectMeter(AppCompatActivity appCompatActivity,BluetoothConnectionService bluetoothConnectionService) {
        final CharSequence[] charSequence = new CharSequence[]{
                "LNT NON DLMS Single Phase",
                "LNT NON DLMS Three Phase",
                "LNT DLMS",
                "LNG DLMS",
                "GENUS DLMS",
                "SECURE DLMS",
                "VISION DLMS",
                "HPL DLMS"};
        new AlertDialog.Builder(appCompatActivity)
                .setTitle("Meter Make")
                .setCancelable(false)
                .setSingleChoiceItems(charSequence, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         dialogInterface.dismiss();
                        selectMeterMakePassword(charSequence[i].toString(),bluetoothConnectionService);
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    private static void selectMeterMakePassword(String meterMake, BluetoothConnectionService bluetoothConnectionService) {
        String meterpassword;
        switch (meterMake) {
            case "LNT NON DLMS Single Phase":
                meterpassword = "lnt1NONDLMS";
                break;
            case "LNT NON DLMS Three Phase":
                meterpassword = "lnt3NONDLMS";
                break;
            case "LNT DLMS":
                meterpassword = "lnt1";
                break;
            case "LNG DLMS":
                meterpassword = "11111111";
                break;
            case "GENUS DLMS":
                meterpassword = "1A2B3C4D";
                break;
            case "VISION DLMS":
                meterpassword = "Vison";
                break;

            case "HPL DLMS":
            case "HPL DLMS Three Phase":
                meterpassword = "1111111111111111";
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + meterMake);
        }

        if (meterpassword.trim().equals("lnt1NONDLMS") ||
                meterpassword.trim().contains("lnt3NONDLMS")) {
            byte[] bytes;
            if (meterpassword.equals("lnt3NONDLMS")) {
                bytes = "set l=y c=32 s=1 auth=1 pwd=lnt1 iface=n3l \n".getBytes(StandardCharsets.UTF_8);
            } else {
                bytes = "set l=y c=32 s=1 auth=1 pwd=lnt1 iface=n1l \n".getBytes(StandardCharsets.UTF_8);
            }
            bluetoothConnectionService.write(bytes);
            return;
        }

            String settingValue = "set l=y c=32 s=1 auth=1 pwd=" + meterpassword.trim() + " iface=hdlc \n";

            byte[] bytes = settingValue.getBytes(StandardCharsets.UTF_8);
            bluetoothConnectionService.write(bytes);


    }
}
