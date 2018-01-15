package uk.co.alphaowl.uc.sample;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import uk.co.alphaowl.uc.exceptions.PlayerNotRegisteredException;
import uk.co.alphaowl.uc.exceptions.PlayerRegisteredException;
import uk.co.alphaowl.ucandroid.ClientNotInitialisedException;
import uk.co.alphaowl.ucandroid.UCClientService;

public class MainActivity extends AppCompatActivity implements UCClientService.IUCServiceListener {

    private String ip = "10.194.79.34";
    private int port = 28910;

    private UCClientService mService;
    private boolean mBound = false;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            UCClientService.UCClientServiceBinder binder =
                    (UCClientService.UCClientServiceBinder) service;

            mService = binder.getService();
            mService.setServiceListener(MainActivity.this);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText edtIp = findViewById(R.id.edt_ip);
        EditText edtName = findViewById(R.id.edt_name);

        Button btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(v -> {
            if (mBound) {
                try {
                    ip = edtIp.getText().toString();
                    String name = edtName.getText().toString();
                    mService.init(ip, port, -1);
                    mService.register(name);
                } catch (ClientNotInitialisedException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });

        Intent intent = new Intent(this, UCClientService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mBound)
            unbindService(conn);
    }

    @Override
    public void onIOExceptionCaught(IOException ex) {
        System.err.println(ex.getMessage());
    }

    @Override
    public void onPlayerRegisteredExceptionCaught(PlayerRegisteredException ex) {

    }

    @Override
    public void onPlayerNotRegisteredExceptionCaught(PlayerNotRegisteredException ex) {

    }

    @Override
    public void onPlayerRegistered() {
        Intent intent = new Intent(this, DpadActivity.class);
        startActivity(intent);
    }

    @Override
    public void onServerDisconnected() {

    }

    @Override
    public void onPlayerNotFound() {

    }

    @Override
    public void onServerFull() {

    }

    @Override
    public void invalidCmd() {
        Log.d("CMD", "INVALID CMD");
    }
}
