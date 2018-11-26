/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package App.Network.Packet.In;

import App.Core.Client.Client;
import App.Network.Packet.PacketHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Packet receive after create a duel game
 * @author Gaëtan
 */
public class CreateDuelGameAccept implements PacketHandler{
    final private Logger logger;
    
    public CreateDuelGameAccept(){
        logger = Logger.getLogger(SessionStarted.class.getName());
    }
            
    @Override
    public void handle(Client client, String packet) {
        String[] parts = packet.split(" ", 2);
        String session = parts[1];
        logger.log(Level.INFO, "Games Rooom create by : {0}", session);
        /*
        * ToDo
        * GameRoom
        */
    }

    @Override
    public String code() {
        return "DUELA";
    }
}