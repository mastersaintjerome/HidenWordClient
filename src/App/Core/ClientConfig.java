/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package App.Core;

/**
 *
 * @author Gaëtan
 */
public class ClientConfig {
    final private ServerConfig serverConfig;
    
    public ClientConfig(ServerConfig serverConfig){
        this.serverConfig = serverConfig;
    }
    
    public String serverHost(){
        return serverConfig.getHost();
    }
    
    public int serverPort(){
        return serverConfig.getPort();
    }
}
