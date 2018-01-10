package uk.co.alphaowl.ucandroid;


import uk.co.alphaowl.uc.UCClient;
import uk.co.alphaowl.uc.exceptions.PlayerRegisteredException;

class RegisterCommand implements IUCCommand {

    private String playerName;

    public RegisterCommand(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void execute(UCClient instance, UCClientService.IUCServiceListener listener) {
        try {
            instance.register(playerName);
        } catch (PlayerRegisteredException ex) {
            listener.onPlayerRegisteredExceptionCaught(ex);
        }
    }
}
