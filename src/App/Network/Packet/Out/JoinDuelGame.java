/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package App.Network.Packet.Out;

import App.Network.Client;

/**
 * Packet send when a client want to join a duel game room
 * @author Gaëtan
 */
public class JoinDuelGame {
    private final Client client;
    private final int id;
    
    public JoinDuelGame(Client client,int id) {
        this.client = client;
        this.id = id;
    }

    @Override
    public String toString() {
        return "DUELJ " + id;
    }
}
