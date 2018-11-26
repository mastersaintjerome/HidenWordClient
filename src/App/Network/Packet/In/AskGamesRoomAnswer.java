/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package App.Network.Packet.In;

import App.Network.Client;
import App.Network.Packet.PacketHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Packet receive after ask games room
 * @author Gaëtan
 */
public class AskGamesRoomAnswer implements PacketHandler{
    final private Logger logger;
    
    public AskGamesRoomAnswer(){
        logger = Logger.getLogger(SessionStarted.class.getName());
    }
            
    @Override
    public void handle(Client client, String packet) {
        String[] parts = packet.split(" ", 2);
        String message = parts[1];
        logger.log(Level.INFO, "Games Rooom : {0}", message);
        /*
        * TODo 
        * Client Room
        */
    }

    @Override
    public String code() {
        return "AGROOMA";
    }
}
