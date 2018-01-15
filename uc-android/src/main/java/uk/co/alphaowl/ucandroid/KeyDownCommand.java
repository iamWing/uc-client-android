package uk.co.alphaowl.ucandroid;


import uk.co.alphaowl.uc.UCClient;
import uk.co.alphaowl.uc.exceptions.PlayerNotRegisteredException;

class KeyDownCommand implements IUCCommand {

    private String key, extra;

    public KeyDownCommand(String key) {
        this.key = key;
        this.extra = "";
    }

    public KeyDownCommand(String key, String extra) {
        this.key = key;
        this.extra = extra;
    }

    @Override
    public void execute(UCClient instance, UCClientService.IUCServiceListener listener) {
        try {
            instance.keyDown(key, extra);
        } catch (PlayerNotRegisteredException ex) {
            listener.onPlayerNotRegisteredExceptionCaught(ex);
        }
    }
}
