package com.example.mobiup;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.mobiup.controller.BluetoothController;
import com.example.mobiup.databinding.ActivityBluetoothBinding;


public class BluetoothDetection extends AppCompatActivity implements IBluetooth {
private ActivityBluetoothBinding bluetoothBinding;
    private BluetoothController controller;
    private Config config;
    private IDetected listener = null;
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bluetoothBinding = DataBindingUtil.setContentView(this, R.layout.activity_bluetooth);
        controller = new BluetoothController(this, this, receiver);
        config = getIntent().getParcelableExtra(Config.EXTRA_CONFIG);
        if (getIntent().hasExtra(Config.EXTRA_Listener))
            listener = (IDetected) getIntent().getSerializableExtra(Config.EXTRA_Listener);

        //Initializing data from config
       bluetoothBinding.title.setText(TextUtils.isEmpty(config.getTitle()) ? getString(R.string.title) : config.getTitle());
        bluetoothBinding.backgroundcolor.setBackgroundColor(config.getBackgroundcolor() == 0 ? getResources().getColor(R.color.backgroundcolor) : config.getBackgroundcolor());
        bluetoothBinding.pulsator.setColor(config.getPulsecolor() == 0 ? getResources().getColor(R.color.pulsecolor) : config.getPulsecolor());
        bluetoothBinding.pulsator.setAvators(config.getAvatars());

        // let us check bluetoothSupports or not if not then finish the activity
        if (controller.checkIfDeviceSupports()) {
            Toast.makeText(getApplicationContext(), "Device not Support Bluetooth", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        controller.setName();

        //Check the bluetooth persmission
        controller.enableAllPermission();

        //Start Animation
        startPulse();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BluetoothController.REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            controller.startDiscoveryDelay();
        } else
            finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == BluetoothController.MY_PERMISSIONS_REQUEST_Location) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                controller.enableBluetooth();
            } else {
                finish();
            }
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                bluetoothBinding.pulsator.clearedDetectedDevices();
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                displayAlert();
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                bluetoothBinding.pulsator.addDetecteddevice(device);

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                controller.startDiscovery();
            }
        }
    };
    private void displayAlert(){
        //mp.start();
        player = MediaPlayer.create(this, R.raw.clip);
        //player.setVolume(volume_level, volume_level);
        player.start();
        new AlertDialog.Builder(this)

                .setTitle("DISTANCE ")
                .setMessage("يجب عليك أن تترك مسافة الامان ")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        System.exit(0);
                        player.stop();

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)

                .show();




    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.stopDiscovery();
        unregisterReceiver(receiver);
    }

    @Override
    public void startPulse() {

        bluetoothBinding.pulsator.post(new Runnable() {
            @Override
            public void run() {
                bluetoothBinding.pulsator.setListener(listener);
                bluetoothBinding.pulsator.start();
            }
        });
    }

    @Override
    public void setName(final String displayname) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bluetoothBinding.name.setText(displayname);
            }
        });
    }

}
