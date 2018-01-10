package uk.co.alphaowl.ucandroid;

import uk.co.alphaowl.uc.UCClient;
import uk.co.alphaowl.uc.exceptions.PlayerNotRegisteredException;

class JoystickCommand implements IUCCommand {

    private float x, y;

    public JoystickCommand(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void execute(UCClient instance, UCClientService.IUCServiceListener listener) {
        try {
            instance.joystick(x, y);
        } catch (PlayerNotRegisteredException ex) {
            listener.onPlayerNotRegisteredExceptionCaught(ex);
        }
    }
}
