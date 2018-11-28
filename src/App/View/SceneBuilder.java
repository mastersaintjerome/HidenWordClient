/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App.View;

import App.Core.ClientRoom;
import java.awt.event.ActionListener;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.Timer;

/**
 * Permet de créer les scenes de la partie graphique du client.
 *
 * @author Gaëtan Perrot, Barbaria
 */
public class SceneBuilder {

    private SceneManager manager;
    private Timer updateHidenWord;
    private Timer updateTour;
    
    
    public SceneBuilder(final SceneManager manager) {
        this.manager = manager;
        updateHidenWord = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                manager.setHidenWord();
                if(manager.clientIsWinning()){
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            manager.victoryScene(manager.getStage());
                        }
                    });
                }else if(manager.clientIsLost()){
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            manager.defeatScene(manager.getStage());
                        }
                    });
                }
            }
        });
        updateHidenWord.setRepeats(true);
        updateHidenWord.start(); // Go go go!
        
        updateTour = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if(manager.clientIsGameRun()){
                    if(manager.currentScene == manager.WaitingScene){
                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                manager.setScene(manager.getStage(), manager.MultiGameScene);
                            }
                        });
                    }
                    manager.changeTour();
                }
                if(manager.clientIsRunning()){
                    manager.clientAskGamesRoom(); 
                }
            }
        });
        updateTour.setRepeats(true);
        updateTour.start(); // Go go go!
    }

    public void createClavier(Group root, int initPosX, int initPosY) /*
     * Création d'un clavier 
     */ {
        int ecartX = 0, ecartY = 0;
        int i;
        for (i = 0; i < 26; i++) {
            char c = (char) ((char) i + 97);
            String s = "";
            s += c + " ";

            final KeyBoardButton btn = new KeyBoardButton(c);

            btn.setPrefSize(45, 45);

            if (i % 7 == 0) {
                ecartX = 0;
                ecartY += 1;
            }
            btn.setLayoutPositionWithText(initPosX + (45 * ecartX++), initPosY + (45 * ecartY), s);
            btn.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    if (manager.clientIsRunning()) {
                        btn.isUsed = !btn.isUsed;
                        btn.disableButton(btn.isUsed);
                        manager.clientSendChar(btn.getButtonCharactere());
                        manager.changeTour();
                    }
                }
            });
            manager.getClavier().add(btn);
            root.getChildren().add(btn);
        }
    }
    /*
     * Création des boutons du menu
     */

    public void createMenuButtons(final Stage primaryStage, Group root) {
        Button btnPlay = new Button();
        btnPlay.setText("Jouer");
        btnPlay.setFont(new Font(25));

        btnPlay.setLayoutX(100);
        btnPlay.setLayoutY(200);

        btnPlay.setOnAction(new EventHandler<ActionEvent>() {
            Stage stage = primaryStage;

            @Override
            public void handle(ActionEvent event) {
                manager.clientConnect();
                manager.setScene(stage, manager.GameChooseScene);
                stage.show();
            }
        });
        root.getChildren().add(btnPlay);

        Button btnQuit = new Button();
        btnQuit.setText("Quitter");
        btnQuit.setFont(new Font(25));

        btnQuit.setLayoutX(400);
        btnQuit.setLayoutY(200);

        btnQuit.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                manager.clientStop();
                System.exit(0);
            }
        });
        root.getChildren().add(btnQuit);
    }

    public void createRoomChooserButtons(final Stage primaryStage, Group root, ClientRoomController crc) /*
     * Création des boutons permettant de choisir la Room a acceder.
     */ {
        if (crc != null) {
       
            Button btnCreer = new Button();
            btnCreer.setPrefSize(327, 40);
            btnCreer.setText("Créer une room.");
            btnCreer.setFont(new Font(23));

            btnCreer.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    manager.clientCreateDuelGame();
                    manager.setScene(primaryStage, manager.WaitingScene);
                }
            });
            //root.getChildren().add(btnCreer);
            
            ScrollPane s1 = new ScrollPane();
            s1.setLayoutX(148);
            s1.setLayoutY(60);
            s1.setPrefSize(327, 240);

            VBox vBox = new VBox();
            manager.setRoomContainer(vBox);
            vBox.getChildren().add(btnCreer);
            manager.clientgetRoomsForClientRoomController(crc);
            for (final ClientRoom room : crc.getClientRooms()) {
                Button btn = new Button();

                String roomLabel = "Room " + room.getId() + "    Joueur(s) : " + room.getMembers() + "/" + room.getMembersMax();

                btn.setPrefSize(327, 40);
                btn.setText(roomLabel);
                btn.setFont(new Font(23));

                btn.setOnAction(new EventHandler<ActionEvent>() {
                    ClientRoom cRoom = room;

                    public void handle(ActionEvent event) {
                        manager.clientJoinDuelGame(cRoom.getId());
                        manager.setCurrentRoom(cRoom);
                        manager.setScene(primaryStage, manager.WaitingScene);
                    }
                });
                vBox.getChildren().add(btn);

            }
            s1.setContent(vBox);
            root.getChildren().add(s1);
        }
    }

    public void createChooseButtons(final Stage primaryStage, Group root) /*
     * Création des boutons permettant de choisir un type de partie.
     */ {
        int preferedW = 250, preferedH = 40;
        int decX = 180;

        Button btnSolo = new Button();
        btnSolo.setText("Partie Mono-Joueur");
        btnSolo.setFont(new Font(18));

        btnSolo.setLayoutX(decX);
        btnSolo.setLayoutY(100);
        btnSolo.setPrefSize(preferedW, preferedH);

        btnSolo.setOnAction(new EventHandler<ActionEvent>() {
            Stage stage = primaryStage;
            
            @Override
            public void handle(ActionEvent event) {
                manager.clientStartSoloGame();
                manager.setScene(stage, manager.SingleGameScene);
                stage.show();
            }
        });
        root.getChildren().add(btnSolo);

        Button btnDuel = new Button();
        btnDuel.setText("Partie Duel");
        btnDuel.setFont(new Font(18));

        btnDuel.setLayoutX(decX);
        btnDuel.setLayoutY(150);
        btnDuel.setPrefSize(preferedW, preferedH);

        btnDuel.setOnAction(new EventHandler<ActionEvent>() {
            Stage stage = primaryStage;

            public void handle(ActionEvent event) {
                manager.setScene(stage, manager.RoomChooserScene);
                stage.show();
            }
        });
        root.getChildren().add(btnDuel);
    }

    /**
     * Création des bouttons du menu d'erreur
     */
    public void createErrorButtons(final Stage primaryStage, Group root) {
        Button btnRetry = new Button();
        btnRetry.setText("Ressayer");
        btnRetry.setFont(new Font(25));

        btnRetry.setLayoutX(100);
        btnRetry.setLayoutY(200);

        btnRetry.setOnAction(new EventHandler<ActionEvent>() {
            Stage stage = primaryStage;

            public void handle(ActionEvent event) {
                if(!manager.clientIsRunning()){
                    manager.clientConnect();
                }
                stage.show();
            }
        });
        root.getChildren().add(btnRetry);

        Button btnQuit = new Button();
        btnQuit.setText("Quitter");
        btnQuit.setFont(new Font(25));

        btnQuit.setLayoutX(400);
        btnQuit.setLayoutY(200);

        btnQuit.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
        root.getChildren().add(btnQuit);
    }

    public void createEndGameButtons(final Stage primaryStage, Group root) /**
     * Création des bouttons du menu de Victoire
     */
    {
        Button btnMenuG = new Button();
        btnMenuG.setText("Retour au menu de Jeu");
        btnMenuG.setFont(new Font(15));

        btnMenuG.setLayoutX(100);
        btnMenuG.setLayoutY(200);

        btnMenuG.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                manager.clientResetGame();
                manager.setScene(primaryStage, manager.GameChooseScene);
            }
        });
        root.getChildren().add(btnMenuG);

        Button btnMenu = new Button();
        btnMenu.setText("Retour au menu principale");
        btnMenu.setFont(new Font(15));

        btnMenu.setLayoutX(300);
        btnMenu.setLayoutY(200);

        btnMenu.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                manager.clientResetGame();
                manager.setScene(primaryStage, manager.MenuScene);
            }
        });
        root.getChildren().add(btnMenu);
    }

    public void addReturnButton(final Stage stage, Group root) {
        Button btnRet = new Button();
        btnRet.setText("<");
        btnRet.setFont(new Font(15));

        btnRet.setPrefSize(40, 40);

        btnRet.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                manager.returnLastScene(stage);
            }
        });
        root.getChildren().add(btnRet);
    }

    public void addVictoryButton(final Stage stage, Group root) {
        Button btnRet = new Button();
        btnRet.setText("V");
        btnRet.setFont(new Font(15));

        btnRet.setLayoutY(20);

        btnRet.setPrefSize(5, 10);

        btnRet.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                manager.victoryButton(stage);
            }
        });
        root.getChildren().add(btnRet);
    }

    public void addDefeatButton(final Stage stage, Group root) {
        Button btnRet = new Button();
        btnRet.setText("D");
        btnRet.setFont(new Font(15));

        btnRet.setPrefSize(5, 10);
        btnRet.setLayoutY(50);
        btnRet.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                manager.defeatButton(stage);
            }
        });
        root.getChildren().add(btnRet);
    }

    public void createTextInArea(Group root, String textContent, int x, int y, int fontSize) /**
     * Permet de creer une boite de texte.
     */
    {
        Text text = new Text();

        text.setFont(new Font(fontSize));
        text.setText(textContent);

        text.setLayoutX(x);
        text.setLayoutY(y);

        root.getChildren().add(text);
    }

    public Scene createSingleGameScene(Stage stage, int width, int height, Color color) /**
     * Création de la scene de jeu.
     */
    {
        Group root = new Group();
        Scene GameScene = new Scene(root, width, height, color);

        createClavier(root, 40, 100);
        addReturnButton(stage, root);

        Text hideWord = new Text();

        hideWord.setFont(new Font(35));
        hideWord.setText("_____");

        hideWord.setLayoutX(50);
        hideWord.setLayoutY(80);

        root.getChildren().add(hideWord);
        manager.setStage (stage);
        manager.setHideWordS(hideWord);

        return GameScene;
    }

    public Scene createMultiGameScene(Stage stage, int width, int height, Color color) /**
     * Création de la scene de jeu.
     */
    {
        Group root = new Group();
        Scene GameScene = new Scene(root, width, height, color);

        createClavier(root, 40, 100);
        addReturnButton(stage, root);

        Text hideWord = new Text();

        hideWord.setFont(new Font(35));
        hideWord.setText("A votre Tour");

        hideWord.setLayoutX(50);
        hideWord.setLayoutY(80);

        root.getChildren().add(hideWord);

        Text tourJoueur = new Text();

        tourJoueur.setFont(new Font(15));
        tourJoueur.setText("Au tour de X");

        tourJoueur.setLayoutX(400);
        tourJoueur.setLayoutY(30);

        root.getChildren().add(tourJoueur);
        manager.setStage (stage);
        manager.setHideWordM(hideWord);
        manager.setTourJoueurText(tourJoueur);

        return GameScene;
    }

    public Scene createWaitingScene(Stage stage, int width, int height, Color color, Text joueurPresents) /**
     * Création de la scene de jeu.
     */
    {
        Group root = new Group();
        Scene WaitingScene = new Scene(root, width, height, color);

        createTextInArea(root, "En attente d'autres joueurs...", 120, height / 4 + 25, 30);

        joueurPresents = new Text();

        joueurPresents.setFont(new Font(30));
        joueurPresents.setText("1/2");

        joueurPresents.setLayoutX(width / 2);
        joueurPresents.setLayoutY(height / 4 + 50);

        root.getChildren().add(joueurPresents);

        manager.setJoueursPresents(joueurPresents);

        addReturnButton(stage, root);

        return WaitingScene;
    }

    public Scene createMenuScene(Stage primaryStage, int width, int height, Color color) /**
     * Création de la scene du menu principale du jeu.
     */
    {
        Group root = new Group();
        Scene MenuScene = new Scene(root, width, height, color);

        createTextInArea(root, "Mot Mystere", 130, height / 4 + 25, 60);
        createMenuButtons(primaryStage, root);

        return MenuScene;
    }

    public Scene createRoomChooserScene(Stage primaryStage, int width, int height, Color color, ClientRoomController crc) /**
     * Création du menu permettant de choisir la room a acceder.
     */
    {
        crc.getAllRoomsFromServers(manager.clientgetRooms());
        Group root = new Group();

        Scene RoomChooserScene = new Scene(root, width, height, color);

        createTextInArea(root, "Choix de la Room :", 165, 50, 30);
        createRoomChooserButtons(primaryStage, root, crc);
        addReturnButton(primaryStage, root);

        return RoomChooserScene;
    }

    public Scene createGameChooseScene(Stage primaryStage, int width, int height, Color color) /**
     * Création du menu permettant de choisir le type de jeu souhaiter.
     */
    {
        Group root = new Group();
        Scene GameChooseScene = new Scene(root, width, height, color);

        createTextInArea(root, "Choix du mode de jeu :", 100, 75, 40);
        createChooseButtons(primaryStage, root);
        addReturnButton(primaryStage, root);

        return GameChooseScene;

    }

    public Scene createErrorScene(Stage primaryStage, int width, int height, Color color) /**
     * Création du la scene d'erreur
     */
    {
        Group root = new Group();
        Scene ErrorScene = new Scene(root, width, height, color);

        createTextInArea(root, "Error", 50, 80, 50);
        createTextInArea(root, "Une erreur est survenue lors de la connexion au server.", 40, 130, 15);

        createErrorButtons(primaryStage, root);

        return ErrorScene;
    }

    /**
     * Création du la scene d'erreur
     */
    public Scene createVictoryScene(Stage primaryStage, int width, int height, Color color) {
        Group root = new Group();
        Scene VictoryScene = new Scene(root, width, height, color);

        createTextInArea(root, "Victoire", 200, 80, 50);

        createTextInArea(root, "Vous avez gagner la partie ! Félicitation.", 100, 150, 20);

        createEndGameButtons(primaryStage, root);

        return VictoryScene;
    }

    /**
     * Création du la scene d'erreur
     */
    public Scene createDefeatScene(Stage primaryStage, int width, int height, Color color) {
        Group root = new Group();
        Scene DefeatScene = new Scene(root, width, height, color);

        createTextInArea(root, "Perdu", 200, 80, 60);

        createTextInArea(root, "Vous ferez mieux la prochaine fois !", 125, 150, 20);

        createEndGameButtons(primaryStage, root);

        return DefeatScene;

    }

}
