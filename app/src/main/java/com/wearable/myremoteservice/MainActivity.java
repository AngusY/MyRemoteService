package com.wearable.myremoteservice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    IMyInterface remoteService;
    RemoteConnection remoteConnection = null;

    class RemoteConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            remoteService = IMyInterface.Stub.asInterface((IBinder) service);
            Toast.makeText(MainActivity.this,
                    "Remote Service connected.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            remoteService = null;
            Toast.makeText(MainActivity.this,
                    "Remote Service disconnected.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize the service
        remoteConnection = new RemoteConnection();
        Intent intent = new Intent();
        intent.setClassName("com.wearable.myremoteservice",
                com.wearable.myremoteservice.MyService.class.getName());
        if (!bindService(intent, remoteConnection, BIND_AUTO_CREATE)) {
            Toast.makeText(MainActivity.this,
                    "Fail to bind the remote service.", Toast.LENGTH_LONG).show();
        }

        // handle UI
        Button calcBtn = (Button) findViewById(R.id.calc_button);
        final EditText valueEdit = (EditText) findViewById(R.id.value_editText);
        final TextView resultTV = (TextView) findViewById(R.id.result_textView);

        calcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value, result=0;
                try {
                    value = Integer.parseInt(valueEdit.getText().toString());
                    result = remoteService.square(value);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                resultTV.setText(new Integer(result).toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(remoteConnection);
        remoteConnection = null;
    }
}
