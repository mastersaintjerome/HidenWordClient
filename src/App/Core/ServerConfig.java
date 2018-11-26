/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package App.Core;

/**
 *
 * @author Gaëtan Perrot, Barbarian
 */
public class ServerConfig {
    final private String host;
    final private int port;
    
    public ServerConfig(String host,int port){
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
