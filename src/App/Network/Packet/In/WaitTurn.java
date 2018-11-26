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
 * Packet receive when player have to wait for his turn (once)
 * @author Gaëtan
 */
public class WaitTurn implements PacketHandler{
    final private Logger logger;
    
    public WaitTurn(){
        logger = Logger.getLogger(SessionStarted.class.getName());
    }
            
    @Override
    public void handle(Client client, String packet) {
        String[] parts = packet.split(" ", 2);
        String searchWord = parts[1];
        logger.log(Level.INFO, "searchWord: {0}", searchWord);
        client.setSearchWord(searchWord);
        client.setIsTurn(false);
    }

    @Override
    public String code() {
        return "WAITT";
    }
}