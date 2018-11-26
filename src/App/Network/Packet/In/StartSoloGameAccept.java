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
 * Packet receive after create a solo game
 * @author Gaëtan
 */
public class StartSoloGameAccept implements PacketHandler{
    final private Logger logger;
    
    public StartSoloGameAccept(){
        logger = Logger.getLogger(SessionStarted.class.getName());
    }
            
    @Override
    public void handle(Client client, String packet) {
        String[] parts = packet.split(" ", 2);
        String searchWord = parts[1];
        logger.log(Level.INFO, "SearchWord : {0}", searchWord);
        client.setSearchWord(searchWord);
        client.setPseudo("Player");
        client.setGameRun(true);
        client.setPlayerGameState(Client.PlayerGameState.RUN);
    }

    @Override
    public String code() {
        return "SOLOA";
    }
}
