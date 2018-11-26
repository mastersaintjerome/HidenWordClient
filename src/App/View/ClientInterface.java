package App.View;

import javafx.application.Application;
import javafx.stage.Stage;

/*
 * Interface du client.
 * @author Gaëtan Perrot, Barbarian
 */
public class ClientInterface extends Application {

	SceneManager manager = new SceneManager();
 	
	public static void main(String[] args) {
        Application.launch(ClientInterface.class, args);
    }
    
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mot Mistere");
        primaryStage.setResizable(false);
        
        manager.setStage(primaryStage);
        manager.init(primaryStage);
    }
}

