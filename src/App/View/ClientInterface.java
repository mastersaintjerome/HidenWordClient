package App.View;

import App.Core.Client.Client;
import App.Core.ClientConfig;
import App.Core.ServerConfig;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Application;
import javafx.stage.Stage;

/*
 * Interface du client.
 * @author Gaëtan Perrot, Barbarian
 */
public class ClientInterface extends Application {

	SceneManager manager;
 	
	public static void main(String[] args) {
            Application.launch(ClientInterface.class, args);
        }
    
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mot Mistere");
        primaryStage.setResizable(false);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        ServerConfig serverConfig = new ServerConfig("127.0.0.1",5000);
        ClientConfig config = new ClientConfig(serverConfig);
        Client client = new Client(config);
        client.connect();
        executor.submit(client);
        manager = new SceneManager();
        manager.setStage(primaryStage);
        manager.init(primaryStage);
    }
}

