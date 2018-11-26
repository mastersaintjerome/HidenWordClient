/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package App.Core.Client;

import App.Core.ClientConfig;
import App.Network.Packet.In.SessionStarted;
import App.Network.Packet.Out.SessionClosed;
import App.Network.Packet.PacketHandler;
import App.Network.Packet.PacketRegistryHandler;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class client, connect to the server
 * @author Gaëtan Perrot, Barbarian
 */
public class Client implements Runnable{
    public final static char END_OF_PACKET = '\n';
    final private Logger logger;
    final private ClientConfig config;
    volatile private boolean running  = false;
    private PacketRegistryHandler handler;
    
    private String searchWord;
    private String pseudo;
    private Socket socket;
    private boolean isTurn;
    private boolean gameRun;
    private PlayerGameState playerGameState;

    public Client(ClientConfig config){
        logger = Logger.getLogger(Client.class.getName());
        this.config = config;
        isTurn = false;
        gameRun = false;
    }
    
    public void connect() {
        socket = new Socket();
        try {
            int port = config.serverPort();
            String host = config.serverHost();
            socket.connect(new InetSocketAddress(host, port));
            logger.log(Level.INFO, "Connecting Client to " + host + " at port : {0}", port);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot connect to server", e);
        }
        setHandler(handler());
    }
    
    public PacketRegistryHandler handler(){
        PacketRegistryHandler packetRegistryHandler = new PacketRegistryHandler(new PacketHandler[]{
            // @todo Set the input packets here
            new SessionStarted()
        });
        return packetRegistryHandler;
    }
    
    /**
     * Write a packet to the server
     */
    public void write(Object msg) {
        try {
            logger.info("Send >> " + msg);
            socket.getOutputStream().write((msg.toString() + END_OF_PACKET).getBytes());
            socket.getOutputStream().flush();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
    
    public void setHandler(PacketRegistryHandler handler) {
        this.handler = handler;
    }

    /**
     * Read the next packet from server
     */
    public String read() {
        StringBuilder builder = new StringBuilder(255);
        char c;
        try {
            while ((c = (char) socket.getInputStream().read()) > 0 && (c != END_OF_PACKET)) {
                builder.append(c);
                logger.log(Level.INFO, "char : {0}", c);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot read from server", e);
        }
        String packet = builder.toString();

        logger.info("Recv << " + packet);

        return packet;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public boolean isIsTurn() {
        return isTurn;
    }

    public void setIsTurn(boolean isTurn) {
        this.isTurn = isTurn;
    }
    
    public PacketRegistryHandler getHandler() {
        return handler;
    }

    public boolean isGameRun() {
        return gameRun;
    }

    public void setGameRun(boolean gameRun) {
        this.gameRun = gameRun;
    }

    public PlayerGameState getPlayerGameState() {
        return playerGameState;
    }

    public void setPlayerGameState(PlayerGameState playerGameState) {
        this.playerGameState = playerGameState;
    }
    
    /**
     * Game State of the player
     */
    public enum PlayerGameState {

        /**
         * Win the game
         */
        WIN,

        /**
         * Lost the game
         */
        LOST,
        
        /**
         * Game still running for the player
         */
        RUN
    }
    
    /**
     * Stop the current client session
     */
    public void stop() {
        logger.info("Stopping...");
        this.write(new SessionClosed(this));
        running = false;
        try {
            socket.close();
        } catch (IOException e) {}
    }
    
    @Override
    public void run() {
        running = true;
        while (running) {
            logger.log(Level.INFO, "Start reading from Server");
            String packet = read();
            if (!packet.isEmpty()) {
                try {
                    handler.handle(this, packet);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error during handling packet " + packet, e);
                }
            }
        }
    } 
}
