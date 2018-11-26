/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package App.Core.Client;

import App.Core.ClientConfig;
import App.Network.Packet.PacketRegistryHandler;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class client, connect to the server
 * @author Gaëtan Perrot, Barbarian
 */
public class Client implements Runnable{
    final private Logger logger;
    final private ClientConfig config;
    private String sentence;
    private String pseudo;
    private Socket socket;
    volatile private boolean running  = false;
    private PacketRegistryHandler handler;
    
    final private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Future future;

    public Client(ClientConfig config){
        logger = Logger.getLogger(Client.class.getName());
        this.config = config;
    }

    public void connect() {
        socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(config.serverHost(), config.serverPort()));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot connect to server", e);
        }
        //setHandler(new AuthenticationHandler());
    }
    
    /**
     * Write a packet to the server
     */
    public void write(Object msg) {
        try {
            logger.info("Send >> " + msg);
            socket.getOutputStream().write((msg.toString() + "\n").getBytes());
            socket.getOutputStream().flush();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
    
        public void setHandler(PacketRegistryHandler handler) {
        this.handler = handler;
        executor.submit(this);
    }

    /**
     * Read the next packet from server
     */
    public String read() {
        StringBuilder builder = new StringBuilder(255);
        char c;
        try {
            while ((c = (char) socket.getInputStream().read()) > 0) {
                builder.append(c);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot read from server", e);
        }

        String packet = builder.toString();

        logger.info("Recv << " + packet);

        return packet;
    }

    public PacketRegistryHandler getHandler() {
        return handler;
    }
    
    /**
     * Stop the current client session
     */
    public void stop() {
        logger.info("Stopping...");
        running = false;
        executor.shutdown();
        try {
            socket.close();
        } catch (IOException e) {}
    }
    
    @Override
    public void run() {
        running = true;
        while (running) {
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
