package com.example.mobiup;

import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class bluetooth extends AppCompatActivity  implements IDetected {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        getIntent();




       BluetoothConfig.with(this)
                .setBackgroundColor(Color.parseColor("#1E90FF"))
            .setPulseColor(Color.parseColor("#ffffff"))
            .setListener(this)
                .start();

}

    @Override
    public void onSelectedDevice(BluetoothDevice device) {

    }
}

