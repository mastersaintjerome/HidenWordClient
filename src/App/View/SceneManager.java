/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package App.View;

import App.Core.ClientRoom;
import App.Network.Client;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    private Text joueursPresents, tourJoueur;
    final private ExecutorService executor = Executors.newSingleThreadExecutor();

    Scene MenuScene, SingleGameScene, MultiGameScene, RoomChooserScene, GameChooseScene, WaitingScene, ErrorScene, VictoryScene, DefeatScene;

    private final Client client;
    ClientRoomController crc; 
    SceneBuilder builder;
    Stage stage;

    int tourJoueurI = 0;

    private ClientRoom currentRoom = null;

    Scene currentScene;
    Stack<Scene> prevScenes = new Stack<Scene>();
    private Text hideWordM;
    private Text hideWordS;
        
    public SceneManager(Client client){
        this.client = client;
    }
   
    public void clientConnect(){
        client.connect();
        executor.submit(client);
    }
    
    public boolean clientIsRunning(){
        return client.isRunning();
    }
    
    public void clientSendChar(char c){
        client.clientSendChar(c);
    }
    
    public void clientStartSoloGame(){
        client.clientStartSoloGame(); 
    }
    
    public void clientCreateDuelGame(){
        client.clientCreateDuelGame(); 
    }
    
    public void clientJoinDuelGame(int id){
        client.clientJoinDuelGame(id);
    }
    
    public Map<Integer,Integer> clientgetRooms(){
        return client.getRooms();
    }
    
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
    
    public void griseClavier() {
    	System.out.println("griseClavier");
    	for(KeyBoardButton btn : clavier) {
    		btn.setDisable(true);
    	}
    }
    
    public void restaureClavier() {
    	System.out.println("griseClavier");

    	for(KeyBoardButton btn : clavier) {
    		if( ! btn.isUsed )
    			btn.setDisable(false);
    	}
    }
    
    public void resetKeyBoard() 
    /**
     * Restaure le clavier en mettant toutes les touches actives.
     */
    {
    	for(KeyBoardButton btn : getClavier()) {
    		btn.isUsed = false;
        	btn.disableButton(false);
    	}
    	
    }

    public void DeseableKey(char c) {
    	for (KeyBoardButton btn : getClavier()) {
    		if(btn.getButtonCharactere() == c) {
    			if( ! btn.isDisabled() ) {
    				btn.isUsed = true;
    				btn.disableButton(true);
    			}
    		}
    	}
    }
    
    public void returnLastScene(Stage stage) {
    	Scene scene = prevScenes.pop();
    	
    	if(scene != null) {
    		stage.setScene(scene);
    		currentScene = scene;
    	}
    }
    
    /**
     * Actions a effectuer lors d'une sortie de room
     */
    public void leaveRoom(){
    	
    	
    	
    }
    
    public void changeTour() {
        String text;
        if(client.isIsTurn()){
            text = "A votre tour !";
            restaureClavier();
        }else{
            text = "Au tour du joueur adverse !";
            griseClavier();
        }
    }
  
    public void enterScene() 
    /**
     * Actions à effectuer lorsqu'on entre dans une scene. 
     */
    {
    	if(currentScene == MenuScene || currentScene == ErrorScene) {
    		prevScenes.clear();
    	}
    	else if(currentScene == SingleGameScene) {
    		resetKeyBoard();
    		leaveRoom();
    	}else if(currentScene == WaitingScene) {
    	
    		if(getCurrentRoom() != null)  {
		    	getCurrentRoom().addMember();
		    	if(getCurrentRoom().getMembers() < this.getCurrentRoom().getMembersMax()) {
		    		refreshWaitingText();
		    	}
		    	else {
		    		setScene(getStage(), MultiGameScene);
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
            joueursPresents.setText(joueurs);	        
        }
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
        SingleGameScene = builder.createSingleGameScene(primaryStage, 600, 300, Color.CADETBLUE);
        MultiGameScene = builder.createMultiGameScene(primaryStage, 600, 300, Color.CADETBLUE);
        ErrorScene = builder.createErrorScene(primaryStage, 600, 300, Color.CRIMSON);
        VictoryScene = builder.createVictoryScene(primaryStage, 600, 300, Color.CADETBLUE);
        DefeatScene = builder.createDefeatScene(primaryStage, 600, 300, Color.CADETBLUE);
        
        setScene(primaryStage, MenuScene);
        primaryStage.show();
        
        this.hideWordS.setText("_ _ _");
        this.hideWordM.setText("_ _ _");
        setTourJoueurText("Au tour du joueur " + (tourJoueurI+1));
        
    }
    
    public void setStage(Stage stage) {
    	this.stage = stage;
    }
    
    public Stage getStage() {
    	return this.stage;
    }
    
    public void setHidenWord() {
    	if(client != null){
            hideWordS.setText(client.getSearchWord());
            hideWordM.setText(client.getSearchWord());
        }
    }
    
    public void setTourJoueurText(String text) {
    	if(getTourJoueur() != null)
            getTourJoueur().setText(text);
    }
    
    public void setHideWordS(Text word) {
        this.hideWordS = word;
    }
    
    public Text getHideWordS() {
        return this.hideWordS;
    }
    
    public Text getHideWordM() {
        return this.hideWordM;
    }
    
    public void setHideWordM(Text hideWord) {
         this.hideWordM = hideWord;
    }
    
    public Text getTourJoueur() {
    	return this.tourJoueur;
    }
    
    public void setTourJoueurText(Text tour) {
    	this.tourJoueur = tour;
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
