package uk.co.alphaowl.uc.sample;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import uk.co.alphaowl.uc.exceptions.PlayerNotRegisteredException;
import uk.co.alphaowl.uc.exceptions.PlayerRegisteredException;
import uk.co.alphaowl.ucandroid.ClientNotInitialisedException;
import uk.co.alphaowl.ucandroid.UCClientService;

public class DpadActivity extends AppCompatActivity implements UCClientService.IUCServiceListener {

    private UCClientService mService;

    private boolean mBound = false;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            UCClientService.UCClientServiceBinder binder =
                    (UCClientService.UCClientServiceBinder) service;
            mService = binder.getService();
            mService.setServiceListener(DpadActivity.this);
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
        setContentView(R.layout.activity_dpad);

        Intent intent = new Intent(this, UCClientService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);

        Button btnUp = findViewById(R.id.upButton);
        Button btnDown = findViewById(R.id.downButton);
        Button btnL = findViewById(R.id.leftButton);
        Button btnR = findViewById(R.id.rightButton);
        Button btnA = findViewById(R.id.aButton);

        btnUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mBound) {
                    try {
                        mService.keyDown("forwards", null);
                    } catch (ClientNotInitialisedException ex) {

                    }
                }

                return false;
            }
        });

        btnDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mBound) {
                    try {
                        mService.keyDown("backwards",  null);
                    } catch (ClientNotInitialisedException ex) {

                    }
                }
                return false;
            }
        });

        btnL.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mBound) {
                    try {
                        mService.keyDown("left", null);
                    } catch (ClientNotInitialisedException ex) {

                    }
                }
                return false;
            }
        });

        btnR.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mBound) {
                    try {
                        mService.keyDown("right", null);
                    } catch (ClientNotInitialisedException ex) {

                    }
                }
                return false;
            }
        });

        btnA.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mBound) {
                    try {
                        mService.keyDown("jump", null);
                    } catch (ClientNotInitialisedException ex) {

                    }
                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mBound) {
            try {
                mService.deregister();
            } catch (ClientNotInitialisedException ex) {

            }
            unbindService(conn);
        }
    }

    @Override
    public void onIOExceptionCaught(IOException ex) {

    }

    @Override
    public void onPlayerRegisteredExceptionCaught(PlayerRegisteredException ex) {

    }

    @Override
    public void onPlayerNotRegisteredExceptionCaught(PlayerNotRegisteredException ex) {

    }

    @Override
    public void onPlayerRegistered() {

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

    }
}
