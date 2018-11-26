/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package App.View;

import javafx.scene.control.Button;
/*
 * KeyBoard Button extends Button, class to get char from a button
 * @author Gaëtan Perrot, Barbarian
 */
public class KeyBoardButton extends Button {

	private int buttonNumber = 0;
	private char buttonChar = 0;
	public boolean isUsed = false;
	
	public KeyBoardButton(char c) {
		this.buttonChar = c;
		this.buttonNumber = ((int) c) - 65;
	}
	
	public void setLayoutPosition(int x, int y) {
		this.setLayoutX(x);
		this.setLayoutY(y);
	}
	
	public void setLayoutPositionWithText(int x, int y, String text) {
		this.setLayoutPosition(x, y);
		this.setText(text);
	}
	
	public int getButtonNumber() {
		return this.buttonNumber;
	}
	
	public char getButtonCharactere() {
		return this.buttonChar;
	}

	public void disableButton(boolean value) {
		this.setDisable(value);
	}
	
	
	
}
