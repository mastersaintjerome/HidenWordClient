package App.View;

import App.Network.Client;
import App.Core.ClientConfig;
import App.Core.ServerConfig;
import javafx.application.Application;
import javafx.stage.Stage;

/*
 * Interface du client.
 * @author Gaëtan Perrot, Barbaria
 */
public class ClientInterface extends Application {

	SceneManager manager;
 	
	public static void main(String[] args) {
            Application.launch(ClientInterface.class, args);
        }
    
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mot Mistere");
        primaryStage.setResizable(false);
        ServerConfig serverConfig = new ServerConfig("127.0.0.1",5000);
        ClientConfig config = new ClientConfig(serverConfig);
        Client client = new Client(config);
        manager = new SceneManager(client);
        manager.setStage(primaryStage);
        manager.init(primaryStage);
    }
}
