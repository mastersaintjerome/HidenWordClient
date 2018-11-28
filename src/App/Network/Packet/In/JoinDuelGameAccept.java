/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package App.Network.Packet.In;

import App.Network.Client;
import App.Network.Packet.Out.StartDuelGame;
import App.Network.Packet.PacketHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Packet receive after join duel game
 * @author Gaëtan Perrot, Barbaria
 */
public class JoinDuelGameAccept implements PacketHandler{
    final private Logger logger;
    
    public JoinDuelGameAccept() {
        logger = Logger.getLogger(SessionStarted.class.getName());
    }
            
    @Override
    public void handle(Client client, String packet) {
        String[] parts = packet.split(" ", 2);
        String pseudo = parts[1];
        logger.log(Level.INFO, "Pseudo : {0}", pseudo);
        client.setPseudo(pseudo);
        client.setGameRun(true);
        client.setPlayerGameState(Client.PlayerGameState.RUN);
        StartDuelGame  StartDuelGame = new StartDuelGame(client);
        client.write(StartDuelGame);
    }

    @Override
    public String code() {
        return "DUELAJ";
    }
}

        //client.setGameRun(true);
        //client.setPlayerGameState(Client.PlayerGameState.RUN);