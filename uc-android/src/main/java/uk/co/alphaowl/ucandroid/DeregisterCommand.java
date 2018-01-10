package uk.co.alphaowl.ucandroid;

import uk.co.alphaowl.uc.UCClient;
import uk.co.alphaowl.uc.exceptions.PlayerNotRegisteredException;

class DeregisterCommand implements IUCCommand {

    @Override
    public void execute(UCClient instance, UCClientService.IUCServiceListener listener) {
        try {
            instance.deregister();
        } catch (PlayerNotRegisteredException ex) {
            listener.onPlayerNotRegisteredExceptionCaught(ex);
        }
    }
}
