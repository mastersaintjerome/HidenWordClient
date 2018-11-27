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
 * Packet receive after char refused
 * @author Gaëtan
 */
public class GameTurnCharRefused implements PacketHandler{
    final private Logger logger;
    
    public GameTurnCharRefused(){
        logger = Logger.getLogger(SessionStarted.class.getName());
    }
            
    @Override
    public void handle(Client client, String packet) {
        String[] parts = packet.split(" ", 2);
        String charRefused = parts[1];
        logger.log(Level.INFO, "Char refused : {0}", charRefused);
    }

    @Override
    public String code() {
        return "GTCRR";
    }
}