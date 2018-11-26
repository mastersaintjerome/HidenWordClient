/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package App.Network.Packet.Out;

import App.Core.Client.Client;
import App.Network.Packet.PacketRegistryHandler;

/**
 * Packet send when a client closed a session
 * @author Gaëtan
 */
public class SessionClosed {
    private final Client client;
    
    public SessionClosed(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return PacketRegistryHandler.SESSION_STOPPED;
    }
}
