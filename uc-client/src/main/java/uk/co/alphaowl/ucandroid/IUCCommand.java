package uk.co.alphaowl.ucandroid;

import uk.co.alphaowl.uc.UCClient;

interface IUCCommand {
    void execute(UCClient instance, UCClientService.IUCServiceListener listener);
}
