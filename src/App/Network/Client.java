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
	  
	  Socket clientSocket;
	  public boolean connected = false;
	  
	  BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
	  
	  DataOutputStream outToServer;
	  BufferedReader inFromServer;
	  
	  public void connect(String location, int port) throws UnknownHostException, IOException {
		  clientSocket = new Socket(location, port);

		  outToServer = new DataOutputStream(getSocket().getOutputStream());
		  inFromServer = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
		  connected = true;
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
		  connected = false;
	  }
	  
	  public boolean isSentenceEqualTo(String string) {
		  return sentence.equals(string);
	  }
	  
	  public boolean isConnected() {
		  return this.connected;
	  }
	  
	  public static void main(String[] argv){
		  Client client = new Client();

		  try {
			  client.connect("127.0.0.1", 5000);
			  System.out.println("Client connecté");
			  
			  String state = "CONT";
			  String word = "";
			  
			  do{
				  	word = client.getServerAnswer();
					System.out.println(word); // Reception du mot
					//System.out.println("Il vous reste " + client.getServerAnswer() + " tentatives." );
	 
				  	System.out.println("Veuillez entrer un caractère : ");
				  	client.talkToServer();				// Envoi du caractere
					  
					state = client.getServerAnswer();
			
			  } while( state.equals("CONT") );
			  
			  if(state.equals("WIN")) {
				  System.out.println("Bravo, Vous avez trouver le mot : " + word);
			  }
			  else if(state.equals("LOSE")) {
				  System.out.println("Dommage, vous avez perdu.(Looser...)");
			  }
			  
			  client.stopConnection();
			  
		  	} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	  }
}
