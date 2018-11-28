/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package App.Network.Packet.Out;

import App.Network.Client;

/**
 * Packet send when a client want to create a duel game room
 * @author Gaëtan Perrot, Barbaria
 */
public class CreateDuelGame {
    private final Client client;
    
    public CreateDuelGame(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "DUEL " + client;
    }
}
