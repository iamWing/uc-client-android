package uk.co.alphaowl.ucandroid;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class UCClientService extends Service {
    // Binder given to clients
    private final IBinder mBinder = new UCClientServiceBinder();

    public class UCClientServiceBinder extends Binder {
        UCClientService getService() {
            // Return this instance of UCClientService so clients can call public methods
            return UCClientService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }


    /* method for clients */

}
