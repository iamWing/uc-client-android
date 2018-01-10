package uk.co.alphaowl.ucandroid;

public class ClientNotInitialisedException extends Exception {

    public ClientNotInitialisedException() {
        super("Client has not been initialised yet. "
                + "Call init(String, int, buffer) to initial the client.");
    }
}
