/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package App.Network.Packet.Out;

import App.Core.Client.Client;

/**
 * Packet send when client start duel game
 * @author Gaëtan
 */
public class StartDuelGame {
    private final Client client;
    
    public StartDuelGame(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "SDUEL " + client;
    }
}
