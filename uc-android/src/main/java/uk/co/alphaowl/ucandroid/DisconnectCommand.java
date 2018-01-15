package uk.co.alphaowl.ucandroid;

import uk.co.alphaowl.uc.UCClient;

class DisconnectCommand implements IUCCommand {

    @Override
    public void execute(UCClient instance, UCClientService.IUCServiceListener listener) {
        instance.disconnect();
    }
}
