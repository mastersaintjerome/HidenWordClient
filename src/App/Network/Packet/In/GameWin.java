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
 * Packet receive after games win
 * @author Gaëtan
 */
public class GameWin implements PacketHandler{
    final private Logger logger;
    
    public GameWin(){
        logger = Logger.getLogger(SessionStarted.class.getName());
    }
            
    @Override
    public void handle(Client client, String packet) {
        String[] parts = packet.split(" ", 2);
        String session = parts[1];
        logger.log(Level.INFO, "Session : {0} WIN !", session);
        client.setGameRun(false);
        client.setPlayerGameState(Client.PlayerGameState.WIN);
    }

    @Override
    public String code() {
        return "GWIN";
    }
}