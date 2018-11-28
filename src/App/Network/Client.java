/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package App.Network;

import App.Core.ClientConfig;
import App.Network.Packet.In.AskGamesRoomAnswer;
import App.Network.Packet.In.CreateDuelGameAccept;
import App.Network.Packet.In.Disconnected;
import App.Network.Packet.In.GameLost;
import App.Network.Packet.In.GameTurnCharAccepted;
import App.Network.Packet.In.GameTurnCharRefused;
import App.Network.Packet.In.GameWin;
import App.Network.Packet.In.JoinDuelGameAccept;
import App.Network.Packet.In.JoinDuelGameRefuse;
import App.Network.Packet.In.NextTurn;
import App.Network.Packet.In.SessionStarted;
import App.Network.Packet.In.StartSoloGameAccept;
import App.Network.Packet.In.WaitTurn;
import App.Network.Packet.Out.CreateDuelGame;
import App.Network.Packet.Out.GameTurnCharSend;
import App.Network.Packet.Out.JoinDuelGame;
import App.Network.Packet.Out.SessionClosed;
import App.Network.Packet.Out.StartDuelGame;
import App.Network.Packet.Out.StartSoloGame;
import App.Network.Packet.PacketHandler;
import App.Network.Packet.PacketRegistryHandler;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
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
    
    volatile private String searchWord;
    volatile private String pseudo;
    volatile private Socket socket;
    volatile private boolean isTurn;
    volatile private boolean gameRun;
    volatile private PlayerGameState playerGameState;
    volatile private Map<Integer,Integer> rooms;

    public Client(ClientConfig config){
        logger = Logger.getLogger(Client.class.getName());
        this.config = config;
        isTurn = false;
        gameRun = false;
        rooms = new HashMap<>();
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
            new SessionStarted(),
            new StartSoloGameAccept(),
            new AskGamesRoomAnswer(),
            new CreateDuelGameAccept(),
            new Disconnected(),
            new GameLost(),
            new GameWin(),
            new GameTurnCharAccepted(),
            new GameTurnCharRefused(),
            new JoinDuelGameAccept(),
            new JoinDuelGameRefuse(),
            new NextTurn(),
            new WaitTurn()  
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
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot read from server", e);
        }
        String packet = builder.toString();

        logger.info("Recv << " + packet);

        return packet;
    }

   public void resetGame(){
        searchWord = "";
        isTurn = false;
        gameRun = false;
        playerGameState = PlayerGameState.RUN;
        rooms.clear();
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

    public boolean isRunning() {
        return running;
    }
    
    public void setGameRun(boolean gameRun) {
        this.gameRun = gameRun;
    }

    public PlayerGameState getPlayerGameState() {
        return playerGameState;
    }
    
    public boolean isWinning(){
        return playerGameState == PlayerGameState.WIN;
    }
    
    public boolean isLost(){
        return playerGameState == PlayerGameState.LOST;
    }

    public void setPlayerGameState(PlayerGameState playerGameState) {
        this.playerGameState = playerGameState;
    }
    
    public void clientSendChar(char c){
        this.write(new GameTurnCharSend(this,c));
    }
    
    public void clientStartSoloGame(){
        this.write(new StartSoloGame(this));
    }
    
    public void clientCreateDuelGame(){
        this.write(new CreateDuelGame(this));
    }
    
    public void clientJoinDuelGame(int id){
        this.write(new JoinDuelGame(this,id));
        this.write(new StartDuelGame(this));
    }
    
    public void addRooms(int id, int player){
        rooms.put(id, player);
    }
    
    public Map<Integer,Integer> getRooms(){
        return rooms;
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
        resetGame();  
        if(socket != null){
            this.write(new SessionClosed(this));
            try {
                socket.close();
            } catch (IOException e) {
                
            }
        }
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
