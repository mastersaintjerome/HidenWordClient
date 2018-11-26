/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package App.Network.Packet;

import App.Network.Client;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * All received packets and events will be mapped to a packet handler
 * @author Gaëtan Perrot, Barbarian
 */
public final class PacketRegistryHandler{

    /**
     * SESSION_STARTED packet code
     */
    final static public String SESSION_STARTED = "\0SESSION_STARTED\0";

    /**
     * SESSION_STOPPED packet code
     */
    final static public String SESSION_STOPPED = "\0SESSION_STOPPED\0";
    
    final static private Logger logger = Logger.getLogger(PacketRegistryHandler.class.getName());
    final private Map<String, PacketHandler> handlers = new HashMap<>();
    
    
    /**
     * Create the the registry with handlers
     * @param handlers
     */
    public PacketRegistryHandler(PacketHandler[] handlers) {
        for (PacketHandler handler : handlers) {
            register(handler);
        }
    }

    public void handle(Client client, String packet) {
        // Extract the code from the packet
        String code = packet.split(" ", 2)[0];

        if (!handlers.containsKey(code)) {
            throw new IllegalArgumentException("Invalid packet code " + code);
        }

        handlers.get(code).handle(client, packet);
    }
    
    /**
     * Register a new packet handler
     * @param handler the packet handler
     */
    public void register(PacketHandler handler) {
        handlers.put(handler.code(), handler);
    }
}