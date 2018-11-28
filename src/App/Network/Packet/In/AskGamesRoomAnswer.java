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
 * @author Gaëtan Perrot, Barbaria
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
        String[] rooms_parts = message.split(",");
        for(int i = 0; i < rooms_parts.length;i++){
            if(rooms_parts[i].length() > 1){
                int id = Character.getNumericValue(rooms_parts[i].charAt(1));
                String[] rooms_players_parts = rooms_parts[i].split(" ");
                int playerNb = Character.getNumericValue(rooms_players_parts[1].charAt(0));
                client.addRooms(id, playerNb);
            }
        }
    }

    @Override
    public String code() {
        return "AGROOMA";
    }
}
