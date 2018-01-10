package uk.co.alphaowl.ucandroid;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;

import uk.co.alphaowl.uc.IUCCallback;
import uk.co.alphaowl.uc.UCClient;

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

    /* interfaces */

    public interface IUCServiceListener {
        <T> void returnException(T ex);
    }

    /* method for clients */

    private IUCServiceListener mListener;
    private IUCCallback mCallback;

    private Thread mWorker;
    private UCConnectionRunnable mRunner;

    public synchronized void setUCServiceListener(IUCServiceListener listener) {
        mListener = listener;
    }

    public synchronized void setUCCallback(IUCCallback callback) {
        mCallback = callback;
    }

    public void init(String ip, int port, int bufferSize) {
        if (bufferSize < -1 || bufferSize == 0)
            throw new IllegalArgumentException(
                    "Invalid buffer size. Buffer size must not be < -1 or equals 0."
            );

        if (mRunner == null) {
            mRunner = new UCConnectionRunnable(ip, port, bufferSize);
            mRunner.updateListeners(mListener, mCallback);
            mWorker = new Thread(mRunner);
            mWorker.start();
        }
    }

    /* for run in background */

    class UCConnectionRunnable implements Runnable {

        private IUCServiceListener mListener;
        private IUCCallback mCallback;

        private String mIp;
        private int mPort;
        private int mBufferSize;

        UCConnectionRunnable(String ip, int port, int bufferSize) {
            mIp = ip;
            mPort = port;
            mBufferSize = bufferSize;
        }

        synchronized void updateListeners(IUCServiceListener listener, IUCCallback callback) {
            mListener = listener;
            mCallback = callback;
        }


        @Override
        public void run() {

            UCClient instance;

            try {
                instance = UCClient.init(mIp, mPort, mBufferSize, mCallback);
            } catch (IOException ex) {
                mListener.returnException(ex);
            }
        }
    }

}
