package App.Network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

/**
 *
 * @author Gaëtan Perrot, Barbarian
 */
public class Client {
    String sentence = "";
    String serverSentence = "";
    private String pseudo;
    Socket clientSocket;
    public boolean running  = false;

    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

    DataOutputStream outToServer;
    BufferedReader inFromServer;

    public void connect(String location, int port) throws UnknownHostException, IOException {
            clientSocket = new Socket(location, port);

            outToServer = new DataOutputStream(getSocket().getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
            running  = true;
    }

    public Socket getSocket() {
            return this.clientSocket;
    }

    public void setSentence() throws IOException {
            sentence = inFromUser.readLine();
    }

    public String getSentence() {
            return this.sentence;
    }

    public void sendToServer(String sentence) throws IOException {
            outToServer.writeBytes(sentence + '\n');
    }

    public void sendSentenceToServer() throws IOException {
            outToServer.writeBytes(getSentence() + '\n');
    }

    public void talkToServer() throws IOException {
            setSentence();
            this.sendSentenceToServer();
    }

    public void readServerAnswer() throws IOException {
            serverSentence = inFromServer.readLine();
    }

    public String getServerAnswer() throws IOException {
            this.readServerAnswer();
            return this.serverSentence;
    }

    public void stopConnection() throws IOException {
            sendToServer("CLOSE");
            getSocket().close();
            running  = false;
    }

    public boolean isSentenceEqualTo(String string) {
            return sentence.equals(string);
    }

    public boolean isConnected() {
            return this.running;
    }
}
