package com.example.janar.smsreceiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.ParcelUuid;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;


public class SMSActivity extends ActionBarActivity {

    public static final String LOG_TAG = SMSReceiver.class.getName();

    public static final String uuid = "A55D25C2-DA5F-40B2-B8D9-B940BF39795C";

    //public static final String uuid = "00001101-0000-1000-8000-00805f9b34fb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsreceiver);

        try {
            sendViaBluetooth( "Sample Test");
        }catch(IOException ioe){
            Log.e(LOG_TAG, "IO Exception", ioe);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_smsreceiver, menu);
        return true;
    }

    protected void sendViaBluetooth(String msg) throws IOException {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        //BluetoothDevice device = adapter.getRemoteDevice("74:45:8A:A0:AC:47");
        BluetoothDevice device = adapter.getRemoteDevice("28:CF:E9:12:7D:84");
        System.out.println(device.getName());
        for(ParcelUuid uid: device.getUuids())
           System.out.println(uid);
            //BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"));
        BluetoothSocket socket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid));
        adapter.cancelDiscovery();
        System.out.println(socket.getRemoteDevice().getName());
        try {
            socket.connect();
        }catch(IOException ioe) {
            Log.e("", ioe.getMessage());
            try {
                Log.e("", "trying fallback...");

                socket = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(device, 1);
                socket.connect();

                Log.e("", "Connected");
            } catch (Exception e2) {
                Log.e("", "Couldn't establish Bluetooth connection!",e2);
            }
        }
        OutputStream stream = socket.getOutputStream();
        stream.write(msg.getBytes());
        stream.flush();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
