/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package App.Network.Packet.Out;

import App.Core.Client.Client;

/**
 * Packet send when a client want to know the games rooms available
 * @author Gaëtan
 */
public class AskGamesRoom {
    private final Client client;
    
    public AskGamesRoom(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "AGROOM " + client;
    }
}
