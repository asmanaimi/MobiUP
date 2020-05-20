package com.example.mobiup;

import android.bluetooth.BluetoothDevice;

import java.io.Serializable;

public interface IDetected extends Serializable {

    public void onSelectedDevice(BluetoothDevice device);
}
