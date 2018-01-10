package uk.co.alphaowl.ucandroid;

import uk.co.alphaowl.uc.UCClient;
import uk.co.alphaowl.uc.exceptions.PlayerNotRegisteredException;

class GyroCommand implements IUCCommand {

    private float x, y, z;

    public GyroCommand(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void execute(UCClient instance, UCClientService.IUCServiceListener listener) {
        try {
            instance.gyro(x, y, z);
        } catch (PlayerNotRegisteredException ex) {
            listener.onPlayerNotRegisteredExceptionCaught(ex);
        }
    }
}
