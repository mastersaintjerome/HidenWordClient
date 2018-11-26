/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package App.Network.Packet.Out;

import App.Network.Client;

/**
 * Packet send when a client send a char
 * @author Gaëtan
 */
public class GameTurnCharSend {
    private final Client client;
    private final char c;
    
    public GameTurnCharSend(Client client, char c) {
        this.client = client;
        this.c = c;
    }

    @Override
    public String toString() {
        return "GTCR " + c;
    }
}
