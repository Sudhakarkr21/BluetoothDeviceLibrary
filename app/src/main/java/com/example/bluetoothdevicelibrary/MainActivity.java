package com.example.bluetoothdevicelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bledevicelib.BluetoothConnectionService;
import com.example.bledevicelib.PrinterInfo;

public class MainActivity extends AppCompatActivity
        implements BluetoothConnectionService.DataTransfer {

    BluetoothConnectionService bluetoothConnectionService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluetoothConnectionService = new BluetoothConnectionService(this, BluetoothAdapter.getDefaultAdapter());
        findViewById(R.id.button).setOnClickListener(view -> {
            PrinterInfo.showPrinterList(this,0,printerName -> {
                BluetoothDevice bluetoothDevice = PrinterInfo.getPrinter(printerName);
                bluetoothConnectionService.dataInitailizer(this);
                bluetoothConnectionService.startClient(bluetoothDevice);

            });
        });
    }

    @Override
    public void dataTransferSerial(String value) {

    }

    @Override
    public void isConnected(boolean isConnected) {
        Toast.makeText(this, isConnected ? "Connected" : "DisConnected", Toast.LENGTH_SHORT).show();
    }
}