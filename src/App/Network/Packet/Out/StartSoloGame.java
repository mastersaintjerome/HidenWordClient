/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package App.Network.Packet.Out;

import App.Core.Client.Client;

/**
 * Packet send when a client want to start a solo game
 * @author Gaëtan
 */
public class StartSoloGame {
    private final Client client;
    
    public StartSoloGame(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "SOLO " + client;
    }
}
