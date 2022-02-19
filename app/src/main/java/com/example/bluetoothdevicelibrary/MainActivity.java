package com.example.bluetoothdevicelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bledevicelib.BluetoothConnectionService;
import com.example.bledevicelib.PrinterInfo;
import com.example.bledevicelib.Utils;

public class MainActivity extends AppCompatActivity
        implements BluetoothConnectionService.DataTransfer {

    BluetoothConnectionService bluetoothConnectionService;
    TextView txtRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtRead = findViewById(R.id.txtRead);
        bluetoothConnectionService = new BluetoothConnectionService(this, BluetoothAdapter.getDefaultAdapter());
        findViewById(R.id.button).setOnClickListener(view -> {
            clearData();
            PrinterInfo.showPrinterList(this,0,printerName -> {
                BluetoothDevice bluetoothDevice = PrinterInfo.getPrinter(printerName);
                bluetoothConnectionService.dataInitailizer(this);
                bluetoothConnectionService.startClient(bluetoothDevice);

            });
        });

        String test = "chetan";

        findViewById(R.id.selectMeter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearData();
                PrinterInfo.alertDialogSelectMeter(MainActivity.this,bluetoothConnectionService);
            }
        });

        findViewById(R.id.btnMeterMake).setOnClickListener(view -> {
            clearData();
            bluetoothConnectionService.write(Utils.METER_MAKE);
        });

        findViewById(R.id.btnMeterSerailNo).setOnClickListener(view -> {
            clearData();
            bluetoothConnectionService.write(Utils.METER_SERIAl_NUMBER);
        });

        findViewById(R.id.btnIntantKwh).setOnClickListener(view -> {
            clearData();
            bluetoothConnectionService.write(Utils.INSTANT_KWH);
        });

        findViewById(R.id.btnprofileData).setOnClickListener(view -> {
            clearData();
            bluetoothConnectionService.write(Utils.PROFILE_BILLING_DATA_1P);
        });

        findViewById(R.id.btnNonDLMS1).setOnClickListener(view -> {
            clearData();
            bluetoothConnectionService.write(Utils.LNT_NONDLMS_1P);
        });

        findViewById(R.id.btnNonDLMS3).setOnClickListener(view -> {
            clearData();
            bluetoothConnectionService.write(Utils.LNT_NONDLMS_3P);
        });
    }

    private void clearData(){
        txtRead.setText("");
    }


    @Override
    public void dataTransferSerial(String value) {
        txtRead.append(value);
    }

    @Override
    public void isConnected(boolean isConnected) {
        Toast.makeText(this, isConnected ? "Connected" : "DisConnected", Toast.LENGTH_SHORT).show();
    }
}