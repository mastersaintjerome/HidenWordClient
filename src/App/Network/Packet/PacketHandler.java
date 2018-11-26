/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package App.Network.Packet;

import App.Network.Client;

/**
 * Handle incoming packets
 * @author Gaëtan Perrot, Barbarian
 */
public interface PacketHandler {
    /**
     * Handle a received packet
     *
     * @param client The receiver client
     * @param packet The received packet
     */
    public void handle(Client client, String packet);
    
    /**
     * The handled packet code
     * @return the packet code
     */
    public String code();
}
