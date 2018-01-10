package uk.co.alphaowl.ucandroid;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import uk.co.alphaowl.uc.IUCCallback;
import uk.co.alphaowl.uc.UCClient;
import uk.co.alphaowl.uc.exceptions.PlayerNotRegisteredException;
import uk.co.alphaowl.uc.exceptions.PlayerRegisteredException;

public class UCClientService extends Service {
    // Binder given to clients
    private final IBinder mBinder = new UCClientServiceBinder();

    public class UCClientServiceBinder extends Binder {
        public UCClientService getService() {
            // Return this instance of UCClientService so clients can call public methods
            return UCClientService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }

    /* interfaces */

    public interface IUCServiceListener extends IUCCallback {
        void onIOExceptionCaught(IOException ex);

        void onPlayerRegisteredExceptionCaught(PlayerRegisteredException ex);

        void onPlayerNotRegisteredExceptionCaught(PlayerNotRegisteredException ex);
    }

    /* method for clients */

    private IUCServiceListener mListener;

    private Thread mWorker;
    private UCConnectionRunnable mRunner;

    public synchronized void setServiceListener(IUCServiceListener listener) {
        mListener = listener;
    }

    public void init(String ip, int port, int bufferSize) {
        if (bufferSize < -1 || bufferSize == 0)
            throw new IllegalArgumentException(
                    "Invalid buffer size. Buffer size must not be < -1 or equals 0."
            );

        if (mRunner == null) {
            mRunner = new UCConnectionRunnable(ip, port, bufferSize);
            mRunner.setServiceListener(mListener);
            mWorker = new Thread(mRunner);
            mWorker.start();
        }
    }

    public void disconect() throws ClientNotInitialisedException {
        if (mRunner != null) {
            mRunner.queueCmd(new DisconnectCommand());
            mRunner = null;
            mWorker.interrupt();
        } else {
            throw new ClientNotInitialisedException();
        }
    }

    public void register(String playerName) throws ClientNotInitialisedException {
        if (mRunner != null) {
            mRunner.queueCmd(new RegisterCommand(playerName));
        } else {
            throw new ClientNotInitialisedException();
        }
    }

    /* for run in background */

    class UCConnectionRunnable implements Runnable {

        private IUCServiceListener mListener;

        private LinkedBlockingQueue<IUCCommand> cmdQueue = new LinkedBlockingQueue<>();

        private String mIp;
        private int mPort;
        private int mBufferSize;

        UCConnectionRunnable(String ip, int port, int bufferSize) {
            mIp = ip;
            mPort = port;
            mBufferSize = bufferSize;
        }

        synchronized void setServiceListener(IUCServiceListener listener) {
            mListener = listener;
        }

        void queueCmd(IUCCommand cmd) {
            cmdQueue.offer(cmd);
        }


        @Override
        public void run() {

            try {
                UCClient instance = UCClient.init(mIp, mPort, mBufferSize, mListener);

                while (!Thread.currentThread().isInterrupted()) {
                    IUCCommand cmd = cmdQueue.poll();

                    if (cmd != null)
                        cmd.execute(instance, mListener);
                }
            } catch (IOException ex) {
                mListener.onIOExceptionCaught(ex);
            }
        }
    }

}
