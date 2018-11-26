/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package App.View;

import App.Core.ClientRoom;
import App.Network.Client;
import java.util.ArrayList;
import java.util.Stack;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/*
 * Systeme permettant de gérer la partie graphique du jeu.
 * @author Gaëtan Perrot, Barbarian
 */

public class SceneManager {
	
	private ArrayList<KeyBoardButton> clavier = new ArrayList<KeyBoardButton>();
 	private Text hideWord, joueursPresents;
 	
 	Scene MenuScene, GameScene, RoomChooserScene, GameChooseScene, WaitingScene, ErrorScene, VictoryScene, DefeatScene;
 	
	Client client = new Client();
	ClientRoomController crc; 
	SceneBuilder builder;
	Stage stage;
	
	private ClientRoom currentRoom = null; 
	
	Scene currentScene;
	Stack<Scene> prevScenes = new Stack<Scene>();
   
    public void victoryButton(Stage stage) {
    	Scene scene = VictoryScene;
    	stage.setScene(scene);
		currentScene = scene;
    }
    
    public void defeatButton(Stage stage) {
    	Scene scene = DefeatScene;
    	stage.setScene(scene);
		currentScene = scene;
    }
    
    public void ResetKeyBoard() {
    	for(KeyBoardButton btn : getClavier()) {
    		btn.isUsed = false;
        	btn.disableButton(false);
    	}
    }

    public void returnLastScene(Stage stage) {
    	Scene scene = prevScenes.pop();
    	
    	if(scene != null) {
    		stage.setScene(scene);
    		currentScene = scene;
    	}
    }
    
    public void leaveRoom() 
    /**
     * Actions a effectuer lors d'une sortie de room
     */
    {
    	
    	
    	
    }
  
    public void enterScene() 
    /**
     * Actions a effectuer lorsqu'on entre dans une scene. 
     */
    {
    	if(currentScene == MenuScene || currentScene == ErrorScene) {
    		prevScenes.clear();
    	}
    	else if(currentScene == GameScene) {
    		ResetKeyBoard();
    		leaveRoom();
    	}else if(currentScene == WaitingScene) {
    		if(getCurrentRoom() != null) {
	    		//crc.connectToRoom(getCurrentRoom());
	    		
	    		//setCurrentRoom(crc.getRoom(getCurrentRoom().getId()));
	    		getCurrentRoom().addMember();
	    		if(getCurrentRoom().getMembers() < this.getCurrentRoom().getMembersMax()) {
	    			refreshWaitingText();
	    		}
	    		else {
	    	        setScene(getStage(), GameScene);
	    	        prevScenes.pop();
	    	        prevScenes.push(RoomChooserScene);
	    		}
    		}
    	}else {
    		Scene prev = prevScenes.pop();
    		if(prev == VictoryScene || prev == DefeatScene) {
        		prevScenes.push(MenuScene);
        	}else {
        		prevScenes.push(prev);
        	}
    	}
    }
    
    public void refreshWaitingText() {
    	if(getCurrentRoom() != null && joueursPresents != null) {
	    	Integer members = getCurrentRoom().getMembers(),
	    			membersMax = getCurrentRoom().getMembersMax();
	    	
	        String joueurs = "" + members.toString() + "/" + membersMax.toString() + "\n";
	        System.out.println(joueurs);
	        
	        joueursPresents.setText(joueurs);
	        
        }
    	System.out.println(joueursPresents);
    }
    
    public void setScene(Stage stage, Scene scene) {
    	prevScenes.push(currentScene);
    	stage.setScene(scene);
    	currentScene = scene;
    	enterScene();
    }
    
    public void init(Stage primaryStage) {
    	builder = new SceneBuilder(this);
    	setStage(primaryStage);
    	
        MenuScene = builder.createMenuScene(primaryStage, 600, 300, Color.CADETBLUE);
		GameChooseScene = builder.createGameChooseScene(primaryStage, 600, 300, Color.CADETBLUE);
		RoomChooserScene = builder.createRoomChooserScene(primaryStage, 600, 300, Color.CADETBLUE, crc);
        WaitingScene = builder.createWaitingScene(primaryStage, 600, 300, Color.CADETBLUE, joueursPresents);
		GameScene = builder.createGameScene(primaryStage, 600, 300, Color.CADETBLUE, hideWord);
        ErrorScene = builder.createErrorScene(primaryStage, 600, 300, Color.CRIMSON);
        VictoryScene = builder.createVictoryScene(primaryStage, 600, 300, Color.CADETBLUE);
        DefeatScene = builder.createDefeatScene(primaryStage, 600, 300, Color.CADETBLUE);
        
        setScene(primaryStage, MenuScene);
        primaryStage.show();
        
        this.setHidenWord("_ _ _");
        System.out.println("Init ended");
    }
    
    public void setStage(Stage stage) {
    	this.stage = stage;
    }
    
    public Stage getStage() {
    	return this.stage;
    }
    
    public void setHidenWord(String word) {
    	if(this.hideWord != null)
    		this.hideWord.setText(word);
    }
    
    public String getHidenWord() {
    	if(hideWord != null)
    		return this.hideWord.getText();
    	return null;
    }
    
    public void setHideWord(Text word) {
    		this.hideWord = word;
    }
    
    public Text getHideWord() {
    		return this.hideWord;
    }
    
    public void setJoueursPresents(Text word) {
		this.joueursPresents = word;
	}
	
	public Text getJoueursPresents() {
			return this.joueursPresents;
	}

	public ArrayList<KeyBoardButton> getClavier() {
		return clavier;
	}

	public void setClavier(ArrayList<KeyBoardButton> clavier) {
		this.clavier = clavier;
	}

	public ClientRoom getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(ClientRoom currentRoomId) {
		this.currentRoom = currentRoomId;
	}
}
