# Universal Controller Client for Android

Universal Controller is a SDK that allows developers to use their customised 
controller on mobile devices or a tailor made hardware controller with unity. 
This client side library is built for Android by using a background service to 
keep the connection between the server and client alive. If you are just 
looking for a Java based library, you can have a look a the 
[Universal Controller Client for Java](https://github.com/iamWing/uc-client-java).

For the server side SDK, it can be found 
[here](https://github.com/iamWing/UniversalController_Server).

---

## Getting start

To get the connection made between the Android client and the server, and keep 
it alive through out different activities, you must retrieve the service in all 
the activities that invoke with the connection.

```java
private String ip = "";
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

...

@Override
    protected void onDestroy() {
        super.onDestroy();

        if (mBound)
            unbindService(conn);
    }
```

Also, as in most case the active activity will not be the same throughout your 
application, you will need to update the listener after intent to another 
activity by using `mService.setServiceListener(IUCServiceListener)`.

And depends on the performance of your server, you should set a time interval 
between each send action of the commands to server. As tested, 50ms should be 
shortest value that a normal PC can handles and not returning errors due to 
multiple commands received in an extremely short period of time too often.

For the commands that will be sent to the server, please refer to the 
document of the server side SDK.

---

## Version history

__v1.0.0__

- Implemented custom background service for socket connection.
- Implemented commands needed to communicate with Universal Controller Server.
- Included a sample application module.

---

## License & copyright

Copyright (c) 2018 Wing Chau & AlphaOwl.co.uk
<br />
All rights reserved.

This software may be modified and distributed under the terms
of the MIT license. See the LICENSE file for details.